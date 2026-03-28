package project.sesac.application.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.application.common.ProfileLevel;
import project.sesac.application.common.RoleSelectionMapper;
import project.sesac.domain.Agent;
import project.sesac.domain.Board;
import project.sesac.domain.Information;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.dto.UserUpdateDto;
import project.sesac.domain.type.GrowthLevel;
import project.sesac.domain.type.InformationPreference;
import project.sesac.service.AgentService;
import project.sesac.service.BoardService;
import project.sesac.service.InformationService;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainApplicationService {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
    private final InformationService informationService;
    private final BoardService boardService;
    private final AgentService agentService;

    public MainPageView getMainPage(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        GrowthLevel level = memberInfo.currentLevel();
        ProfileLevel profileLevel = new ProfileLevel(level.label(), level.imageName(), memberInfo.progressPercent());
        List<String> missionList = memberInfoService.checkmission(memberInfo.missionStage());
        List<Information> informations = new ArrayList<>(informationService.findByPreference(memberInfo.preferredInformation()));
        if (memberInfo.preferredInformation() == InformationPreference.ALL) {
            informationService.shuffleLogic(informations);
        }

        List<Board> boards = boardService.findAll();
        return new MainPageView(
                memberInfo.getName(),
                profileLevel,
                memberInfo.isClearMission1(),
                memberInfo.isClearMission2(),
                missionList.get(0),
                missionList.get(1),
                limit(informations, 5),
                limit(boards, 5)
        );
    }

    public void completeMission(Long memberId, int missionIndex) {
        if (missionIndex == 1) {
            memberInfoService.mission1ToTrue(memberId);
            return;
        }
        if (missionIndex == 2) {
            memberInfoService.mission2ToTrue(memberId);
        }
    }

    public MyPageView getMyPage(Long memberId) {
        MemberInfo memberInfo = memberInfoService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        Member member = memberService.findById(memberId);
        List<Agent> agentList = agentService.findByLoginId(member.getLoginId());

        Long boardId = 0L;
        String boardTitle = "X";
        if (!agentList.isEmpty()) {
            boardId = agentList.get(0).getBoard_id();
            boardTitle = boardService.findById(boardId).getTitle();
        }

        return new MyPageView(
                member.getLoginId(),
                member.getLoginPassword(),
                memberInfo.getName(),
                memberInfo.currentLevel().label(),
                memberInfo.preferredInformation().code(),
                boardId,
                boardTitle
        );
    }

    public void updateUser(Long memberId, UserUpdateDto userUpdateDto) {
        MemberInfo memberInfo = memberInfoService.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));
        Member member = memberService.findById(memberId);

        if (!member.getLoginPassword().equals(userUpdateDto.getLoginPassword())) {
            memberService.changePassword(memberId, userUpdateDto.getLoginPassword());
        }

        InformationPreference newChooseRole = RoleSelectionMapper.toPreference(userUpdateDto.getChooseRole());
        if (newChooseRole != null && memberInfo.preferredInformation() != newChooseRole) {
            memberInfoService.changeChooseRole(memberId, newChooseRole);
        }
    }

    private <T> List<T> limit(List<T> items, int size) {
        if (items.size() <= size) {
            return items;
        }
        return new ArrayList<>(items.subList(0, size));
    }
}
