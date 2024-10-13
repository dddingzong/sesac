package project.sesac.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Agent;
import project.sesac.repository.AgentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentService {

    private final AgentRepository agentRepository;

    @Transactional
    public void save(Agent agent){
        agentRepository.save(agent);
    }

    @Transactional
    public List<Agent> findByBoardId(Long board_id){
        return agentRepository.findByBoardId(board_id);
    }

    @Transactional
    public void deleteAllByBoardId(Long board_id){ // 게시글이 삭제 됐을때 작동
        agentRepository.deleteAllByBoardId(board_id);
    }

    @Transactional
    public void deleteByLoginId(String Login_id){ //회원이 모임 나가기 했을때 작동
        agentRepository.deleteByLoginId(Login_id);
    }

    @Transactional
    public boolean checkByLoginId(String login_id){
        return agentRepository.findByLoginId(login_id).isEmpty();

    }

}
