package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Information;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {

    @Query("select i from Information i where i.infoRole = :chooseRole")
    List<Information> findByInfoRole(@Param("chooseRole")int chooseRole);

    @Query("select i from Information i where i.infoRole = 0")
    List<Information> getCareInfo();

    @Query("select i from Information i where i.infoRole = 1")
    List<Information> getJobInfo();

    @Query("select i from Information i where i.title like concat('%', :keyword, '%')")
    List<Information> getSearchInfo(@Param("keyword") String keyword);
}
