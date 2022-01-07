package hello.boardservice.domain.board;

import hello.boardservice.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 테이블
 */
@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseTimeEntity {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(columnDefinition = "integer default 0")
    private Integer viewCount;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<UploadFile> uploadFiles = new ArrayList<>();

    private LocalDateTime boardStartDate;
    private LocalDateTime boardEndDate;

    @Builder
    public Board(Long id, String title, String content, String author, Integer viewCount, LocalDateTime boardStartDate, LocalDateTime boardEndDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.viewCount = viewCount;
        this.boardStartDate = boardStartDate;
        this.boardEndDate = boardEndDate;
    }

    // 공지사항 제목, 내용 수정한다.
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수가 증가한다.
    public void plusViewCount() {
        this.viewCount++;
    }
}
