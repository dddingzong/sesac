package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.domain.Agent;
import project.sesac.domain.Board;
import project.sesac.domain.Information;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.dto.BoardDto;
import project.sesac.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final LastService lastService;
    private final AgentService agentService;
    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/board/{listNumber}")
    public String board(@PathVariable("listNumber") int listNumber,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        HttpSession session,
                        Model model){

        Long main_id = (Long) session.getAttribute("main_id");

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        logger.info("**********게시판페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        // 데이터 가져오고 filtering 해야됨
        List<Board> boardList = boardService.findAll();
        if (keyword != null) {
            boardList = boardService.keywordFilter(boardList, keyword);
        }

        int count = boardList.size();
        List<Board> posts = new ArrayList<>();

        int first = (listNumber-1)*10;
        int last = listNumber*10-1;

        for (int i=first;i<=last;i++){
            if (i==count){
                break;
            }
            posts.add(boardList.get(i));
        }

        model.addAttribute("posts",posts);
        model.addAttribute("count",count); // information 의 전체 개수
        model.addAttribute("totalPages", count%10==0 ? count/10 : count/10 + 1);
        model.addAttribute("startNumber",listNumber%5==0? listNumber-4 : listNumber - listNumber%5 + 1);
        model.addAttribute("listNumber",listNumber);
        model.addAttribute("keyword",keyword);

        return "board";
    }

    @GetMapping("/board/create")
    public String makeBoard(HttpSession session, Model model, RedirectAttributes redirectAttributes){

        Long main_id = (Long) session.getAttribute("main_id");

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }
        MemberInfo memberInfo = memberInfoService.findById(main_id).get();
        String author = memberService.findLoginIdById(memberInfo.getId());
        // agent에 login_id가 존재하면 오류메세지 (생성된 방이 있거나 or 3시간이 지나지 않았거나)
        if (!agentService.checkByLoginId(author)){ //만약 있으면? -> (나중에 다시 하세요 true면 비어있음 false면 차있음)
            redirectAttributes.addFlashAttribute("errorMessage", "참가하고 있는 모임이 있거나, 모임이 끝난지 세시간이 지나지 않았습니다. 잠시만 기다려주세요.");
            return "redirect:/board/1";
        }


        logger.info("**********게시판 작성 페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        model.addAttribute("author",author);
        model.addAttribute("boardDto",new BoardDto());

        return "createBoard";
    }

    @PostMapping("/board/create")
    public String createBoard(@ModelAttribute("boardDto") BoardDto boardDto, HttpSession session, Model model){

        // boardDto에서 데이터 가져오기
        String author = boardDto.getAuthor();
        String title = boardDto.getTitle();
        String content = boardDto.getContent();
        int total = Integer.parseInt(boardDto.getTotal());
        ArrayList<String> agentList = new ArrayList<>();
        agentList.add(author);

        Board board = new Board(author, title, content, total, 1,false);
        boardService.save(board);
        agentService.save(new Agent(board.getId(),board.getAuthor()));

        return "redirect:/board/1";
    }

    @GetMapping("/board/content/{id}")
    public String boardContent(@PathVariable("id") Long id, HttpSession session, Model model){ // id는 board의 id 이다.

        Long main_id = (Long) session.getAttribute("main_id");
        String loginId = memberService.findLoginIdById(main_id);

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        logger.info("**********게시판 내용 페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());


        // id로 board와 agent 명단 찾기
        // 필요한 데이터 title, content, author, agent, total, agentFull, Agent에서 agent확인
        Board board = boardService.findById(id);
        List<Agent> agentList = agentService.findByBoardId(id);
        List<String> checkDuplicateAgentList = new ArrayList<>();
        for (int i=0;i<agentList.size();i++){
            checkDuplicateAgentList.add(agentList.get(i).getAgent());
        }

        model.addAttribute("board",board);
        model.addAttribute("loginId",loginId);
        model.addAttribute("agentList",agentList);
        model.addAttribute("checkDuplicateAgentList",checkDuplicateAgentList);

        return "boardContent";
    }

    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        // board랑 agent도 같이 삭제
        boardService.deleteById(id);
        agentService.deleteAllByBoardId(id);

        logger.info("**********게시판 삭제 성공**********");
        logger.info("boardId = " + id);

        return "redirect:/board/1";
    }

    @GetMapping("/board/join/{id}")
    public String joinBoard(@PathVariable("id")Long id, HttpSession session, RedirectAttributes redirectAttributes){ // 이건 board_id 이다.

        Long main_id = (Long) session.getAttribute("main_id");
        String loginId = memberService.findLoginIdById(main_id);

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        // agent에 login_id가 존재하면 오류메세지 (생성된 방이 있거나 or 3시간이 지나지 않았거나)
        if (!agentService.checkByLoginId(loginId)){ //만약 있으면? -> (나중에 다시 하세요 true면 비어있음 false면 차있음)
            redirectAttributes.addFlashAttribute("errorMessage", "참가하고 있는 모임이 있거나, 모임이 끝난지 세시간이 지나지 않았습니다. 잠시만 기다려주세요.");
            return "redirect:/board/content/"+id;
        }

        Agent agent = new Agent(id, loginId);
        agentService.save(agent);
        boardService.plusAgent(id);

        // board의 agent와 total의 값이 같아지면 모집마감 (3시간 뒤 게시물 삭제로직 발생)
        // + exp 추가하기
        // + 참여하기, 모임 나가기, 게시글 삭제 버튼 막기 (성공)
        Board board = boardService.findById(id);
        if (board.getAgent() == board.getTotal()){
            logger.info("**********세시간 뒤에 "+id+"번 게시판 삭제**********");
            lastService.lastLogic(id);
            logger.info(id + "번 게시물 lastLogic 끝");
        }

        return "redirect:/board/content/"+id;
    }

    @GetMapping("/board/disconnect/{id}")
    public String joinBoard(@PathVariable("id")Long id, HttpSession session) { // 이건 board_id 이다.

        Long main_id = (Long) session.getAttribute("main_id");
        String loginId = memberService.findLoginIdById(main_id);

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        agentService.deleteByLoginId(loginId);
        boardService.minusAgent(id);

        return "redirect:/board/content/"+id;
    }

    @GetMapping("/board/deadline/{id}")
    public String deadline(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "모임이 모두 마감되었습니다. 구성원분들은 세시간 후부터 다른 모임에 참가하거나. 만들 수 있습니다.");
        return "redirect:/board/content/"+id;
    }





}
