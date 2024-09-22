package project.sesac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import project.sesac.domain.MemberInfo;

import java.util.List;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {

}
