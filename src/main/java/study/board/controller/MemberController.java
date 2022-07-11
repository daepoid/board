package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.board.domain.Member;
import study.board.domain.MemberAuth;
import study.board.dto.CreateMemberDTO;
import study.board.dto.MemberDTO;
import study.board.service.MemberService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public String memberListV2(Model model, Pageable pageable) {
        Page<MemberDTO> memberDTOs = memberService.findMemberDTOs(pageable);
        model.addAttribute("memberDTOs", memberDTOs);

//        double start = Math.floor(memberDTOs.getNumber() / 5) * 5 + 1;
        double start = (int)(memberDTOs.getNumber() / 5) * 5 + 1;
        double last = start + 4 < memberDTOs.getTotalPages() ? start + 4 : Math.max(start, memberDTOs.getTotalPages());
        model.addAttribute("start", start);
        model.addAttribute("last", last);
        return "members/memberList";
    }

    @GetMapping("/{loginId}")
    public String memberInfoByManager(@PathVariable("loginId") Long memberId, Model model) {
        Optional<MemberDTO> memberDTO = memberService.findMemberDTO(memberId);
        if(memberDTO.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("memberDTO", memberDTO.get());
        return "members/memberInfo";
    }

    @GetMapping("/new")
    public String joinMemberForm(@ModelAttribute("createMemberDTO") CreateMemberDTO createMemberDTO) {
        return "members/createMember";
    }

    @PostMapping("/new")
    public String joinMember(@ModelAttribute("createMemberDTO") CreateMemberDTO createMemberDTO) {
        // 아이디 검증
        if(!memberService.validateLoginId(createMemberDTO.getLoginId())) {
            log.info("아이디 검증 실패 = {}", createMemberDTO.getLoginId());
            return "members/createMember";
        }

        // 비밀번호 검증
        if(!memberService.validatePassword(createMemberDTO.getPassword())) {
            log.info("비밀번호 검증 실패 = {}", createMemberDTO.getPassword());
            return "members/createMember";
        }

        if(!createMemberDTO.getPassword().equals(createMemberDTO.getPasswordCheck())) {
            log.info("비밀번호확인 동일성 검증 실패 = {} != {}",
                    createMemberDTO.getPassword(),
                    createMemberDTO.getPasswordCheck());
            return "members/createMember";
        }

        Member member = Member.builder()
                .username(createMemberDTO.getUsername())
                .loginId(createMemberDTO.getLoginId())
                .password(passwordEncoder.encode(createMemberDTO.getPassword()))
                .memberAuth(MemberAuth.NORMAL)
                .createdDate(LocalDateTime.now())
                .build();

        memberService.save(member);
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/new/id-check")
    public boolean idCheck(String loginId) {
        log.info("아이디 검증 => {}", loginId);
        return memberService.findMemberDTOByLoginId(loginId).isEmpty();
    }

    @ResponseBody
    @PostMapping("/new/password-check")
    public boolean passwordCheck(String password, String passwordCheck) {
        log.info("비밀번호 검증 => {} : {}", password, passwordCheck);
        return !password.isBlank() && password.equals(passwordCheck);
    }

    @ResponseBody
    @PostMapping("/new/phone-check")
    public boolean phoneNumberCheck(String phoneNumber) {
        log.info("전화번호 검증 => {}", phoneNumber);
        return !phoneNumber.isBlank() && phoneNumber.matches("^(01[0-1|6-9])-?(\\d{3,4})-?(\\d{4})$");
    }
}
