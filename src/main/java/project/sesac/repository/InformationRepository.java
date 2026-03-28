package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Information;
import project.sesac.domain.type.InformationType;

import java.util.List;

public interface InformationRepository extends JpaRepository<Information, Long> {

    List<Information> findByInformationType(InformationType informationType);

    @Query("select i from Information i where i.title like concat('%', :keyword, '%')")
    List<Information> getSearchInfo(@Param("keyword") String keyword);
}
