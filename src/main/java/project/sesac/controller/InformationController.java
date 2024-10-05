package project.sesac.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.sesac.domain.Information;
import project.sesac.domain.MemberInfo;
import project.sesac.service.InformationService;
import project.sesac.service.MemberInfoService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InformationController {

    private final InformationService informationService;
    private final MemberInfoService memberInfoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/information")
    public String information(HttpSession session, Model model){

        Long main_id = (Long) session.getAttribute("main_id");

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        logger.info("**********정보페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        int chooseRole = memberInfo.getChooseRole();
        // chooseRole 이 0이면 복지만 1이면 취업만 2면 모두
        List<Information> posts = informationService.findByInfoRole(chooseRole);

        for (int i =0; i < posts.size(); i++){
            System.out.println("list.get("+i+") = " + posts.get(i));
        }

        model.addAttribute("posts",posts);

        return "information";
    }

    @GetMapping("/information/test")
    public String test(){
        informationService.informationDataCrawling();
        return "redirect:/information";
    }
}
