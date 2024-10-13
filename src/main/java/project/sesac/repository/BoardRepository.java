package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.Agent;
import project.sesac.domain.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
