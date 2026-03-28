package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sesac.domain.Mission;
import project.sesac.domain.type.MissionType;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findByMissionType(MissionType missionType);

    List<Mission> findByMissionTypeIn(List<MissionType> missionTypes);

    List<Mission> findByContent(String content);
}
