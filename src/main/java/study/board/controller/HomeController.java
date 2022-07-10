package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import study.board.SessionConst;
import study.board.domain.Member;
import study.board.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static study.board.SessionConst.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String mainRootPage(Model model, HttpServletRequest request) {
        if(request.getSession(false) == null) {
            request.getSession();
        }
        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            log.info("로그인 실패 - 로그인 정보 없음");
            return "home";
        }
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if(member.isEmpty()){
            log.info("로그인 실패 - 로그인 정보 매칭 안됨");
            return "home";
        }

        log.info("로그인 성공");
        model.addAttribute("memberId", member.get().getId());
        return "loginHome";
    }
}
