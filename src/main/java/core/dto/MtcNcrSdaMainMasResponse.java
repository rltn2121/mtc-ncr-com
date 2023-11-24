package core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MtcNcrSdaMainMasResponse {
    //계좌번호
     private String acno;

     //통화코드
    private String cur_c;

    //계좌 잔액
    private double ac_jan;

    //global id
    private String gid;
}
