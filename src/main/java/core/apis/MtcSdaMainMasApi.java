package core.apis;

import core.dto.MtcNcrSdaMainMasRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface MtcSdaMainMasApi {
    @GetMapping("/sdaMainMas")
    ResponseEntity<?> getSdaMainMas(@RequestBody MtcNcrSdaMainMasRequest comRequest);
}
