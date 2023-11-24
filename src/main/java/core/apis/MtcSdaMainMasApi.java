package core.apis;

import core.dto.MtcNcrSdaMainMasRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface MtcSdaMainMasApi {
    @GetMapping("/{acno}/{cur_c}/{gid}")
    //ResponseEntity<?> getSdaMainMas(@RequestBody MtcNcrSdaMainMasRequest comRequest);
    ResponseEntity<?> getSdaMainMas(@PathVariable String acno , @PathVariable String cur_c , @PathVariable String gid);

}
