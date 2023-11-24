package core.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

}
