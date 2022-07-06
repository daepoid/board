package study.board.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import study.board.domain.Member;
import study.board.domain.MemberAuth;
import study.board.dto.MemberDTO;
import study.board.repository.MemberRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberRepository memberRepository;

    @GetMapping("/v1/members")
    public List<Member> memberList() {
        return memberRepository.findAll();
    }

    @GetMapping("/v2/members")
    public List<MemberDTO> memberDTOList() {
        return memberRepository.findMemberDTOList();
    }

    @GetMapping("/v3/members")
    public Page<MemberDTO> memberDTOPage(Pageable pageable) {
        return memberRepository.findMemberDTOPage(pageable);
    }

    @PostMapping("/v1/members")
    public Long createMember(@RequestBody @Valid MemberDTO memberDTO) {
        Member member = Member.builder()
                .username(memberDTO.getUsername())
                .loginId(memberDTO.getLoginId())
                .memberAuth(memberDTO.getMemberAuth())
                .password("123")
                .build();

        memberRepository.save(member);
        return member.getId();
    }

    @GetMapping("/v1/members/{memberId}")
    public Member findMemberById(@PathVariable("memberId") Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElse(null);
    }

    @GetMapping("/v2/members/{memberId}")
    public MemberDTO findMemberDTOById(@PathVariable("memberId") Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return new MemberDTO(member.orElse(null));
    }

    @PostMapping("/v1/members/{memberId}")
    public Long updateMember(@PathVariable("memberId") Long memberId,
                             @RequestParam("username") String username,
                             @RequestParam("memberAuth") MemberAuth memberAuth) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            return -1L;
        }

        member.get().changeUsername(username);
        member.get().changeMemberAuth(memberAuth);
        return memberId;
    }

    @DeleteMapping("/v1/members/{memberId}")
    public Long deleteMember(@PathVariable("memberId") Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            return -1L;
        }
        memberRepository.delete(member.get());
        return memberId;
    }
}
