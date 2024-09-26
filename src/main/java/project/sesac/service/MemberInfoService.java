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

    public void save(MemberInfo memberInfo){
        memberInfoRepository.save(memberInfo);
    }

    public Optional<MemberInfo> findById(Long main_id){
        return memberInfoRepository.findById(main_id);
    }

    @Transactional
    public void mission1ToTrue(Long main_id){
        MemberInfo memberInfo = memberInfoRepository.findById(main_id).get();
        memberInfo.mission1ToTrue();

        String mission1Content = checkmission(memberInfo.getPointRole()).get(0);
        int number = roleWeight(missionService.checkContentRole(mission1Content));

        memberInfo.plusPoint(number);
        memberInfo.plusExp();

        em.flush();
        em.clear();
    }

    @Transactional
    public void mission2ToTrue(Long main_id){
        MemberInfo memberInfo = memberInfoRepository.findById(main_id).get();
        memberInfo.mission2ToTrue();

        String mission2Content = checkmission(memberInfo.getPointRole()).get(1);
        int number = roleWeight(missionService.checkContentRole(mission2Content));

        memberInfo.plusPoint(number);
        memberInfo.plusExp();

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

            List<String> missionList = checkmission(memberInfo.getPointRole());
            String mission1 = missionList.get(0);
            String mission2 = missionList.get(1);

            if (!memberInfo.isClearMission1()){ // Mission1을 클리어하지 못한 경우
                int number = roleWeight(missionService.checkContentRole(mission1));
                memberInfo.minusPoint(number);
            } else { // Mission1을 클리어한 경우
                memberInfo.mission1ToFalse();
            }

            if (!memberInfo.isClearMission2()){ // Mission2를 클리어하지 못한 경우
                int number = roleWeight(missionService.checkContentRole(mission2));
                memberInfo.minusPoint(number);
            } else { // Mission1을 클리어한 경우
                memberInfo.mission2ToFalse();
            }

            // pointRole 변경
            // 미션 하나만 클리어했을때 mission entity 가 변경되는 것을 방지하기 위함
            memberInfo.changePointRole(pointToPointRole(memberInfo.getPoint()));
        }

        em.flush();
        em.clear();
    }

    private int pointToPointRole(int point){
        if (point < 10) {
            return 0;
        }else if (point >= 10 && point < 20){
            return 1;
        }else if (point >= 20){
            return 2;
        } else {
            return 0;
        }

    }


    public List<String> checkmission(int pointRole){
        if (pointRole == 0) {
            return dailydefaultmission();
        }else if (pointRole == 1){
            return dailyoutsidemission();
        }else if (pointRole == 2){
            return dailymeetmission();
        } else {
            return null;
        }
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

    private int roleWeight(int role){
        if (role == 2){
            return 5;
        } else {
            return 1;
        }
    }
}
