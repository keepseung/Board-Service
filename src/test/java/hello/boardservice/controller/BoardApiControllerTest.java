package hello.boardservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import hello.boardservice.dto.board.BoardUpdateRequestDto;
import hello.boardservice.repository.BoardRepository;
import hello.boardservice.repository.UploadFileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BoardApiControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @AfterEach
    void clearData(){
        uploadFileRepository.deleteAll();
        boardRepository.deleteAll();
    }
    @Test
    @DisplayName("공지사항 저장하기 및 응답 테스트")
    public void saveBoard() throws Exception {

        String title = "test title";
        String content = "test content";
        String author = "test author";
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate= LocalDateTime.now();

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        mockMvc.perform(multipart("/api/v1/board")
                        .file(firstFile)
                        .file(secondFile)
                        .file(jsonFile)
                        .param("title", title)
                        .param("content", content)
                        .param("author", author)
                        .param("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                        .param("endDate", endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                )
                .andExpect(status().isOk());

        // 최근에 저장된 순서로 조회하기
        List<Board> boards = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(boards.get(0).getTitle()).isEqualTo(title);
        assertThat(boards.get(0).getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("공지사항 제목, 내용 수정 테스트")
    public void updateBoard() throws Exception {
        //given
         Board savedPosts = boardRepository.save(Board.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/board/" + updateId;

        //when
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        // 최근에 저장된 순서로 조회하기
        List<Board> all = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
    }

    @Test
    @DisplayName("공지사항 아이디 조회 테스트")
    public void findBoardById() throws Exception {
        //given
        String now ="2021-01-01 12:30:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);

        Board savedPosts = boardRepository.save(Board.builder()
                .title("title")
                .content("content")
                .author("author")
                .viewCount(0)
                .boardStartDate(formatDateTime)
                .boardEndDate(formatDateTime)
                .build());
        UploadFile uploadFile = UploadFile.builder()
                .storeFileName("pretest1.jpg")
                .uploadFileName("3412-3123-adwd-1f2f.jpg")
                .board(savedPosts)
                .build();

        uploadFileRepository.save(uploadFile);

        //when
        String url = "http://localhost:" + port + "/api/v1/board/" + savedPosts.getId();
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        //then
        // 최근에 저장된 순서로 조회하기
        List<Board> all = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(all.get(0).getTitle()).isEqualTo(savedPosts.getTitle());
        assertThat(all.get(0).getContent()).isEqualTo(savedPosts.getContent());
    }

    @Test
    @DisplayName("공지사항 리스트 조회 테스트")
    public void getBoardList() throws Exception {
        //given
        String now ="2021-01-01 12:30:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formatDateTime = LocalDateTime.parse(now, formatter);
        Board savedPosts = boardRepository.save(Board.builder()
                .title("title")
                .content("content")
                .author("author")
                .viewCount(0)
                .boardStartDate(formatDateTime)
                .boardEndDate(formatDateTime)
                .build());
        UploadFile uploadFile = UploadFile.builder()
                .storeFileName("pretest1.jpg")
                .uploadFileName("3412-3123-adwd-1f2f.jpg")
                .board(savedPosts)
                .build();

        uploadFileRepository.save(uploadFile);

        String url = "http://localhost:" + port + "/api/v1/board";

        //when
        mvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());

        // then
        List<Board> all = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        assertThat(all.get(0).getTitle()).isEqualTo("title");
    }

    @Test
    @DisplayName("공지사항 삭제 테스트")
    void deleteBoard() throws Exception {
        //given
        Board savedBoard = boardRepository.save(Board.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedBoard.getId();
        String expectedTitle = "title2";
        String expectedContent = "content2";

        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/board/" + updateId;

        //when
        mvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        assertThrows(NoSuchElementException.class, ()->boardRepository.findBoard(savedBoard.getId()).get());
    }
}