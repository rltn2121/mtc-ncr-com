package core.apis;

import core.dto.MtcNcrSdaMainMasRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface MtcSdaMainMasApi {
    @GetMapping("/{acno}")
    ResponseEntity<?> getSdaMainMas(@PathVariable("acno") String acno,
                                    @RequestParam(value = "cur_c") String cur_c);
}
