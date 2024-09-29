package project.sesac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String index(){
        return "test";
    }

    @GetMapping("/testimonial")
    public String testimonial(){
        return "testimonial";
    }

}
