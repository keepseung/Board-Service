package hello.boardservice.service;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import hello.boardservice.dto.board.BoardDetailResponseDto;
import hello.boardservice.dto.board.BoardResponseDto;
import hello.boardservice.dto.board.BoardSaveRequestDto;
import hello.boardservice.dto.board.BoardUpdateRequestDto;
import hello.boardservice.repository.BoardRepository;
import hello.boardservice.repository.UploadFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Rollback(value = false)
@SpringBootTest
public class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void saveTestData() {
        // 테스트 시작 전에 공지사항과 첨부파일 저장하기
        BoardSaveRequestDto board1 = createBoardSaveDto("미리 저장한 공지 제목1", "미리 저장할 공지 내용1", "미리 저장할 공지 작가1", LocalDateTime.now(), LocalDateTime.now());
        BoardSaveRequestDto board2 = createBoardSaveDto("미리 저장한 공지 제목2", "미리 저장할 공지 내용2", "미리 저장할 공지 작가2", LocalDateTime.now(), LocalDateTime.now());
        BoardSaveRequestDto board3 = createBoardSaveDto("미리 저장한 공지 제목3", "미리 저장할 공지 내용3", "미리 저장할 공지 작가3", LocalDateTime.now(), LocalDateTime.now());
        BoardSaveRequestDto board4 = createBoardSaveDto("미리 저장한 공지 제목4", "미리 저장할 공지 내용4", "미리 저장할 공지 작가4", LocalDateTime.now(), LocalDateTime.now());
        Board savedBoard1 = boardRepository.save(board1.toEntity());
        Board savedBoard2 = boardRepository.save(board2.toEntity());
        boardRepository.save(board3.toEntity());
        boardRepository.save(board4.toEntity());

        UploadFile uploadFile1 = createUploadFile("pretest1.jpg", "3412-3123-adwd-1f2f.jpg", savedBoard1);
        UploadFile uploadFile2 = createUploadFile("pretest2.jpg", "2412-3123-adwd-1f2f.jpg", savedBoard1);
        UploadFile uploadFile3 = createUploadFile("pretest2.jpg", "2412-3123-adwd-1f2f.jpg", savedBoard2);
        uploadFileRepository.save(uploadFile1);
        uploadFileRepository.save(uploadFile2);
        uploadFileRepository.save(uploadFile3);
    }

    @AfterEach
    void clearData(){
        uploadFileRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("첨부파일 없는 공지사항 추가 테스트")
    void saveBoard() {
        // given
        BoardSaveRequestDto board1 = createBoardSaveDto("테스트 제목1", "테스트 내용1", "테스트 작가1", LocalDateTime.now(), LocalDateTime.now());
        BoardSaveRequestDto board2 = createBoardSaveDto("테스트 제목2", "테스트 내용2", "테스트 작가2", LocalDateTime.now(), LocalDateTime.now());

        // when
        Board saveBoard1 = boardService.save(board1);
        Board saveBoard2 = boardService.save(board2);

        // then
        Board findBoard1 = boardRepository.findById(saveBoard1.getId()).get();
        Board findBoard2 = boardRepository.findById(saveBoard2.getId()).get();

        assertThat(saveBoard1).isEqualTo(findBoard1);
        assertThat(saveBoard2).isEqualTo(findBoard2);
    }

    // 수정
    @Test
    @DisplayName("공지사항 수정 테스트")
    void updateBoard() {
        // given
        Board board = boardRepository.findAll().get(0);
        BoardUpdateRequestDto updateRequestDto = BoardUpdateRequestDto.builder()
                .title("수정 제목11")
                .content("수정 내용")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        // when
        boardService.update(board.getId(), updateRequestDto);

        // then
        Board findBoard = boardRepository.findById(board.getId()).get();
        assertThat("수정 제목11").isEqualTo(findBoard.getTitle());
        assertThat("수정 내용").isEqualTo(findBoard.getContent());
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void deleteBoard() {
        Board board = boardRepository.findAll().get(0);
        // when
        boardService.deleteById(board.getId());

        // then
        assertThrows(NoSuchElementException.class, () ->  boardRepository.findById(board.getId()).get());
    }

    @Test
    @DisplayName("공지사항 리스트 페이징 조회 테스트")
    void findAllBoard() {
        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        // when
        List<BoardResponseDto> findList = boardService.findList(pageRequest);

        // then
        assertThat(findList.size()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("공지사항 한 건 조회 테스트")
    void findOneBoard() {
        // given
        BoardSaveRequestDto board1 = createBoardSaveDto("테스트 제목1", "테스트 내용1", "테스트 작가1", LocalDateTime.now(), LocalDateTime.now());
        Board savedBoard1 = boardRepository.save(board1.toEntity());

        UploadFile uploadFile1 = createUploadFile("test1.jpg", "3412-3123-adwd-1f2f.jpg", savedBoard1);
        uploadFileRepository.save(uploadFile1);

        // insert 쿼리를 실행해 영속성 컨텍스트를 내용을 DB에 반영하도록 한다.
        em.flush();
        em.clear();

        // when
        BoardDetailResponseDto findDetailDto = boardService.findById(savedBoard1.getId());

        // then
        assertThat(findDetailDto.getTitle()).isEqualTo(savedBoard1.getTitle());
        assertThat(findDetailDto.getContent()).isEqualTo(savedBoard1.getContent());
        assertThat(findDetailDto.getViewCount()).isEqualTo(1);
        assertThat(findDetailDto.getUploadFiles().size()).isEqualTo(1);
        assertThat(findDetailDto.getUploadFiles().get(0).getId()).isEqualTo(uploadFile1.getId());
    }

    private BoardSaveRequestDto createBoardSaveDto(String title, String content, String author, LocalDateTime startDate, LocalDateTime endDate) {
        return BoardSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .startDate(startDate)
                .endDate(endDate)
                .build();
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
