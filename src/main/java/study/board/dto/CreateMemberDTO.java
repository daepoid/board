package study.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import study.board.domain.Member;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberDTO {

    @NotBlank
    String loginId;

    @NotBlank
    String password;

    @NotBlank
    String username;
}
