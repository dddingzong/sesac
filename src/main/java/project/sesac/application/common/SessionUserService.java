package project.sesac.application.common;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import project.sesac.domain.Member;
import project.sesac.domain.MemberInfo;
import project.sesac.service.MemberInfoService;
import project.sesac.service.MemberService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionUserService {

    private static final String SESSION_KEY = "main_id";

    private final MemberService memberService;
    private final MemberInfoService memberInfoService;

    public Optional<CurrentUser> getCurrentUser(HttpSession session) {
        Long memberId = (Long) session.getAttribute(SESSION_KEY);
        if (memberId == null) {
            return Optional.empty();
        }

        Optional<Member> member = memberService.findOptionalById(memberId);
        Optional<MemberInfo> memberInfo = memberInfoService.findById(memberId);
        if (member.isEmpty() || memberInfo.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new CurrentUser(
                memberId,
                member.get().getLoginId(),
                memberInfo.get().getName()
        ));
    }

    public void signIn(HttpSession session, Long memberId) {
        session.setAttribute(SESSION_KEY, memberId);
    }

    public void signOut(HttpSession session) {
        session.invalidate();
    }
}
