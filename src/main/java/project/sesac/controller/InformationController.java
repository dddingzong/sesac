package project.sesac.controller;


import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import project.sesac.domain.Information;
import project.sesac.domain.MemberInfo;
import project.sesac.service.InformationService;
import project.sesac.service.MemberInfoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InformationController {

    private final InformationService informationService;
    private final MemberInfoService memberInfoService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/information/{listNumber}")
    public String informationList(@PathVariable("listNumber") int listNumber,
                                  @RequestParam(value = "role", required = false) Integer role,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  HttpSession session,
                                  Model model){

        Long main_id = (Long) session.getAttribute("main_id");

        if (main_id == null) {
            // main_id가 없으면 로그인 페이지로 redirect
            return "redirect:/login";
        }

        MemberInfo memberInfo = memberInfoService.findById(main_id).get();

        logger.info("**********정보페이지 진입 성공**********");
        logger.info("사용자 이름 = " + memberInfo.getName());

        List<Information> informationList = new ArrayList<>();

        if (role == null){ // 기본 information
            int chooseRole = memberInfo.getChooseRole();
            // chooseRole 이 0이면 복지만 1이면 취업만 2면 모두
            // information list 가져오기
            informationList = informationService.findByInfoRole(chooseRole);
            if (chooseRole == 2){
                // 뒤섞는로직 (복지 취업 복지 취업 순으로)
                informationService.shuffleLogic(informationList);
            }
             if(keyword != null) {
                 informationList = informationService.keywordFilter(informationList, keyword);
             }
        } else if (role == 0) { // careInformation
            informationList = informationService.findAll();
            if(keyword != null) {
                // 뒤섞는로직
                informationService.shuffleLogic(informationList);
                informationList = informationService.keywordFilter(informationList, keyword);
            }
        } else if (role == 1) { // careInformation
            informationList = informationService.getCareInfo();
            if(keyword != null) {
                informationList = informationService.keywordFilter(informationList, keyword);
            }
        } else if (role == 2) { // jobInformation
            informationList = informationService.getJobInfo();
            if(keyword != null) {
                informationList = informationService.keywordFilter(informationList, keyword);
            }
        }


        int count = informationList.size();
        List<Information> posts = new ArrayList<>();

        int first = (listNumber-1)*10;
        int last = listNumber*10 -1;
        for (int i = first; i <= last; i++){
            if (i == count){
                break;
            }
            posts.add(informationList.get(i));
        }

        model.addAttribute("posts",posts);
        model.addAttribute("count",count); // information 의 전체 개수
        model.addAttribute("totalPages", count%10==0 ? count/10 : count/10 + 1);
        model.addAttribute("startNumber",listNumber%5==0? listNumber-4 : listNumber - listNumber%5 + 1);
        model.addAttribute("listNumber",listNumber);
        model.addAttribute("role",role);
        model.addAttribute("keyword",keyword);
        return "information";
    }


    @GetMapping("/information/test")
    public String test(){
        informationService.informationDataCrawling();
        return "redirect:/information/1";
    }


}
