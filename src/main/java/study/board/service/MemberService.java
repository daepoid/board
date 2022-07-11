package study.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.domain.Member;
import study.board.dto.MemberDTO;
import study.board.dto.MemberUpdatableDTO;
import study.board.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {
        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public Optional<MemberDTO> findMemberDTO(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new MemberDTO(member.get()));
        }
    }

    public Optional<MemberDTO> findMemberDTOByLoginId(String loginId) {
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        if(member.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new MemberDTO(member.get()));
        }
    }

    public List<MemberDTO> findMemberDTOs() {
        return memberRepository.findMemberDTOs();
    }

    public Page<MemberDTO> findMemberDTOs(Pageable pageable) {
        return memberRepository.findMemberDTOs(pageable);
    }

    @Transactional
    public void editMemberInfo(MemberUpdatableDTO memberUpdatableDTO) {
        Optional<Member> member = memberRepository.findById(memberUpdatableDTO.getId());
        member.ifPresent(value -> value.changeUsername(memberUpdatableDTO.getUsername()));
    }

    @Transactional
    public void updateMember(Long memberId, MemberUpdatableDTO memberUpdatableDTO) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()) {
            return;
        }

        String newUsername = memberUpdatableDTO.getUsername();
        member.get().changeUsername(newUsername.isBlank() ? member.get().getUsername() : newUsername);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        member.ifPresent(memberRepository::delete);
    }

    public boolean validateLoginId(String loginId) {
        return loginId.matches("^\\w{8,20}$");
    }

    public boolean validatePassword(String password) {
        String regex =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$";
        String samePw = "(\\w)\\1\\1";

        return !password.isBlank() && password.matches(regex) && !password.matches(samePw);
    }
}
