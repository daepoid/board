package study.board.repository;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Member;
import study.board.dto.MemberDTO;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.board.domain.MemberAuth.*;

@SpringBootTest
@Transactional
class MemberRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;

    JPAQueryFactory queryFactory;

    @Autowired
    EntityManager em;

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
    public void findMemberDTOsList() throws Exception {
        List<MemberDTO> memberDTOs = memberRepository.findMemberDTOs();
        assertThat(memberDTOs.size()).isEqualTo(MAX);
        assertThat(memberDTOs.get(0).getUsername()).isEqualTo("username1");
    }

    @Test
    public void findMemberDTOsPage() throws Exception {
        final int offset = 0;
        final int limit = 5;
        Page<MemberDTO> memberDTOs = memberRepository.findMemberDTOs(PageRequest.of(offset, 5));
        assertThat(memberDTOs.getTotalElements()).isEqualTo(MAX);
        assertThat(memberDTOs.getSize()).isEqualTo(limit);
        assertThat(memberDTOs.getNumber()).isEqualTo(offset);
        assertThat(memberDTOs.getTotalPages()).isEqualTo(Math.max(0, (MAX - 1)) / limit + 1);
        assertThat(memberDTOs.getNumberOfElements()).isEqualTo(Math.min(MAX, limit));
        assertThat(memberDTOs.getContent().get(0).getUsername()).isEqualTo("username1");
    }
}