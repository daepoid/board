package study.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsername(String username);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findByMemberAuth(MemberAuth memberAuth);

}
