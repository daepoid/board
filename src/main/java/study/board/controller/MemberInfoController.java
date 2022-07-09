package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.board.dto.MemberDTO;
import study.board.dto.MemberUpdatableDTO;
import study.board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Optional;

import static study.board.SessionConst.*;

@Slf4j
@Controller
@RequestMapping("/my-info")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberService memberService;

    @GetMapping("")
    public String memberInfoForm(Model model, HttpServletRequest request) {
        if(request.getSession(false) == null) {
            request.getSession();
            return "redirect:/";
        }

        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            return "redirect:/";
        }

        Optional<MemberDTO> memberDTO = memberService.findMemberDTOByLoginId(loginId);
        if(memberDTO.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("memberDTO", memberDTO.get());
        return "members/memberInfo";
    }

    @GetMapping("/edit")
    public String editMemberInfoForm(Model model, HttpServletRequest request) {
        if(request.getSession(false) == null) {
            request.getSession();
            return "redirect:/";
        }

        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            return "redirect:/";
        }

        Optional<MemberDTO> memberDTO = memberService.findMemberDTOByLoginId(loginId);
        if(memberDTO.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("memberUpdatableDTO", new MemberUpdatableDTO(memberDTO.get()));
        return "members/editMyInfo";
    }

    @PostMapping("/edit")
    public String editMemberInfo(@Valid @ModelAttribute("memberUpdatableDTO") MemberUpdatableDTO memberUpdatableDTO, HttpServletRequest request) {
        if(request.getSession(false) == null) {
            request.getSession();
            return "redirect:/";
        }

        String loginId = (String) request.getSession(false).getAttribute(LOGIN_MEMBER);
        if(loginId == null) {
            return "redirect:/";
        }

        Optional<MemberDTO> findMemberDTO = memberService.findMemberDTOByLoginId(loginId);
        if(findMemberDTO.isEmpty()) {
            return "redirect:/";
        }

        if(!memberUpdatableDTO.getId().equals(findMemberDTO.get().getId())) {
            return "redirect:/";
        }

        log.info("memberUpdatableDTO = {}", memberUpdatableDTO);
        memberService.editMemberInfo(memberUpdatableDTO);
        return "redirect:/my-info";
    }

}
