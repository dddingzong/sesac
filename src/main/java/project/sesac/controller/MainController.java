package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.domain.*;
import project.sesac.domain.dto.MemberDto;
import project.sesac.domain.dto.MemberInfoDto;

import project.sesac.domain.dto.UserUpdateDto;
import project.sesac.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//@RestController = @Controller + @ResponseBody JSON 같은 데이터 전달
@Controller //View 전달
@RequiredArgsConstructor
public class MainController {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
    private final InformationService informationService;
    private final BoardService boardService;
    private final AgentService agentService;
    private final MissionService missionService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/main")
    public String mainPage(HttpSession session, Model model){ //memberInfo 만 받는 식으로

        Long main_id = (Long) session.getAttribute("main_id");
        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        logger.info("**********메인페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        // 성장 단계 name, exp 사용
        String name = memberInfo.getName();
        model.addAttribute("name",name);

        int exp = memberInfo.getExp();
        String level  = changeExpToLevel(exp);
        int restExp = restExp(exp); // 1%~100% 사이
        model.addAttribute("level",level);
        String picture = levelToPicture(level);
        model.addAttribute("picture", "images/"+picture+".png");
        model.addAttribute("restExp",restExp);

        // 미션 상태 전달
        model.addAttribute("clearMission1", memberInfo.isClearMission1());
        model.addAttribute("clearMission2", memberInfo.isClearMission2());

        // 일일 미션 point 사용
        int pointRole = memberInfo.getPointRole();

        // 상태는 총 세가지 0: 기본미션 두개, 1: 기본미션 한개 랜덤하나, 2: 만남미션 한개 기본, 야외 중 한개
        // default:0, 기본미션, 야외미션 성공시 +1, 만남미션 성공시 +5
        // 최종 포인트 10 이상이면 야외미션 추가 (0->1), 20 이상이면 만남 미션 추가 (1->2)
        List<String> missionList = memberInfoService.checkmission(pointRole);
        String mission1 = missionList.get(0);
        String mission2 = missionList.get(1);

        model.addAttribute("mission1",mission1);
        model.addAttribute("mission2",mission2);

        // Information database 에서 가져오는 식
        int chooseRole = memberInfo.getChooseRole();
        List<Information> infoList = informationService.findByInfoRole(chooseRole);
        List<Information> list1 = new ArrayList<>();
        if (chooseRole == 2){
            // 뒤섞는로직 (복지 취업 복지 취업 순으로)
            informationService.shuffleLogic(infoList);
        }
        // 첫 5개 요소를 가져오고 리스트에 추가
        if (infoList.size() > 5) {
            list1.addAll(infoList.subList(0, 5));
        } else {
            list1.addAll(infoList);
        }
        model.addAttribute("list1",list1);

        // Board database 에서 가져오는 식
        List<Board> boardList = boardService.findAll();
        List<Board> list2 = new ArrayList<>();

        if (boardList.size() > 5) {
            list2.addAll(boardList.subList(0, 5));
        } else {
            list2.addAll(boardList);
        }
        model.addAttribute("list2",list2);

        return "main";
    }

    // 버튼 클릭시 로직
    @PostMapping("/mission/complete")
    public String completeMission(@RequestParam("missionIndex") int missionIndex, HttpSession session, Model model) {
        Long main_id = (Long) session.getAttribute("main_id");
        if (main_id == null) {
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        // 미션 1 클릭 시
        if (missionIndex == 1) {
            memberInfoService.mission1ToTrue(memberInfo.getId());
            logger.info("**********"+memberInfo.getName()+" mission1 clear*********");
        }
        // 미션 2 클릭 시
        else if (missionIndex == 2) {
            memberInfoService.mission2ToTrue(memberInfo.getId());
            logger.info("**********"+memberInfo.getName()+" mission2 clear*********");
        }

        return "redirect:/main"; // 메인 페이지로 리다이렉트
    }

    // 로그인 시 api 가 MainController 로 들어옴
    @PostMapping(value = "/login/signin")
    public String signin(@ModelAttribute MemberInfoDto memberInfoDto, HttpSession session, RedirectAttributes redirectAttributes){
        String loginId = memberInfoDto.getLoginId();
        String loginPassword = memberInfoDto.getLoginPassword();

        // ID가 존재하는지 확인

        // member 데이터에서 login 유효성 검사
        // loginId 가 존재하는지 확인
        List<Member> members = memberService.findByLoginId(loginId);

        if (members.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 아이디입니다.");
            return "redirect:/";
        }

        // loginId와 Password가 일치하는지 확인
        Member member = members.get(0);
        if (!member.getLoginPassword().equals(loginPassword)) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/";
        }

        // login 에 성공했다면 main page 로 main_id 넘기기
        // loginId로 main_id 찾기
        long main_id = member.getId();

        // main_id를 세션에 저장
        session.setAttribute("main_id",main_id);

        logger.info("**********로그인 성공**********");

        return "redirect:/main";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session){
        session.invalidate();
        logger.info("**********로그아웃 성공**********");
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model){

        Long main_id = (Long) session.getAttribute("main_id");
        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();
        Member member = memberService.findById(main_id);

        List<Agent> agentList = agentService.findByLoginId(member.getLoginId());

        Long boardId = 0L;
        String boardTitle = "X";
        if (agentList.isEmpty()){ // 모임 참여 X

        } else { // 모임 참여 O
            boardId = agentList.get(0).getBoard_id();
            boardTitle = boardService.findById(boardId).getTitle();
        }

        logger.info("**********마이페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        // exp -> level
        String level = changeExpToLevel(memberInfo.getExp());

        // 필요한 데이터
        // member: loginId, loginPassword
        // memberInfo: name, exp, chooseRole
        // Agent board_id
        model.addAttribute("loginId",member.getLoginId());
        model.addAttribute("loginPassword",member.getLoginPassword());
        model.addAttribute("name",memberInfo.getName());
        model.addAttribute("level",level);
        model.addAttribute("chooseRole",memberInfo.getChooseRole());
        model.addAttribute("boardId",boardId);
        model.addAttribute("boardTitle",boardTitle);

        return "mypage";
    }

    @PostMapping("/mypage/update")
    public String updateUser(@ModelAttribute UserUpdateDto userUpdateDto, HttpSession session, Model model) {

        Long main_id = (Long) session.getAttribute("main_id");
        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        // 기존 Password와 chooseRole
        MemberInfo memberInfo = memberInfoService.findById(main_id).get();
        Member member = memberService.findById(main_id);
        String oldLoginPassword = member.getLoginPassword();
        int oldChooseRole = memberInfo.getChooseRole();


        // 변경 후 Password와 chooseRole
        String newLoginPassword = userUpdateDto.getLoginPassword();
        int newChooseRole = chooseRoleToInt(userUpdateDto.getChooseRole());

        if (!oldLoginPassword.equals(newLoginPassword)){ // 다르면 수정 로직 실행
            memberService.changePassword(main_id, newLoginPassword);
        }

        if (oldChooseRole != newChooseRole){ // 다르면 수정 로직 실행
            memberInfoService.changeChooseRole(main_id,newChooseRole);
        }

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




    private String changeExpToLevel(int exp) {
        if (exp < 200){
            return "씨앗";
        } else if (exp >= 200 && exp < 600) {
            return "새싹";
        } else if (exp >= 600 && exp < 1400) {
            return "가지";
        } else if (exp >= 1400 && exp < 2600) {
            return "나무";
        } else if (exp >= 2600){
            return "열매";
        } else {
            return "미상";
        }
    }

    private String levelToPicture(String level) {
        if (level.equals("씨앗")){
            return "seed";
        } else if (level.equals("새싹")){
            return "sprout";
        } else if (level.equals("가지")){
            return "branch";
        } else if (level.equals("나무")){
            return "tree";
        } else if (level.equals("열매")){
            return "apple";
        } else {
            return "sprout";
        }
    }

    private int restExp(int exp) {
        if (exp < 200) {
            return (int) (((double) exp / 200) * 100);
        } else if (exp >= 200 && exp < 600) {
            return (int) (((double) (exp - 200) / 400) * 100);
        } else if (exp >= 600 && exp < 1400) {
            return (int) (((double) (exp - 600) / 800) * 100);
        } else if (exp >= 1400 && exp < 2600) {
            return (int) (((double) (exp - 1400) / 1200) * 100);
        } else if (exp >= 2600) {
            return 100;
        } else {
            return 0;
        }
    }

    // ChooseRole 의 값을 String -> int
    int chooseRoleToInt(String chooseRole){
        if (chooseRole.equals("care")){
            return 0;
        } else if (chooseRole.equals("job")) {
            return 1;
        } else if (chooseRole.equals("all")){
            return 2;
        } else {
            return -1;
        }
    }

}
