package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sesac.domain.MeetMission;

public interface MeetMissionRepository extends JpaRepository<MeetMission, Long> {
}
