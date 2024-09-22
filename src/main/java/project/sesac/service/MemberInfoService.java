package project.sesac.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.sesac.domain.MemberInfo;
import project.sesac.repository.MemberInfoRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;

    public void save(MemberInfo memberInfo){
        memberInfoRepository.save(memberInfo);
    }

    public Optional<MemberInfo> findById(Long main_id){
        return memberInfoRepository.findById(main_id);
    }
}
