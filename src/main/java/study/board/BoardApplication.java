package study.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

import static study.board.SessionConst.*;

@EnableJpaAuditing // Auditing 사용시에 꼭 필요
@SpringBootApplication
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		// 실무에서는 Spring Security Context Holder 에서 세션 정보를 가져와서 아이디를 꺼내야 한다.
		// Http Session에서 정보를 꺼내서 사용해야한다.
		return () -> {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			String loginId = (String) attr.getRequest().getSession().getAttribute(LOGIN_MEMBER);
			if(loginId != null){
				return Optional.of(loginId);
			} else {
				return Optional.of("Anonymous");
			}
		};
	}

	@Bean
	JPAQueryFactory jpaQueryFactory (EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
