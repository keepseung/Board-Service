package hello.boardservice.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.boardservice.domain.board.Board;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardUpdateRequestDto {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime endDate;

    @Builder
    public BoardUpdateRequestDto(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .boardStartDate(startDate)
                .boardEndDate(endDate)
                .build();
    }

}
