package project.sesac.domain.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDto {

    private String name;

    private String loginId;

    private String loginPassword;

    private String loginPassword_confirm;

    private String chooseRole; // 0이면 복지만 1이면 취업만 2이면 모두

    public MemberDto(String name, String loginId, String loginPassword, String loginPassword_confirm, String chooseRole) {
        this.name = name;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.loginPassword_confirm = loginPassword_confirm;
        this.chooseRole = chooseRole;
    }
}
