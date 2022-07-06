package study.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import study.board.domain.Member;
import study.board.domain.MemberAuth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@ToString
@NoArgsConstructor
public class MemberDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String loginId;

    @NotNull
    private MemberAuth memberAuth;

    @QueryProjection
    public MemberDTO(Long id, String username, String loginId, MemberAuth memberAuth) {
        this.id = id;
        this.username = username;
        this.loginId = loginId;
        this.memberAuth = memberAuth;
    }

    @QueryProjection
    public MemberDTO(Member member) {
        if(member == null) {
            this.id = null;
            this.username = null;
            this.loginId = null;
            this.memberAuth = MemberAuth.UNDEFINED;
        } else {
            this.id = member.getId();
            this.username = member.getUsername();
            this.loginId = member.getLoginId();
            this.memberAuth = member.getMemberAuth();
        }
    }
}
