package project.sesac;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.sesac.application.auth.AuthApplicationService;
import project.sesac.application.auth.AuthResult;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.domain.dto.MemberDto;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuthApplicationServiceTest {

    @Autowired
    private AuthApplicationService authApplicationService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberInfoService memberInfoService;

    @Test
    void signupCreatesMemberInfoWithSameMemberId() {
        MemberDto memberDto = new MemberDto("테스트", "signup_user", "1234", "1234", "all");

        AuthResult result = authApplicationService.signup(memberDto);

        assertThat(result.success()).isTrue();

        List<Member> members = memberService.findByLoginId("signup_user");
        assertThat(members).hasSize(1);

        Member savedMember = members.get(0);
        MemberInfo savedMemberInfo = memberInfoService.findById(savedMember.getId()).orElseThrow();

        assertThat(savedMemberInfo.getId()).isEqualTo(savedMember.getId());
        assertThat(savedMemberInfo.getName()).isEqualTo("테스트");
    }
}
