package project.sesac.service;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import project.sesac.domain.Agent;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.repository.BoardRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LastService {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
    private final BoardService boardService;
    private final AgentService agentService;
    private final BoardRepository boardRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EntityManager em;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(30);

    @Async("taskExecutor") // taskExecutor 스레드 풀을 사용
    public void lastLogic(Long board_id) {
        logger.info(board_id + "번 게시물 lastLogic 진입");
        scheduler.schedule(() -> {
            Logic(board_id);
        }, 3, TimeUnit.HOURS);
    }


    @Transactional
    public void Logic(Long board_id) {
        logger.info(board_id + "번 게시물 Logic 진입");
        // exp 추가하기 (Agent 에서 board_id를 가지고 login_id 찾기 ->
        //             Member 에서 login_id 가지고 main_id 찾기 ->
        //             MemberInfo 에서 main_id 가지고 exp +)

        List<Agent> agentList = agentService.findByBoardId(board_id);
        int size = agentList.size();

        for (int i=0;i<agentList.size();i++){
            String login_id = agentList.get(i).getAgent();
            Member member = memberService.findByLoginId(login_id).get(0);
            MemberInfo memberInfo = memberInfoService.findById(member.getId()).get();
            memberInfo.plusExpInLastLogic(20*size);
        }
        logger.info(board_id + "번 게시물 exp 정리 끝");
        // 3시간 뒤 게시물 삭제로직 발생
        boardService.deleteById(board_id);
        agentService.deleteAllByBoardId(board_id);
        logger.info(board_id + "번 게시물 게시물 삭제 끝");
        em.flush();
        em.clear();
    }

    @PreDestroy
    public void shutdownScheduler() {
        logger.info("애플리케이션 종료 시 스케줄러 종료");
        scheduler.shutdown();
    }
}
