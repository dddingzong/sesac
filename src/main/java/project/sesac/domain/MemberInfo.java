package project.sesac.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class MemberInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_id")
    private Long id;

    private String name;

    @ColumnDefault("0")
    private int exp; //경험치 총합

    @ColumnDefault("0")
    private int point; // point 총합

    private int chooseRole; // 0이면 복지만 1이면 취업만 2이면 모두

    @Column(columnDefinition = "boolean default false")
    boolean clearMission1;

    @Column(columnDefinition = "boolean default false")
    boolean clearMission2;

    public MemberInfo(String name, int chooseRole) {
        this.name = name;
        this.chooseRole = chooseRole;
    }
}
