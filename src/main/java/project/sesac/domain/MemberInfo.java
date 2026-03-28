package project.sesac.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import project.sesac.domain.converter.InformationPreferenceConverter;
import project.sesac.domain.converter.MissionStageConverter;
import project.sesac.domain.type.GrowthLevel;
import project.sesac.domain.type.InformationPreference;
import project.sesac.domain.type.MissionStage;
import project.sesac.domain.type.MissionType;

@Getter
@NoArgsConstructor
@Entity
@ToString(exclude = "member")
public class MemberInfo {

    @Id
    @Column(name = "main_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "main_id")
    private Member member;

    private String name;

    @ColumnDefault("0")
    private int exp; //경험치 총합

    @ColumnDefault("0")
    private int point; // point 총합

    @Column(name = "choose_role")
    @Convert(converter = InformationPreferenceConverter.class)
    private InformationPreference informationPreference; // 0이면 복지만 1이면 취업만 2이면 모두

    @Column(columnDefinition = "boolean default false")
    boolean clearMission1;

    @Column(columnDefinition = "boolean default false")
    boolean clearMission2;

    @Column(name = "point_role")
    @ColumnDefault("0")
    @Convert(converter = MissionStageConverter.class)
    private MissionStage missionStage;

    public MemberInfo(Member member, String name, InformationPreference informationPreference) {
        this.member = member;
        this.name = name;
        this.informationPreference = informationPreference;
        this.missionStage = MissionStage.DEFAULT;
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

    public void completeMission1(MissionType missionType) {
        this.clearMission1 = true;
        rewardMission(missionType);
    }

    public void completeMission2(MissionType missionType) {
        this.clearMission2 = true;
        rewardMission(missionType);
    }

    public void settleMissionDay(MissionType mission1Type, MissionType mission2Type) {
        settleSingleMission(clearMission1, mission1Type);
        settleSingleMission(clearMission2, mission2Type);
        this.clearMission1 = false;
        this.clearMission2 = false;
        refreshMissionStage();
    }

    public void changeChooseRole(InformationPreference newPreference){
        this.informationPreference = newPreference;
    }

    public InformationPreference preferredInformation() {
        return informationPreference;
    }

    public MissionStage missionStage() {
        return missionStage;
    }

    public int getChooseRole() {
        return informationPreference.code();
    }

    public int getPointRole() {
        return missionStage.code();
    }

    public GrowthLevel currentLevel() {
        return GrowthLevel.fromExp(exp);
    }

    public int progressPercent() {
        return currentLevel().progressPercent(exp);
    }

    public void plusExpInLastLogic(int number){
        this.exp = this.exp + number;
    }

    private void rewardMission(MissionType missionType) {
        this.point += missionType.rewardPoint();
        this.exp += 40;
    }

    private void settleSingleMission(boolean clear, MissionType missionType) {
        if (!clear) {
            subtractPoint(missionType.rewardPoint());
        }
    }

    private void refreshMissionStage() {
        this.missionStage = MissionStage.fromPoint(point);
    }

    private void subtractPoint(int number) {
        if (number > point) {
            this.point = 0;
        } else {
            this.point = this.point - number;
        }
    }

}
