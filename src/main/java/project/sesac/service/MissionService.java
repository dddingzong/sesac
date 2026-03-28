package project.sesac.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.sesac.domain.DefaultMission;
import project.sesac.domain.MeetMission;
import project.sesac.domain.Mission;
import project.sesac.domain.OutsideMission;
import project.sesac.domain.type.MissionType;
import project.sesac.repository.DefaultMissionRepository;
import project.sesac.repository.MeetMissionRepository;
import project.sesac.repository.MissionRepository;
import project.sesac.repository.OutsideMissionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final DefaultMissionRepository defaultMissionRepository;
    private final OutsideMissionRepository outsideMissionRepository;
    private final MeetMissionRepository meetMissionRepository;

    public List<String> defaultmission() {
        return pickDistinctContents(missionRepository.findByMissionType(MissionType.DEFAULT), 2);
    }

    public List<String> outsidemission() {
        List<String> list = new ArrayList<>();
        String outsideMission = pickRandomContent(missionRepository.findByMissionType(MissionType.OUTSIDE));
        String otherMission = pickRandomContentExcluding(
                missionRepository.findByMissionTypeIn(List.of(MissionType.DEFAULT, MissionType.OUTSIDE)),
                outsideMission
        );

        list.add(outsideMission);
        list.add(otherMission);
        return list;
    }

    public List<String> meetmission() {
        List<String> list = new ArrayList<>();
        list.add(pickRandomContent(missionRepository.findByMissionTypeIn(List.of(MissionType.DEFAULT, MissionType.OUTSIDE))));
        list.add(pickRandomContent(missionRepository.findByMissionType(MissionType.MEET)));

        return list;
    }

    public DefaultMission dailyDefaultMission() {
        return defaultMissionRepository.findAll().get(0);
    }

    public OutsideMission dailyOutsideMission() {
        return outsideMissionRepository.findAll().get(0);
    }

    public MeetMission dailyMeetMission() {
        return meetMissionRepository.findAll().get(0);
    }

    public MissionType checkContentRole(String content) {
        return missionRepository.findByContent(content).get(0).getMissionType();
    }

    public void ensureDailyMissionInitialized() {
        if (defaultMissionRepository.count() == 0
                || outsideMissionRepository.count() == 0
                || meetMissionRepository.count() == 0) {
            updateMission();
        }
    }

    // 00시마다 미션이 갱신
    @Transactional
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateMission() {

        defaultMissionRepository.deleteAll();
        outsideMissionRepository.deleteAll();
        meetMissionRepository.deleteAll();

        List<String> defaultList = defaultmission();
        DefaultMission defaultMission = new DefaultMission(defaultList.get(0), defaultList.get(1));

        List<String> outsideList = outsidemission();
        OutsideMission outsideMission = new OutsideMission(outsideList.get(0), outsideList.get(1));

        List<String> missionList = meetmission();
        MeetMission meetMission = new MeetMission(missionList.get(0), missionList.get(1));

        defaultMissionRepository.save(defaultMission);
        outsideMissionRepository.save(outsideMission);
        meetMissionRepository.save(meetMission);
    }

    private List<String> pickDistinctContents(List<Mission> missions, int size) {
        List<Mission> candidates = new ArrayList<>(missions);
        Collections.shuffle(candidates);

        List<String> result = new ArrayList<>();
        for (Mission mission : candidates) {
            if (result.contains(mission.getContent())) {
                continue;
            }
            result.add(mission.getContent());
            if (result.size() == size) {
                return result;
            }
        }

        throw new IllegalStateException("일일 미션을 구성할 수 있는 데이터가 부족합니다.");
    }

    private String pickRandomContent(List<Mission> missions) {
        if (missions.isEmpty()) {
            throw new IllegalStateException("미션 데이터가 비어 있습니다.");
        }
        int index = ThreadLocalRandom.current().nextInt(missions.size());
        return missions.get(index).getContent();
    }

    private String pickRandomContentExcluding(List<Mission> missions, String excludedContent) {
        List<Mission> candidates = missions.stream()
                .filter(mission -> !mission.getContent().equals(excludedContent))
                .toList();

        return pickRandomContent(candidates);
    }
}
