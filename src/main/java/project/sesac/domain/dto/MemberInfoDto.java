package project.sesac.domain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberInfoDto {

    private String loginId;

    private String loginPassword;

    public MemberInfoDto(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }
}
