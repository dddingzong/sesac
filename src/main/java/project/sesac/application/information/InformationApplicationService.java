package project.sesac.application.information;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.application.common.ActionResult;
import project.sesac.application.common.PaginationResult;
import project.sesac.application.common.PaginationSupport;
import project.sesac.domain.Information;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.type.InformationPreference;
import project.sesac.service.InformationService;
import project.sesac.service.MemberInfoService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationApplicationService {

    private final InformationService informationService;
    private final MemberInfoService memberInfoService;

    public InformationPageView getInformationPage(Long memberId, int pageNumber, Integer role, String keyword) {
        MemberInfo memberInfo = memberInfoService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        List<Information> informations = resolveInformation(memberInfo, role);
        if (keyword != null && !keyword.isBlank()) {
            informations = informationService.keywordFilter(informations, keyword);
        }

        PaginationResult<Information> page = PaginationSupport.paginate(informations, pageNumber, 10, 5);
        return new InformationPageView(page, role, keyword);
    }

    public ActionResult refreshInformation() {
        int refreshedCount = informationService.informationDataCrawling();
        if (refreshedCount == 0) {
            return ActionResult.failure("정보를 새로고침하지 못했습니다. 기존 데이터를 유지합니다.");
        }
        return new ActionResult(true, refreshedCount + "개의 정보를 새로고침했습니다.");
    }

    private List<Information> resolveInformation(MemberInfo memberInfo, Integer role) {
        if (role == null) {
            List<Information> informationList = new ArrayList<>(informationService.findByPreference(memberInfo.preferredInformation()));
            if (memberInfo.preferredInformation() == InformationPreference.ALL) {
                informationService.shuffleLogic(informationList);
            }
            return informationList;
        }

        if (role == 0) {
            List<Information> informationList = new ArrayList<>(informationService.findAll());
            informationService.shuffleLogic(informationList);
            return informationList;
        }

        if (role == 1) {
            return informationService.getCareInfo();
        }

        if (role == 2) {
            return informationService.getJobInfo();
        }

        return informationService.findAll();
    }
}
