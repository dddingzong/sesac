package project.sesac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.sesac.domain.Member;
import project.sesac.domain.dto.MemberDto;

@Controller
public class LoginController {

    @GetMapping("/")
    public String loginPage(){
        return "login";
    }

    //회원가입 시 데이터가 Controller 로 들어옴
    @ResponseBody
    @PostMapping(value = "/login/signup") // /login/signup 으로 POST 로 들어오면 로직 실행
    public String add(@RequestBody MemberDto memberDto){

        // 아이디 중복 확인 로직 구현

        // 비밀번호 확인 로직 구현

        // 둘 다 통과 했을시에는 dto 에서 실제 Entity 로 변환
        // 이후 Member, MemberInfo 에 테이블 추가
        System.out.println("memberDto = " + memberDto); // 잘 들어왔는지 확인

        return "redirect:login";
    }


}
