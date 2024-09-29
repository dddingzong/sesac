package project.sesac.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.sesac.domain.MemberInfo;
import project.sesac.service.MemberInfoService;

@Controller
@RequiredArgsConstructor
public class InformationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final MemberInfoService memberInfoService;

    @GetMapping("/information")
    public String information(HttpSession session){

        Long main_id = (Long) session.getAttribute("main_id");

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();
        int chooseRole = memberInfo.getChooseRole();


        return "/information";
    }
}
