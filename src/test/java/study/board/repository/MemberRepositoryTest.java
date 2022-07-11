package study.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static study.board.domain.MemberAuth.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final int MAX = 10;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        for(int i = 1; i <= MAX; i++) {
            em.persist(new Member(
                    "username" + i,
                    "loginId" + i,
                    "123",
                    i % 2 == 0 ? NORMAL : i % 3 == 0 ? MANAGER : ADMINISTRATOR,
                    LocalDateTime.now()));
        }
    }

    @Test
    public void basicCRUD() throws Exception {
        // Create
        Member member = Member.builder()
                .loginId("username")
                .password("loginId")
                .username(passwordEncoder.encode("123"))
                .memberAuth(ADMINISTRATOR)
                .createdDate(LocalDateTime.now())
                .build();
        memberRepository.save(member);
        em.flush();
        em.clear();

        // Read
        Optional<Member> findMember = memberRepository.findById(member.getId());
        assertThat(findMember.orElse(null)).isNotNull();
        assertThat(findMember.get().getId()).isEqualTo(member.getId());
        assertThat(findMember.get().getUsername()).isEqualTo(member.getUsername());

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(MAX + 1);

        // Update
        String newUsername = "username111";
        findMember.get().changeUsername(newUsername);
        assertThat(findMember.get().getUsername()).isEqualTo(newUsername);
        em.flush();
        em.clear();

        List<Member> updatedMembers = memberRepository.findByUsername(newUsername);
        assertThat(updatedMembers.size()).isEqualTo(1);
        assertThat(updatedMembers.get(0).getId()).isEqualTo(findMember.get().getId());
        assertThat(updatedMembers.get(0).getUsername()).isEqualTo(newUsername);

        // Delete
        memberRepository.delete(updatedMembers.get(0));
        Optional<Member> deletedMember = memberRepository.findById(updatedMembers.get(0).getId());
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

        List<Member> result3 = memberRepository.findByMemberAuth(NORMAL);
        assertThat(result3.size()).isEqualTo(5);
        assertThat(result3.get(0).getUsername()).isEqualTo("username2");
    }
}