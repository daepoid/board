package study.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import study.board.dto.MemberDTO;
import study.board.service.MemberService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
}
