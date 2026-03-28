package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.application.board.BoardApplicationService;
import project.sesac.application.board.BoardCreateView;
import project.sesac.application.board.BoardDetailView;
import project.sesac.application.board.BoardPageView;
import project.sesac.application.common.ActionResult;
import project.sesac.application.common.CurrentUser;
import project.sesac.application.common.SessionUserService;
import project.sesac.domain.dto.BoardDto;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardApplicationService boardApplicationService;
    private final SessionUserService sessionUserService;

    @GetMapping("/board/{listNumber}")
    public String board(@PathVariable("listNumber") int listNumber,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        HttpSession session,
                        Model model){

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        BoardPageView view = boardApplicationService.getBoardPage(listNumber, keyword);
        model.addAttribute("posts", view.page().items());
        model.addAttribute("count", view.page().totalCount());
        model.addAttribute("totalPages", view.page().totalPages());
        model.addAttribute("startNumber", view.page().startPage());
        model.addAttribute("listNumber", view.page().currentPage());
        model.addAttribute("keyword", view.keyword());

        return "board";
    }

    @GetMapping("/board/create")
    public String makeBoard(HttpSession session, Model model, RedirectAttributes redirectAttributes){

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        BoardCreateView view = boardApplicationService.getBoardCreateView(currentUser.get().memberId());
        if (!view.canCreate()) {
            redirectAttributes.addFlashAttribute("errorMessage", view.errorMessage());
            return "redirect:/board/1";
        }

        model.addAttribute("author", view.author());
        model.addAttribute("boardDto",new BoardDto());

        return "createBoard";
    }

    @PostMapping("/board/create")
    public String createBoard(@ModelAttribute("boardDto") BoardDto boardDto, HttpSession session){
        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        boardApplicationService.createBoard(currentUser.get().memberId(), boardDto);

        return "redirect:/board/1";
    }

    @GetMapping("/board/content/{id}")
    public String boardContent(@PathVariable("id") Long id, HttpSession session, Model model){ // id는 board의 id 이다.

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        BoardDetailView view = boardApplicationService.getBoardDetail(currentUser.get().memberId(), id);
        model.addAttribute("board", view.board());
        model.addAttribute("loginId", view.loginId());
        model.addAttribute("agentList", view.agentList());
        model.addAttribute("checkDuplicateAgentList", view.participantLoginIds());

        return "boardContent";
    }

    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long id, HttpSession session, RedirectAttributes redirectAttributes){
        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        ActionResult result = boardApplicationService.deleteBoard(currentUser.get().memberId(), id);
        if (!result.succeeded()) {
            redirectAttributes.addFlashAttribute("errorMessage", result.message());
            return "redirect:/board/content/" + id;
        }

        return "redirect:/board/1";
    }

    @GetMapping("/board/join/{id}")
    public String joinBoard(@PathVariable("id")Long id, HttpSession session, RedirectAttributes redirectAttributes){ // 이건 board_id 이다.

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        ActionResult result = boardApplicationService.joinBoard(currentUser.get().memberId(), id);
        if (!result.succeeded()) {
            redirectAttributes.addFlashAttribute("errorMessage", result.message());
        }

        return "redirect:/board/content/"+id;
    }

    @GetMapping("/board/disconnect/{id}")
    public String joinBoard(@PathVariable("id")Long id, HttpSession session) { // 이건 board_id 이다.

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        boardApplicationService.disconnectBoard(currentUser.get().memberId(), id);

        return "redirect:/board/content/"+id;
    }

    @GetMapping("/board/deadline/{id}")
    public String deadline(@PathVariable("id")Long id, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("errorMessage", "모임이 모두 마감되었습니다. 구성원분들은 세시간 후부터 다른 모임에 참가하거나. 만들 수 있습니다.");
        return "redirect:/board/content/"+id;
    }

}
