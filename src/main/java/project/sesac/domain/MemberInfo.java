package project.sesac.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Entity
public class MemberInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_id")
    private Long id;

    private String name;

    @ColumnDefault("0")
    private int exp; //경험치 총합

    @ColumnDefault("0")
    private int point; // point 총합

    private int chooseRole; // 0이면 정책만 1이면 둘 다

    public MemberInfo(String name, int exp, int point, int chooseRole) {
        this.name = name;
        this.exp = exp;
        this.point = point;
        this.chooseRole = chooseRole;
    }
}
