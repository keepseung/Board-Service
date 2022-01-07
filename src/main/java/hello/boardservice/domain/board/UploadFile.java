package hello.boardservice.domain.board;

import hello.boardservice.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 공지사항 첨부 파일 테이블
 */
@Getter
@NoArgsConstructor
@Entity
public class UploadFile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uploadFileName;

    @Column(nullable = false)
    private String storeFileName;

    @Builder
    public UploadFile(String uploadFileName, String storeFileName, Board board) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.board = board;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

}
