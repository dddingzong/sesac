package project.sesac.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserUpdateDto {

    private String loginPassword;
    private String chooseRole;

    public UserUpdateDto(String loginPassword, String chooseRole) {
        this.loginPassword = loginPassword;
        this.chooseRole = chooseRole;
    }
}
