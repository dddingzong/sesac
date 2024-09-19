package project.sesac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/test")
    public String index(){
        return "test";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/doctor")
    public String doctor(){
        return "doctor";
    }

    @GetMapping("/testimonial")
    public String testimonial(){
        return "testimonial";
    }

    @GetMapping("/treatment")
    public String treatment(){
        return "treatment";
    }
}
