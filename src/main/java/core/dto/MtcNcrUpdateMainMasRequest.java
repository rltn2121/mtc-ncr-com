package core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MtcNcrUpdateMainMasRequest {
    private String acno;
    private String gid;
    private String aprvSno;
    private List<MtcNcrUpdateMainMasRequestSub> requestSubList;
    private MtcNcrPayRequest payInfo;
    private String svcId;
}
