package project.sesac.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Member;
import project.sesac.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    public void save(Member member){
        memberRepository.save(member);
    }

    public List<Member> findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }

    // 아이디 중복 확인 메소드
    public boolean isLoginIdDuplicated(String loginId) {
        return !memberRepository.findByLoginId(loginId).isEmpty();
    }

    public String findLoginIdById(Long main_id){
        Member member = memberRepository.findById(main_id).get();
        return member.getLoginId();
    }

    public Member findById(Long main_id){
        return memberRepository.findById(main_id).get();
    }

    @Transactional
    public void changePassword(Long main_id, String newLoginPassword){
        Member member = memberRepository.findById(main_id).get();
        member.changePassword(newLoginPassword);

        em.flush();
        em.clear();
    }

}
