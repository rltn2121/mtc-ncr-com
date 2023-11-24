package core.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class MtcResultRequest {
    private String acno;
    private String trxdt;
    private String curC;
    private int upmuG;
    private String aprvSno;
    private Double trxAmt;
    private Double nujkJan;
    private String errMsg;
    private MtcNcrPayRequest payinfo;
    private String gid;

    public MtcResultRequest(String acno, String curC, int trxAmt, double acJan, String s, MtcNcrPayRequest mtcNcrPayRequest, String gid) {
    }
}
