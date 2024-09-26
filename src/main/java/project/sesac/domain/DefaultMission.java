package project.sesac.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class DefaultMission {

    @Id @GeneratedValue
    private Long id;

    private String mission1;
    private String mission2;

    public DefaultMission(String mission1, String mission2) {
        this.mission1 = mission1;
        this.mission2 = mission2;
    }

}
