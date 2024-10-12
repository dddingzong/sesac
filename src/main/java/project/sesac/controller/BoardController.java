package project.sesac.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardController {


    @GetMapping("/board")
    public String board(){

        return "board";
    }
}
