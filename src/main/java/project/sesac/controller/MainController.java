package project.sesac.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.Mission;
import project.sesac.domain.dto.MemberInfoDto;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@RestController = @Controller + @ResponseBody JSON 같은 데이터 전달
@Controller //View 전달
@RequiredArgsConstructor
public class MainController {


    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
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
        model.addAttribute("restExp",restExp);
        // 일일 미션 point 사용

        int point = memberInfo.getPoint();

        // 상태는 총 세가지 0: 기본미션 두개, 1: 기본미션 한개 랜덤하나, 2: 랜덤하나 야외미션 하나


        Mission mission1;
        Mission mission2;

        // todayInformation database 에서 가져오는 식


        // 모여봐요! database 에서 가져오는 식


        return "main";
    }

    // 로그인 시 api 가 MainController 로 들어옴
    @PostMapping(value = "/login/signin")
    public String signin(@ModelAttribute MemberInfoDto memberInfoDto, HttpSession session){

        // member 데이터에서 login 유효성 검사




        String loginId = memberInfoDto.getLoginId();
        // loginId로 main_id 찾기
        long main_id = memberService.findByLoginId(loginId).get(0).getId();

        // main_id를 세션에 저장
        session.setAttribute("main_id",main_id);

        logger.info("**********로그인 성공**********");

        return "redirect:/main";
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


}
