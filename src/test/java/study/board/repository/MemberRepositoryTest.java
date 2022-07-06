package study.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        for(int i = 1; i <= 10; i++) {
            em.persist(new Member(
                    "username" + i,
                    "loginId" + i,
                    "123",
                    i % 2 == 0 ? MemberAuth.NORMAL : i % 3 == 0 ? MemberAuth.MANAGER : MemberAuth.ADMINISTRATOR));
        }
    }

    @Test
    public void basicCRUD() throws Exception {
        // Create
        Member member = new Member("username1", "loginId1", "123", MemberAuth.ADMINISTRATOR);
        memberRepository.save(member);
        memberRepository.save(new Member("username2", "loginId2", "123", MemberAuth.MANAGER));
        memberRepository.save(new Member("username3", "loginId3", "123", MemberAuth.NORMAL));
        memberRepository.save(new Member("username4", "loginId4", "123", MemberAuth.NORMAL));
        memberRepository.save(new Member("username5", "loginId5", "123", MemberAuth.NORMAL));
        em.flush();
        em.clear();

        // Read
        Optional<Member> findMember = memberRepository.findById(member.getId());
        assertThat(findMember.orElse(null)).isNotNull();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getUsername()).isEqualTo(member.getUsername());

        List<Member> members = memberRepository.findAll();
        assertThat(members).extracting("username").containsExactly("username1", "username2", "username3", "username4", "username5");

        // Update
        member.changeUsername("username111");

        List<Member> findMembers = memberRepository.findByUsername("username111");
        assertThat(findMembers.size()).isEqualTo(1);
        assertThat(findMembers.get(0).getId()).isEqualTo(member.getId());

        // Delete
        memberRepository.delete(member);
        Optional<Member> deletedMember = memberRepository.findById(member.getId());
        assertThat(deletedMember.orElse(null)).isNull();
    }

    @Test
    public void findOptions() throws Exception {
        Optional<Member> result1 = memberRepository.findByLoginId("loginId1");
        assertThat(result1.orElse(null)).isNotNull();
        assertThat(result1.get().getUsername()).isEqualTo("username1");

        List<Member> result2 = memberRepository.findByUsername("username2");
        assertThat(result2.size()).isEqualTo(1);
        assertThat(result2.get(0).getUsername()).isEqualTo("username2");

        List<Member> result3 = memberRepository.findByMemberAuth(MemberAuth.NORMAL);
        assertThat(result3.size()).isEqualTo(5);
        assertThat(result3.get(0).getUsername()).isEqualTo("username2");
    }
}