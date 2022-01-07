package hello.boardservice;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import hello.boardservice.dto.board.BoardSaveRequestDto;
import hello.boardservice.dto.board.UploadFileDto;
import hello.boardservice.repository.BoardRepository;
import hello.boardservice.repository.UploadFileRepository;
import hello.boardservice.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class BoardserviceApplication {

    private final BoardService boardService;
    private final UploadFileRepository uploadFileRepository;
    public static void main(String[] args) {
        SpringApplication.run(BoardserviceApplication.class, args);
    }

    @PostConstruct
    private void testData(){
        BoardSaveRequestDto board1 = BoardSaveRequestDto.builder()
                .title("테스트11")
                .content("테스트 내용 11")
                .author("테스트 내용 11")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        BoardSaveRequestDto board2 = BoardSaveRequestDto.builder()
                .title("테스트22")
                .content("테스트 내용22")
                .author("테스트 내용 22")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        Board save1 = boardService.save(board1);
        Board save2 = boardService.save(board2);


        UploadFile file1 = UploadFile.builder()
                .uploadFileName("test1.jpg")
                .storeFileName("test1.jpg")
                .board(save1)
                .build();
        UploadFile file2 = UploadFile.builder()
                .uploadFileName("test2.jpg")
                .storeFileName("test2.jpg")
                .board(save1)
                .build();
        UploadFile file3 = UploadFile.builder()
                .uploadFileName("test3.jpg")
                .storeFileName("test3.jpg")
                .board(save2)
                .build();
        uploadFileRepository.save(file1);
        uploadFileRepository.save(file2);
        uploadFileRepository.save(file3);

    }
}
