package hello.boardservice.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import hello.boardservice.domain.board.Board;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSaveRequestDto {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    @Size(max = 255)
    private String author;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    private List<MultipartFile> files = new ArrayList<>();

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .viewCount(0)
                .boardStartDate(startDate)
                .boardEndDate(endDate)
                .build();
    }

    @Builder
    public BoardSaveRequestDto(String title, String content, String author, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
