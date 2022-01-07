package hello.boardservice.repository;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("첨부 파일이 없는 공지사항 저장 및 조회하기")
    void boardSaveAndGetList() {
        // given
        // 공지사항 저장하기
        String title = "테스트 공지사항";
        String content = "테스트 본문";
        String author = "author";

        String now ="2021-01-01 12:30:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);

        boardRepository.save(Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .viewCount(0)
                .boardStartDate(formatDateTime)
                .boardEndDate(formatDateTime)
                .build());

        //when
        // 저장한 공지사항 조회하기
        List<Board> boardList = boardRepository.findAll();
        Board savedBoard = boardList.get(0);

        //then
        assertThat(savedBoard.getTitle()).isEqualTo(savedBoard.getTitle());
        assertThat(savedBoard.getContent()).isEqualTo(savedBoard.getContent());
    }

    @AfterEach
    void clearData(){
        uploadFileRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("첨부 파일이 있는 공지사항 저장 및 조회하기")
    void boardFileSaveAndGetList() {
        // given
        // 공지사항 저장 및 업로드 파일 저장하기
        // 공지사항 저장하기
        String title = "테스트 공지사항";
        String content = "테스트 본문";
        String author = "author";

        String now ="2021-01-01 12:30:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);

        Board savedBoard = boardRepository.save(Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .viewCount(0)
                .boardStartDate(formatDateTime)
                .boardEndDate(formatDateTime)
                .build());

        String uploadFileName = "testFilename.jpg";
        String storeFileName = UUID.randomUUID().toString();
        UploadFile uploadFile = createUploadFile(uploadFileName, storeFileName, savedBoard);

        // when
        // 저장한 업로드 파일 조회하기
        UploadFile savedUploadFile = uploadFileRepository.findById(uploadFile.getId()).get();

        //then
        assertThat(savedUploadFile.getUploadFileName()).isEqualTo(uploadFileName);
        assertThat(savedUploadFile.getStoreFileName()).isEqualTo(storeFileName);
        assertThat(savedUploadFile.getBoard()).isEqualTo(savedBoard);
        assertThat(savedBoard.getTitle()).isEqualTo(savedBoard.getTitle());
        assertThat(savedBoard.getContent()).isEqualTo(savedBoard.getContent());
    }

    private Board createBoard(String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        Board board = Board.builder()
                .title(title)
                .content(content)
                .boardStartDate(startDate)
                .boardEndDate(endDate)
                .build();
        em.persist(board);
        return board;
    }

    private UploadFile createUploadFile(String uploadFileName, String storeFileName, Board board) {
        UploadFile uploadFile = UploadFile.builder()
                .storeFileName(storeFileName)
                .uploadFileName(uploadFileName)
                .board(board)
                .build();
        em.persist(uploadFile);
        return uploadFile;
    }

}