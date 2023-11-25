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

    public int getUpmug(String svcid , String successYn)
    {
        if("PAY".equals(svcid)&&"SUCCESS".equals(successYn)) return 1;
        if("EXG".equals(svcid)&&"SUCCESS".equals(successYn)) return 2;
        if("PAY".equals(svcid)&&"FAIL".equals(successYn)) return 3;
        if("EXG".equals(svcid)&&"FAIL".equals(successYn)) return 4;

        return 0 ;
    }

    @KafkaListener(topics = "mtc.ncr.comRequest", groupId="practice22201785")
    public void consumeMessage(@Payload MtcNcrUpdateMainMasRequest updateRequest ,
                               @Header(name = KafkaHeaders.RECEIVED_KEY , required = false) String key ,
                               @Header(KafkaHeaders.RECEIVED_TOPIC ) String topic ,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp ,
                               @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("############sda main mas 구독시작한다###############{}" , updateRequest.toString());
        MtcResultRequest resultDto = new MtcResultRequest();
        for(int i = 0 ; i < updateRequest.getRequestSubList().size() ; i++)
        {
            MtcResultRequest resultRequest
                    = new MtcResultRequest( updateRequest.getAcno() , updateRequest.getRequestSubList().get(i).getTrxdt(),
                    updateRequest.getRequestSubList().get(i).getCur_c() ,
                    0, /*업무구분은 비워둔다*/
                    updateRequest.getAprvSno() /*승인번호*/,
                    updateRequest.getRequestSubList().get(i).getTrxAmt() /*요청금액*/,
                    0.0 /*거래전잔액으로 셋팅한다*/,
                    "" /*에러메세지는 비워둔다*/,
                    updateRequest.getPayInfo(),
                    updateRequest.getGid());

            try
            {
                MtcNcrSdaMainMasResponse mainMasResponse =
                        mainMasService.getMainMas( new MtcNcrSdaMainMasRequest(updateRequest.getAcno(),
                                updateRequest.getRequestSubList().get(i).getCur_c() , updateRequest.getGid()));

                resultRequest.setNujkJan(mainMasResponse.getAc_jan()); // 누적잔액 set

                if(( updateRequest.getRequestSubList().get(i).getSign() < 0 ) //출금요청이면서
                    && (mainMasResponse.getAc_jan() < updateRequest.getRequestSubList().get(i).getTrxAmt())) //잔액이 부족한 경우
                {
                    // result Queue에 넣는다.
                    resultRequest.setUpmuG(getUpmug(updateRequest.getSvcId(), "FAIL"));
                    resultRequest.setErrMsg("잔액보다 충전시도 금액이 더 큽니다");
                    log.info("$$ insert value to result topic : {}" , resultRequest.toString());
                    kafkaTemplate.send("mtc.ncr.result", "FAIL" , resultRequest);
                    break; // 더이상 진행하지 않음
                }
                else
                {
                    //결제를 시도한다.
                    int result = mainMasService.updateMainMas(updateRequest.getRequestSubList().get(i) , updateRequest.getAcno());

                    log.info("$$$ update Main Mas return value -----> {}" , result == 1 ? "성공" : "실패");

                    if( result == 1 ) { //성공
                        // result 큐에 성공으로 넣음
                        resultRequest.setUpmuG(getUpmug(updateRequest.getSvcId(), "SUCCESS"));
                        log.info("$$ insert value to result topic : {}" , resultRequest.toString());
                        kafkaTemplate.send("mtc.ncr.result", "SUCCESS" , resultRequest);
                    }
                    else {
                        // result 큐에 실패로 넣음
                        resultRequest.setUpmuG(getUpmug(updateRequest.getSvcId(), "FAIL"));
                        log.info("$$ insert value to result topic : {}" , resultRequest.toString());
                        kafkaTemplate.send("mtc.ncr.result", "FAIL" , resultRequest);
                        break; // 더이상 진행하지 않음
                    }
                }
            }
            catch (Exception e)
            {
                // result 큐에 실패로 넣음
                resultRequest.setUpmuG(getUpmug(updateRequest.getSvcId(), "FAIL"));
                log.info("$$ insert value to result topic : {}" , resultRequest.toString());
                kafkaTemplate.send("mtc.ncr.result", "FAIL" , resultRequest);
            }
        }

    }
}
