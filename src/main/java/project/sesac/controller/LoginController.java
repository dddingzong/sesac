package project.sesac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.dto.MemberDto;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String loginPage(){
        return "login";
    }

    //회원가입 시 데이터가 Controller 로 들어옴
    @ResponseBody
    @PostMapping(value = "/login/signup") // /login/signup 으로 POST 로 들어오면 로직 실행
    public void add(@RequestBody MemberDto memberDto){

        // 아이디 중복 확인 로직 구현

        // 비밀번호 확인 로직 구현

        // 둘 다 통과 했을시에는 dto 에서 실제 Entity 로 변환
        // 이후 Member, MemberInfo 에 테이블 추가

        Member member = new Member(
                memberDto.getLoginId(),
                memberDto.getLoginPassword()
        );


        MemberInfo memberInfo = new MemberInfo(
                memberDto.getName(),
                getChooseRoleToInt(memberDto)
        );

        memberService.save(member);
        memberInfoService.save(memberInfo);

        logger.info("**********회원가입 성공**********");
    }

    // ChooseRole 의 값을 String -> int
    int getChooseRoleToInt(MemberDto memberDto){
        if (memberDto.getChooseRole().equals("care")){
            return 0;
        } else if (memberDto.getChooseRole().equals("job")) {
            return 1;
        } else if (memberDto.getChooseRole().equals("all")){
            return 2;
        } else {
            return -1;
        }
    }

}
