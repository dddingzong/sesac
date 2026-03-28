package project.sesac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.sesac.application.auth.AuthApplicationService;
import project.sesac.application.auth.AuthResult;
import project.sesac.domain.dto.MemberDto;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthApplicationService authApplicationService;

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "login";
    }

    //회원가입 시 데이터가 Controller 로 들어옴
    @ResponseBody
    @PostMapping(value = "/login/signup") // /login/signup 으로 POST 로 들어오면 로직 실행
    public ResponseEntity<String> add(@RequestBody MemberDto memberDto) {
        AuthResult result = authApplicationService.signup(memberDto);
        return ResponseEntity.status(result.status()).body(result.message());
    }

}
