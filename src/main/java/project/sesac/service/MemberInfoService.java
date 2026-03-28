package project.sesac.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.sesac.domain.DefaultMission;
import project.sesac.domain.MeetMission;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.OutsideMission;
import project.sesac.domain.type.InformationPreference;
import project.sesac.domain.type.MissionStage;
import project.sesac.domain.type.MissionType;
import project.sesac.repository.MemberInfoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final MissionService missionService;
    private final EntityManager em;

    public MemberInfo save(MemberInfo memberInfo){
        return memberInfoRepository.save(memberInfo);
    }

    public Optional<MemberInfo> findById(Long main_id){
        return memberInfoRepository.findById(main_id);
    }

    @Transactional
    public void mission1ToTrue(Long main_id){
        MemberInfo memberInfo = memberInfoRepository.findById(main_id).get();
        String mission1Content = checkmission(memberInfo.missionStage()).get(0);
        memberInfo.completeMission1(missionService.checkContentRole(mission1Content));

        em.flush();
        em.clear();
    }

    @Transactional
    public void mission2ToTrue(Long main_id){
        MemberInfo memberInfo = memberInfoRepository.findById(main_id).get();
        String mission2Content = checkmission(memberInfo.missionStage()).get(1);
        memberInfo.completeMission2(missionService.checkContentRole(mission2Content));

        em.flush();
        em.clear();
    }

    @Transactional
    public void changeChooseRole(Long main_id, InformationPreference newChooseRole){
        MemberInfo memberInfo = memberInfoRepository.findById(main_id).get();
        memberInfo.changeChooseRole(newChooseRole);

        em.flush();
        em.clear();
    }

    // 11시 50분마다 정산(안깬 미션이 있으면 마이너스, clearmission값 초기화)
    @Transactional
    @Scheduled(cron = "0 55 23 * * *", zone = "Asia/Seoul")
    public void memberInfoChange(){

        // 모든 memberInfo 의 mission1Clear, mission2Clear 를 false 로 변환
        List<MemberInfo> memberInfoList = memberInfoRepository.findAll();

        for (int i = 0; i < memberInfoList.size();i++){
            MemberInfo memberInfo = memberInfoList.get(i);

            // content 를 가져와야한다
            // memberInfo 의 point 확인
            // 그 point 에 맞는 Mission Entity 확인
            // Mission Entity 의 mission1, mission2 추출

            List<String> missionList = checkmission(memberInfo.missionStage());
            String mission1 = missionList.get(0);
            String mission2 = missionList.get(1);

            memberInfo.settleMissionDay(
                    missionService.checkContentRole(mission1),
                    missionService.checkContentRole(mission2)
            );
        }

        em.flush();
        em.clear();
    }

    public List<String> checkmission(MissionStage missionStage){
        if (missionStage == MissionStage.DEFAULT) {
            return dailydefaultmission();
        }else if (missionStage == MissionStage.OUTSIDE){
            return dailyoutsidemission();
        }else if (missionStage == MissionStage.MEET){
            return dailymeetmission();
        }

        throw new IllegalStateException("알 수 없는 미션 단계입니다: " + missionStage);
    }

    private List<String> dailydefaultmission() {
        DefaultMission defaultMission = missionService.dailyDefaultMission();
        List<String> list = new ArrayList<>();
        list.add(defaultMission.getMission1());
        list.add(defaultMission.getMission2());
        return list;
    }

    private List<String> dailyoutsidemission() {
        OutsideMission outsideMission = missionService.dailyOutsideMission();
        List<String> list = new ArrayList<>();
        list.add(outsideMission.getMission1());
        list.add(outsideMission.getMission2());
        return list;
    }

    private List<String> dailymeetmission() {
        MeetMission meetMission = missionService.dailyMeetMission();
        List<String> list = new ArrayList<>();
        list.add(meetMission.getMission1());
        list.add(meetMission.getMission2());
        return list;
    }
}
