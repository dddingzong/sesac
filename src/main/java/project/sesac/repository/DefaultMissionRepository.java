package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sesac.domain.DefaultMission;

public interface DefaultMissionRepository extends JpaRepository<DefaultMission, Long> {
}
