package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Agent;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent,Long> {

    @Query("select a from Agent a where a.board_id = :board_id")
    List<Agent> findByBoardId(@Param("board_id")Long board_id);

    @Modifying
    @Query("delete from Agent a where a.board_id = :board_id")
    void deleteAllByBoardId(@Param("board_id")Long board_id);

    @Modifying
    @Query("delete from Agent a where a.agent = :login_id")
    void deleteByLoginId(@Param("login_id")String login_id);

    @Query("select a from Agent a where a.agent = :login_id")
    List<Agent> findByLoginId(@Param("login_id")String login_id);

}
