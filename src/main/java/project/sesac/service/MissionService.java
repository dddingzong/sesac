package project.sesac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Mission;
import project.sesac.repository.MissionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

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
}
