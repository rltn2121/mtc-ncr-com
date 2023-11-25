package core.apis;

import core.dto.MtcNcrPayRequest;
import core.dto.MtcNcrSdaMainMasRequest;
import core.dto.MtcNcrUpdateMainMasRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface MtcSdaMainMasApi {
    @GetMapping("/{acno}")
    ResponseEntity<?> getSdaMainMas(@PathVariable("acno") String acno,
                                    @RequestParam(value = "cur_c") String cur_c);

}
