package hello.boardservice.dto.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UploadFileDto {
    private Long id;
    private String url;

    public UploadFileDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }
}
