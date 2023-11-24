package core.queue;

import core.Repository.SdaMainMasRepository;
import core.domain.SdaMainMas;
import core.dto.*;
import core.service.MtcSdaMainMasService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class SdaMainMasRequestConsumer {
    private static final Logger log = LoggerFactory.getLogger(SdaMainMasRequestConsumer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final WebClient webClient;
    private final SdaMainMasRepository sdaMainMasRepository;
    private final MtcSdaMainMasService mainMasService;

    @KafkaListener(topics = "mtc.ncr.comRequest", groupId="practice22201785")
    public void consumeMessage(@Payload MtcNcrUpdateMainMasRequest updateRequest ,
                               @Header(name = KafkaHeaders.RECEIVED_KEY , required = false) String key ,
                               @Header(KafkaHeaders.RECEIVED_TOPIC ) String topic ,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp ,
                               @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("############sda main mas 구독시작한다###############{}" , updateRequest.toString());
        MtcResultRequest resultDto = new MtcResultRequest();
        try
        {
            for(int i = 0 ; i < updateRequest.getRequestSubList().size() ; i++)
            {
                MtcNcrSdaMainMasResponse mainMasResponse = mainMasService.getMainMas(new MtcNcrSdaMainMasRequest(updateRequest.getAcno() , updateRequest.getRequestSubList().get(i).getCur_c(),""));
                if(( updateRequest.getRequestSubList().get(i).getSign() < 0 ) //출금요청이면서
                    && (mainMasResponse.getAc_jan() < updateRequest.getRequestSubList().get(i).getTrxAmt())) //잔액이 부족한경우
                {
                    // result Queue에도 넣는다.
                    kafkaTemplate.send("mtc.ncr.result", "PAY" ,
                            new MtcResultRequest( updateRequest.getAcno() ,
                                    updateRequest.getRequestSubList().get(i).getTrxdt(),
                                    updateRequest.getRequestSubList().get(i).getCur_c() ,
                                    2,
                                    "",
                                    updateRequest.getRequestSubList().get(i).getTrxAmt(),
                                    mainMasResponse.getAc_jan() /*거래전잔액으로 셋팅한다*/,
                                    "출금요청 금액이 잔액보다 큽니다." ,
                                    new MtcNcrPayRequest() /*payinfo 는 null로 셋팅*/,
                                    updateRequest.getGid()
                                    )
                            );
                    break; // 더이상 진행하지 않음
                }
                else
                {
                    //결제를 시도한다.
                    int result = mainMasService.updateMainMas(updateRequest.getRequestSubList().get(i) , updateRequest.getAcno());
                    if( result == 1 ) { //성공
                        // result 큐에 성공으로 넣음
                    }
                    else {
                        // result 큐에 실패로 넣음
                        break; // 더이상 진행하지 않음
                    }
                }
            }
        }
        catch (Exception e)
        {
            // result 큐에 실패로 넣음
        }
    }
}
