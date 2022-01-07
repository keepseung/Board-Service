package hello.boardservice.dto.board;

import hello.boardservice.domain.board.Board;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDetailResponseDto extends BoardResponseDto {
    private String boardStartDate;
    private String boardEndDate;
    private List<UploadFileDto> uploadFileDtoList = new ArrayList<>();

    public BoardDetailResponseDto(Board board, List<UploadFileDto> uploadFileDtos) {
        super(board);
        this.boardStartDate = getDateTimeString(board.getBoardStartDate());
        this.boardEndDate = getDateTimeString(board.getBoardEndDate());
        this.uploadFileDtoList = uploadFileDtos;
    }
}
