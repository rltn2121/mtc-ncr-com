package core.service;

import core.domain.SdaMainMas;
import lombok.RequiredArgsConstructor;
import core.Repository.SdaMainMasRepository;
import core.dto.MtcNcrSdaMainMasRequest;
import core.dto.MtcNcrSdaMainMasResponse;
import core.domain.SdaMainMasId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MtcSdaMainMasService {
    private final SdaMainMasRepository sdaMainMasRepository;
    private static final Logger log = LoggerFactory.getLogger(MtcSdaMainMasService.class);
    public MtcNcrSdaMainMasResponse getMainMas(MtcNcrSdaMainMasRequest requestInfo)
    {
        MtcNcrSdaMainMasResponse sdaMainMasResponse = new MtcNcrSdaMainMasResponse();
        try
        {
            //sda_main_mas에서 과목코드와 계좌번호로 값을 읽어온다.
            SdaMainMas sdaMainMas = this.sdaMainMasRepository
                    .findById(new SdaMainMasId(requestInfo.getAcno(), requestInfo.getCurC())).get();
           log.info("$$$sda_main_mas {} " , sdaMainMas.toString());
            sdaMainMasResponse.setGid(requestInfo.getGid());
            sdaMainMasResponse.setAcno(sdaMainMas.getAcno());
            sdaMainMasResponse.setCur_c(sdaMainMas.getCur_c());
            sdaMainMas.setAc_jan(sdaMainMas.getAc_jan());
        }
        catch( Exception e)
        {
            log.info("$$$$$read 실패 : [{}]" , e.toString());
        }
        return sdaMainMasResponse;
    }
}
