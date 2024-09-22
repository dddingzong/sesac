package project.sesac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.domain.MemberInfo;
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
    public String mainPage(@ModelAttribute("memberInfo")MemberInfo memberInfo, Model model){ //memberInfo 만 받는 식으로

        logger.info("**********메인페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        // 성장 단계 name, exp 사용
        String name = memberInfo.getName();
        model.addAttribute("name",name);

        int exp = memberInfo.getExp();
        String level  = changeExpToLevel(exp);


        // 일일 미션 point 사용


        // todayInformation database 에서 가져오는 식


        // 모여봐요! database 에서 가져오는 식


        return "main";
    }

    // 로그인 시 api 가 MainController 로 들어옴
    @PostMapping(value = "/login/signin")
    public String signin(@ModelAttribute MemberInfoDto memberInfoDto, RedirectAttributes redirectAttributes){

        // member 데이터에서 login 유효성 검사



        String loginId = memberInfoDto.getLoginId();
        // loginId로 main_id 찾기
        long main_id = memberService.findByLoginId(loginId).get(0).getId();
        // main_id 로 MemberInfo 추출
        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        //memberInfo 에 대한 데이터를 같이 보내야한다.
        redirectAttributes.addFlashAttribute("memberInfo", memberInfo);

        logger.info("**********로그인 성공**********");

        return "redirect:/main";
    }


    private String changeExpToLevel(int exp) {
        if (exp <= 100){
            return "새싹";
        } else {
            return "열매";
        }
    }


}
