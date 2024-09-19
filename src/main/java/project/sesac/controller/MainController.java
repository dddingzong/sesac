package project.sesac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController = @Controller + @ResponseBody JSON 같은 데이터 전달
@Controller //View 전달
public class MainController {

    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

    //로그인 시 데이터가 Controller로 들어옴
}
