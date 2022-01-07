package hello.boardservice.repository;

import hello.boardservice.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b left join fetch b.uploadFiles where b.id =:id")
    Optional<Board> findBoard(@Param("id") Long id);

}
