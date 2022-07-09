package study.board.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Article;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitData {
    private final InitMemberService initMemberService;
    @PostConstruct
    public void init() {
        initMemberService.init();
    }

    @Component
    static class InitMemberService {
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            for(int i = 1; i <= 100; i++) {
                MemberAuth memberAuth = switch (i) {
                    case 1 -> MemberAuth.ADMINISTRATOR;
                    case 2 -> MemberAuth.MANAGER;
                    case 3 -> MemberAuth.UNDEFINED;
                    default -> MemberAuth.NORMAL;
                };
                em.persist(Member.builder()
                        .username("username" + i)
                        .loginId("loginId" + i)
                        .password("123")
                        .memberAuth(memberAuth)
                        .createdDate(LocalDateTime.now())
                        .build());
            }
        }
    }
}
