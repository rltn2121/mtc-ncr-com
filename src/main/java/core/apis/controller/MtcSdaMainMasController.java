package core.apis.controller;

import core.apis.MtcSdaMainMasApi;
import core.dto.MtcNcrSdaMainMasRequest;
import core.dto.MtcNcrSdaMainMasResponse;
import core.service.MtcSdaMainMasService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping(value="/sdaMainMas", method= RequestMethod.GET, consumes="application/json;")
@RequestMapping("/sdaMainMas")
@RequiredArgsConstructor
public class MtcSdaMainMasController implements MtcSdaMainMasApi {

    private final static Logger log = LoggerFactory.getLogger(MtcSdaMainMasController.class);
    private final MtcSdaMainMasService sdaMainMasService;

    @Override
    public ResponseEntity<?> getSdaMainMas(String acno, String cur_c) {
        MtcNcrSdaMainMasResponse mainMasResponse = new MtcNcrSdaMainMasResponse();
        MtcNcrSdaMainMasRequest comRequest = new MtcNcrSdaMainMasRequest(acno,cur_c,"gid123");
        mainMasResponse = sdaMainMasService.getMainMas(comRequest);
        return ResponseEntity.ok(mainMasResponse);
    }
}

