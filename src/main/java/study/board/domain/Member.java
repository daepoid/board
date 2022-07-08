package study.board.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "loginId", "memberAuth"})
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Column(updatable = false)
    private String loginId;

    @NotBlank
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberAuth memberAuth;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @Builder
    public Member(String username, String loginId, String password,
                  MemberAuth memberAuth, LocalDateTime createdDate) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
        this.memberAuth = memberAuth;
        this.createdDate = createdDate;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeMemberAuth(MemberAuth memberAuth) {
        this.memberAuth = memberAuth;
    }

    public void changeCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public void admitMember() {
        this.memberAuth = MemberAuth.NORMAL;
    }
}
