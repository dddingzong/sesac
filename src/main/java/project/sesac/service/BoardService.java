package project.sesac.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Board;
import project.sesac.domain.Information;
import project.sesac.repository.BoardRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final EntityManager em;

    @Transactional
    public List<Board> findAll(){
        return boardRepository.findAll();
    }

    @Transactional
    public List<Board> keywordFilter(List<Board> boardList, String keyword) {
        List<Board> realList = new ArrayList<>();

        for (int i=0;i<boardList.size();i++) {
            Board board = boardList.get(i);
            if (board.getTitle().contains(keyword)){
                realList.add(boardList.get(i));
            }
        }
        return realList;
    }

    @Transactional
    public void save(Board board){
        boardRepository.save(board);
    }

    @Transactional
    public Board findById(Long id){
        return boardRepository.findById(id).get();
    }

    @Transactional
    public void deleteById(Long board_id){
        boardRepository.deleteById(board_id);
    }

    @Transactional
    public void plusAgent(Long id){
        Board board = boardRepository.findById(id).get();
        board.plusAgent();

        em.flush();
        em.clear();
    }

    @Transactional
    public void minusAgent(Long id){
        Board board = boardRepository.findById(id).get();
        board.minusAgent();

        em.flush();
        em.clear();
    }
}
