package project.sesac.application.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.application.common.ActionResult;
import project.sesac.application.common.PaginationResult;
import project.sesac.application.common.PaginationSupport;
import project.sesac.domain.Agent;
import project.sesac.domain.Board;
import project.sesac.domain.dto.BoardDto;
import project.sesac.service.AgentService;
import project.sesac.service.BoardService;
import project.sesac.service.LastService;
import project.sesac.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardApplicationService {

    private static final String BOARD_ACCESS_ERROR = "참가하고 있는 모임이 있거나, 모임이 끝난지 세시간이 지나지 않았습니다. 잠시만 기다려주세요.";

    private final BoardService boardService;
    private final LastService lastService;
    private final AgentService agentService;
    private final MemberService memberService;

    public BoardPageView getBoardPage(int pageNumber, String keyword) {
        List<Board> boards = boardService.findAll();
        if (keyword != null && !keyword.isBlank()) {
            boards = boardService.keywordFilter(boards, keyword);
        }

        PaginationResult<Board> page = PaginationSupport.paginate(boards, pageNumber, 10, 5);
        return new BoardPageView(page, keyword);
    }

    public BoardCreateView getBoardCreateView(Long memberId) {
        String loginId = memberService.findLoginIdById(memberId);
        if (!agentService.checkByLoginId(loginId)) {
            return new BoardCreateView(loginId, false, BOARD_ACCESS_ERROR);
        }
        return new BoardCreateView(loginId, true, null);
    }

    public void createBoard(Long memberId, BoardDto boardDto) {
        String author = memberService.findLoginIdById(memberId);
        int total = Integer.parseInt(boardDto.getTotal());
        Board board = new Board(author, boardDto.getTitle(), boardDto.getContent(), total, 1, total <= 1);
        boardService.save(board);
        agentService.save(new Agent(board.getId(), author));
    }

    public BoardDetailView getBoardDetail(Long memberId, Long boardId) {
        String loginId = memberService.findLoginIdById(memberId);
        Board board = boardService.findById(boardId);
        List<Agent> agentList = agentService.findByBoardId(boardId);
        List<String> participantIds = agentList.stream()
                .map(Agent::getAgent)
                .toList();

        return new BoardDetailView(board, loginId, agentList, participantIds);
    }

    public ActionResult deleteBoard(Long memberId, Long boardId) {
        String loginId = memberService.findLoginIdById(memberId);
        Board board = boardService.findById(boardId);
        if (!board.getAuthor().equals(loginId)) {
            return ActionResult.failure("작성자만 게시글을 삭제할 수 있습니다.");
        }

        boardService.deleteById(boardId);
        agentService.deleteAllByBoardId(boardId);
        return ActionResult.success();
    }

    public ActionResult joinBoard(Long memberId, Long boardId) {
        String loginId = memberService.findLoginIdById(memberId);
        if (!agentService.checkByLoginId(loginId)) {
            return ActionResult.failure(BOARD_ACCESS_ERROR);
        }

        Board board = boardService.findById(boardId);
        if (!board.canJoin()) {
            return ActionResult.failure("모임이 모두 마감되었습니다. 구성원분들은 세시간 후부터 다른 모임에 참가하거나. 만들 수 있습니다.");
        }

        if (agentService.existsByBoardIdAndLoginId(boardId, loginId)) {
            return ActionResult.failure("이미 참가 중인 모임입니다.");
        }

        agentService.save(new Agent(boardId, loginId));
        boardService.plusAgent(boardId);

        Board updatedBoard = boardService.findById(boardId);
        if (updatedBoard.isAgentFull()) {
            lastService.lastLogic(boardId);
        }

        return ActionResult.success();
    }

    public ActionResult disconnectBoard(Long memberId, Long boardId) {
        String loginId = memberService.findLoginIdById(memberId);
        if (!agentService.existsByBoardIdAndLoginId(boardId, loginId)) {
            return ActionResult.failure("참가 중인 모임이 아닙니다.");
        }

        agentService.deleteByBoardIdAndLoginId(boardId, loginId);
        boardService.minusAgent(boardId);
        return ActionResult.success();
    }
}
