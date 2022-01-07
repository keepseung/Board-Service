package hello.boardservice.repository;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    void deleteUploadFileByBoard(Board board);
}
