package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sesac.domain.OutsideMission;

public interface OutsideMissionRepository extends JpaRepository<OutsideMission, Long> {
}
