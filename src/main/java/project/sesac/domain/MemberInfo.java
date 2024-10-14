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

    @ColumnDefault("0")
    private int pointRole;

    public MemberInfo(String name, int chooseRole) {
        this.name = name;
        this.chooseRole = chooseRole;
    }

    public void mission1ToTrue(){
        this.clearMission1 = true;
    }

    public void mission2ToTrue(){
        this.clearMission2 = true;
    }

    public void mission1ToFalse(){
        this.clearMission1 = false;
    }

    public void mission2ToFalse(){
        this.clearMission2 = false;
    }

    public void plusPoint(int number){
        this.point = this.point + number;
    }

    public void minusPoint(int number){
        this.point = this.point - number;
    }

    public void changePointRole(int pointRole){
        this.pointRole = pointRole;
    }

    public void plusExp(){
        this.exp = this.exp + 40;
    }

    public void plusExpInLastLogic(int number){
        this.exp = this.exp + number;
    }

    public void changeChooseRole(int newChooseRole){
        this.chooseRole = newChooseRole;
    }

}
