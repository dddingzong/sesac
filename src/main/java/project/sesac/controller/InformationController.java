package project.sesac.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.sesac.application.common.ActionResult;
import project.sesac.application.common.CurrentUser;
import project.sesac.application.common.SessionUserService;
import project.sesac.application.information.InformationApplicationService;
import project.sesac.application.information.InformationPageView;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class InformationController {

    private final InformationApplicationService informationApplicationService;
    private final SessionUserService sessionUserService;

    @GetMapping("/information/{listNumber}")
    public String informationList(@PathVariable("listNumber") int listNumber,
                                  @RequestParam(value = "role", required = false) Integer role,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  HttpSession session,
                                  Model model){

        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        InformationPageView view = informationApplicationService.getInformationPage(
                currentUser.get().memberId(),
                listNumber,
                role,
                keyword
        );

        model.addAttribute("posts", view.page().items());
        model.addAttribute("count", view.page().totalCount());
        model.addAttribute("totalPages", view.page().totalPages());
        model.addAttribute("startNumber", view.page().startPage());
        model.addAttribute("listNumber", view.page().currentPage());
        model.addAttribute("role", view.role());
        model.addAttribute("keyword", view.keyword());
        return "information";
    }


    @GetMapping("/information/getCareAndJobInformation")
    public String getCareAndJobInformation(HttpSession session, RedirectAttributes redirectAttributes){
        Optional<CurrentUser> currentUser = sessionUserService.getCurrentUser(session);
        if (currentUser.isEmpty()) {
            return "redirect:/login";
        }

        ActionResult result = informationApplicationService.refreshInformation();
        redirectAttributes.addFlashAttribute("errorMessage", result.message());
        return "redirect:/information/1";
    }


}
