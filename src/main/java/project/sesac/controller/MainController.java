package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.application.auth.AuthApplicationService;
import project.sesac.application.auth.AuthResult;
import project.sesac.application.common.CurrentUser;
import project.sesac.application.common.SessionUserService;
import project.sesac.application.main.MainApplicationService;
import project.sesac.application.main.MainPageView;
import project.sesac.application.main.MyPageView;
import project.sesac.domain.dto.MemberInfoDto;
import project.sesac.domain.dto.UserUpdateDto;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MissionService;

import java.util.Optional;

//@RestController = @Controller + @ResponseBody JSON 같은 데이터 전달
@Controller //View 전달
@RequiredArgsConstructor
public class MainController {

    private final MainApplicationService mainApplicationService;
    private final AuthApplicationService authApplicationService;
    private final SessionUserService sessionUserService;
    private final MemberInfoService memberInfoService;
    private final MissionService missionService;

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model){ //memberInfo 만 받는 식으로

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        MainPageView view = mainApplicationService.getMainPage(currentUser.get().memberId());
        model.addAttribute("name", view.name());
        model.addAttribute("level", view.profileLevel().level());
        model.addAttribute("picture", "images/" + view.profileLevel().imageName() + ".png");
        model.addAttribute("restExp", view.profileLevel().progressPercent());
        model.addAttribute("clearMission1", view.clearMission1());
        model.addAttribute("clearMission2", view.clearMission2());
        model.addAttribute("mission1", view.mission1());
        model.addAttribute("mission2", view.mission2());
        model.addAttribute("list1", view.informations());
        model.addAttribute("list2", view.boards());

        return "main";
    }

    // 버튼 클릭시 로직
    @PostMapping("/mission/complete")
    public String completeMission(@RequestParam("missionIndex") int missionIndex, HttpSession session, Model model) {
        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        mainApplicationService.completeMission(currentUser.get().memberId(), missionIndex);

        return "redirect:/main"; // 메인 페이지로 리다이렉트
    }

    // 로그인 시 api 가 MainController 로 들어옴
    @PostMapping(value = "/login/signin")
    public String signin(@ModelAttribute MemberInfoDto memberInfoDto, HttpSession session, RedirectAttributes redirectAttributes){
        AuthResult result = authApplicationService.signin(memberInfoDto);
        if (!result.success()) {
            redirectAttributes.addFlashAttribute("error", result.message());
            return "redirect:/";
        }

        sessionUserService.signIn(session, result.memberId());

        return "redirect:/main";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session){
        sessionUserService.signOut(session);
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model){

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        MyPageView view = mainApplicationService.getMyPage(currentUser.get().memberId());
        model.addAttribute("loginId", view.loginId());
        model.addAttribute("loginPassword", view.loginPassword());
        model.addAttribute("name", view.name());
        model.addAttribute("level", view.level());
        model.addAttribute("chooseRole", view.chooseRole());
        model.addAttribute("boardId", view.boardId());
        model.addAttribute("boardTitle", view.boardTitle());

        return "mypage";
    }

    @PostMapping("/mypage/update")
    public String updateUser(@ModelAttribute UserUpdateDto userUpdateDto, HttpSession session, Model model) {

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        mainApplicationService.updateUser(currentUser.get().memberId(), userUpdateDto);

        return "redirect:/main";
    }

    @GetMapping("/main/memberInfoChange")
    public String memberInfoChange(){
        memberInfoService.memberInfoChange();
        return "redirect:/main";
    }

    @GetMapping("/main/updateMission")
    public String updateMission(){
        missionService.updateMission();
        return "redirect:/";
    }

}
