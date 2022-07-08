package study.board.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
