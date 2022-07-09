package study.board.dto;

import lombok.*;
import study.board.domain.Member;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdatableDTO {

    private Long id;

    @NotBlank
    private String username;

    public MemberUpdatableDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }

    public MemberUpdatableDTO(MemberDTO memberDTO) {
        this.id = memberDTO.getId();
        this.username = memberDTO.getUsername();
    }
}
