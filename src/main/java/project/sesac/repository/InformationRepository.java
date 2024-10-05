package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Information;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {

    @Query("select i from Information i where i.infoRole = :chooseRole")
    List<Information> findByInfoRole(@Param("chooseRole")int chooseRole);
}
