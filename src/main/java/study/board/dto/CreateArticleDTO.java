package study.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleDTO {

    @NotNull
    private Long memberId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String imgUrl;
}
