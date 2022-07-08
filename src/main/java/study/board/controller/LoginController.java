package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.board.SessionConst;
import study.board.domain.Member;
import study.board.dto.LoginDTO;
import study.board.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

import static study.board.SessionConst.LOGIN_MEMBER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginDTO") LoginDTO loginDTO,
                            HttpServletRequest request) {
        request.getSession();
        return "loginPage";
    }

    @PostMapping("/login")
    public String loginMember(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                              HttpServletRequest request) {
        Optional<Member> member = memberRepository.findByLoginId(loginDTO.getLoginId());
        if(member.isEmpty() || !member.get().getPassword().equals(loginDTO.getPassword())) {
            log.info("로그인 실패");
            return "redirect:/login";
        }

        log.info("로그인 성공 {}", loginDTO.getLoginId());
        request.getSession(false).setAttribute(LOGIN_MEMBER, loginDTO.getLoginId());
        return "redirect:/";
    }
}
