package project.sesac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.Member;
import project.sesac.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(Member member){
        memberRepository.save(member);
    }

    public List<Member> findByLoginId(String loginId){
        return memberRepository.findByLoginId(loginId);
    }
}
