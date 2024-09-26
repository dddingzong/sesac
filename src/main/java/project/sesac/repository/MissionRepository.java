package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Mission;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    //defaultmission()에서 사용
    @Query("select m.content from Mission m where m.contentRole = 0 order by rand() limit 2")
    List<String> defaultmission();

    //outsidemission()에서 사용
    @Query("select m.content from Mission m where m.contentRole = 0 order by rand() limit 1")
    List<String> outsidemission();

    @Query("select m.content from Mission m where m.contentRole = 0 or m.contentRole = 1 order by rand() limit 1")
    List<String> outsideordefualtmission();

    //meetmission()에서 사용
    @Query("select m.content from Mission m where m.contentRole = 2 order by rand() limit 1")
    List<String> meetmission();

    @Query("select m from Mission m where m.content = :content")
    List<Mission> findByContent(@Param("content") String content);
}
