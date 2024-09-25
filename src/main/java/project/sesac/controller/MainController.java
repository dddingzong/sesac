package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.Mission;
import project.sesac.domain.dto.MemberInfoDto;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.sesac.service.MissionService;

import java.util.ArrayList;
import java.util.List;

//@RestController = @Controller + @ResponseBody JSON 같은 데이터 전달
@Controller //View 전달
@RequiredArgsConstructor
public class MainController {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
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
        model.addAttribute("picture", "/images/"+picture+".png");
        model.addAttribute("restExp",restExp);

        // 일일 미션 point 사용
        int point = memberInfo.getPoint();

        // 상태는 총 세가지 0: 기본미션 두개, 1: 기본미션 한개 랜덤하나, 2: 만남미션 한개 기본, 야외 중 한개
        // default:0, 기본미션, 야외미션 성공시 +1, 만남미션 성공시 +5
        // 최종 포인트 10 이상이면 야외미션 추가 (0->1), 20 이상이면 만남 미션 추가 (1->2)
        List<String> missionList = choosemission(point);
        String mission1 = missionList.get(0);
        String mission2 = missionList.get(1);

        model.addAttribute("mission1",mission1);
        model.addAttribute("mission2",mission2);

        // todayInformation database 에서 가져오는 식


        // 모여봐요! database 에서 가져오는 식


        return "main";
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

    private List<String> defaultmission() {
        return missionService.defaultmission();
    }

    private List<String> outsidemission() {
        return missionService.outsidemission();
    }

    private List<String> meetmission() {
        return missionService.meetmission();
    }

    private List<String> choosemission(int point){

        if (point < 10) {
            return defaultmission();
        }else if (point >= 10 && point < 20){
            return outsidemission();
        }else if (point >= 20){
            return meetmission();
        } else {
            return null;
        }
    }


}
