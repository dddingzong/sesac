package project.sesac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.sesac.domain.DefaultMission;
import project.sesac.domain.MeetMission;
import project.sesac.domain.OutsideMission;
import project.sesac.repository.DefaultMissionRepository;
import project.sesac.repository.MeetMissionRepository;
import project.sesac.repository.MissionRepository;
import project.sesac.repository.OutsideMissionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DefaultMissionRepository defaultMissionRepository;
    private final OutsideMissionRepository outsideMissionRepository;
    private final MeetMissionRepository meetMissionRepository;

    public List<String> defaultmission() {
        return missionRepository.defaultmission();
    }

    public List<String> outsidemission() {
        List<String> list = new ArrayList<>();

        String mission1;
        String mission2;

        while (true) {
            mission1 = missionRepository.outsidemission().get(0);
            mission2 = missionRepository.outsideordefualtmission().get(0);

            if (!mission1.equals(mission2)) {
                break;
            }
        }

        list.add(mission1);
        list.add(mission2);

        return list;
    }

    public List<String> meetmission() {

        List<String> list = new ArrayList<>();

        list.add(missionRepository.outsideordefualtmission().get(0));
        list.add(missionRepository.meetmission().get(0));

        return list;
    }

    public DefaultMission dailyDefaultMission(){
        return defaultMissionRepository.findAll().get(0);
    }

    public OutsideMission dailyOutsideMission(){
        return outsideMissionRepository.findAll().get(0);
    }

    public MeetMission dailyMeetMission(){
        return meetMissionRepository.findAll().get(0);
    }

    // 00시마다 미션이 갱신
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateMission(){

        defaultMissionRepository.deleteAll();
        outsideMissionRepository.deleteAll();
        meetMissionRepository.deleteAll();

        List<String> defaultList = defaultmission();
        DefaultMission defaultMission = new DefaultMission(defaultList.get(0),defaultList.get(1));

        List<String> outsideList = outsidemission();
        OutsideMission outsideMission = new OutsideMission(outsideList.get(0),outsideList.get(1));

        List<String> missionList = meetmission();
        MeetMission meetMission = new MeetMission(missionList.get(0),missionList.get(1));

        defaultMissionRepository.save(defaultMission);
        outsideMissionRepository.save(outsideMission);
        meetMissionRepository.save(meetMission);
    }

}
