package project.sesac.domain.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MemberDto {

    private String name;

    private String loginId;

    private String loginPassword;

    private String loginPassword_confirm;

    private String chooseRole;

    public MemberDto(String name, String loginId, String loginPassword, String loginPassword_confirm, String chooseRole) {
        this.name = name;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.loginPassword_confirm = loginPassword_confirm;
        this.chooseRole = chooseRole;
    }
}
