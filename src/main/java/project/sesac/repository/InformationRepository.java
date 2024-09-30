package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.sesac.domain.Information;

public interface InformationRepository extends JpaRepository<Information, Long> {
}
