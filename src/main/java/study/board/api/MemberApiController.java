package study.board.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import study.board.domain.Member;
import study.board.domain.MemberAuth;
import study.board.dto.CreateMemberDTO;
import study.board.dto.MemberDTO;
import study.board.dto.MemberUpdatableDTO;
import study.board.service.MemberService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@Api(tags = {"사용자 정보 API"})
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/v1/members")
    @ApiOperation(value = "전체 사용자 페이징 조회", notes = "전체 고객을 페이징 처리하여 페이지 타입으로 반환")
    public Page<MemberDTO> findMembersV1(Pageable pageable) {
        return memberService.findMemberDTOs(pageable);
    }

    @PostMapping("/v1/members/new")
    @ApiOperation(value = "사용자 생성", notes = "고객을 생성하고 아이디를 반환")
    public Long createMemberV1(@RequestBody @Valid CreateMemberDTO createMemberDTO) {

        // 아이디 검증
        if(!memberService.validateLoginId(createMemberDTO.getLoginId())) {
            log.info("아이디 검증 실패 = {}", createMemberDTO.getLoginId());
            return -1L;
        }

        // 비밀번호 검증
        if(!memberService.validatePassword(createMemberDTO.getPassword())) {
            log.info("비밀번호 검증 실패 = {}", createMemberDTO.getPassword());
            return -1L;
        }

        Member member = Member.builder()
                .username(createMemberDTO.getUsername())
                .loginId(createMemberDTO.getLoginId())
                .password(passwordEncoder.encode(createMemberDTO.getPassword()))
                .memberAuth(MemberAuth.NORMAL)
                .createdDate(LocalDateTime.now())
                .build();

        memberService.save(member);
        return member.getId();
    }

    @GetMapping("/v1/members/{memberId}")
    @ApiOperation(value = "사용자 조회", notes = "사용자를 조회하여 사용자 정보를 반환")
    public MemberDTO findMemberV1(@PathVariable("memberId") Long memberId) {
        return memberService.findMemberDTO(memberId).orElse(null);
    }

    @PostMapping("/v1/members/{memberId}")
    @ApiOperation(value = "사용자 정보 수정", notes = "사용자 정보를 수정하고 아이디를 반환")
    public Long updateMember(@PathVariable("memberId") Long memberId,
                             @RequestBody MemberUpdatableDTO memberUpdatableDTO) {
        memberService.updateMember(memberId, memberUpdatableDTO);
        return memberId;
    }

    @DeleteMapping("/v1/members/{memberId}")
    @ApiOperation(value = "사용자 정보 삭제", notes = "사용자를 삭제하고 아이디를 반환")
    public Long deleteMember(@PathVariable("memberId") Long memberId) {
        memberService.deleteMember(memberId);
        return memberId;
    }
}
