package project.sesac.application.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.sesac.application.common.RoleSelectionMapper;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.dto.MemberDto;
import project.sesac.domain.dto.MemberInfoDto;
import project.sesac.domain.type.InformationPreference;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;
import project.sesac.service.MissionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;
    private final MissionService missionService;

    @Transactional
    public AuthResult signup(MemberDto memberDto) {
        if (memberService.isLoginIdDuplicated(memberDto.getLoginId())) {
            return AuthResult.failure(HttpStatus.CONFLICT, "아이디가 이미 존재합니다.");
        }

        if (!memberDto.getLoginPassword().equals(memberDto.getLoginPassword_confirm())) {
            return AuthResult.failure(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        InformationPreference chooseRole = RoleSelectionMapper.toPreference(memberDto.getChooseRole());
        if (chooseRole == null) {
            return AuthResult.failure(HttpStatus.CONFLICT, "제공 받을 정보를 선택해 주십시오.");
        }

        Member savedMember = memberService.save(new Member(memberDto.getLoginId(), memberDto.getLoginPassword()));
        memberInfoService.save(new MemberInfo(savedMember, memberDto.getName(), chooseRole));
        return AuthResult.success(HttpStatus.OK, "회원가입 성공", null);
    }

    public AuthResult signin(MemberInfoDto memberInfoDto) {
        List<Member> members = memberService.findByLoginId(memberInfoDto.getLoginId());
        if (members.isEmpty()) {
            return AuthResult.failure(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.");
        }

        Member member = members.get(0);
        if (!member.getLoginPassword().equals(memberInfoDto.getLoginPassword())) {
            return AuthResult.failure(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        missionService.ensureDailyMissionInitialized();
        return AuthResult.success(HttpStatus.OK, "로그인 성공", member.getId());
    }
}
