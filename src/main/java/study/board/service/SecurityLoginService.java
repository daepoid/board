package study.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.board.domain.Member;
import study.board.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityLoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 진입 = {}", username);
        Optional<Member> member = memberRepository.findByLoginId(username);
        if(member.isPresent()) {
            String role = "ROLE_" + member.get().getMemberAuth().toString();
            log.info("loadUserByUsername Role = {}", role);
            return new User(member.get().getLoginId(), member.get().getPassword(), List.of(new SimpleGrantedAuthority(role)));
        }
        throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }
}
