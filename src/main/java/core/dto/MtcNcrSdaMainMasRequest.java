package core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MtcNcrSdaMainMasRequest {
    //계좌번호
     private String acno;

     //통화코드
    private String curC;

    //global id
    private String gid;

}
