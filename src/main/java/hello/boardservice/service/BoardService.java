package hello.boardservice.service;

import hello.boardservice.domain.board.Board;
import hello.boardservice.dto.ResponseMessage;
import hello.boardservice.dto.board.*;
import hello.boardservice.exception.BoardNotExistException;
import hello.boardservice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final FileService fileService;

    @Value("${file.requestUrl}")
    private String requestUrl;

    @Transactional
    public Board save(BoardSaveRequestDto boardSaveRequestDto) {
        return boardRepository.save(boardSaveRequestDto.toEntity());
    }

    @Transactional
    public Long update(Long id, BoardUpdateRequestDto boardUpdateRequestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotExistException(ResponseMessage.BOARD_FIND_BY_ID_FAIL));
        // 공지사항의 제목과 내용을 수정한다.
        board.update(boardUpdateRequestDto.getTitle(), boardUpdateRequestDto.getContent());
        return id;
    }

    @Transactional
    public BoardDetailResponseDto findById(Long id) {
        Board board = boardRepository.findBoard(id)
                .orElseThrow(() -> new BoardNotExistException(ResponseMessage.BOARD_FIND_BY_ID_FAIL));

        // 공지사항의 조회 수를 증가시킨다.
        board.plusViewCount();

        // 파일 엔터티를 Dto로 변환한다.
        List<UploadFileDto> uploadFileDtos = board.getUploadFiles()
                .stream()
                .map(uploadFile -> new UploadFileDto(uploadFile.getId(), requestUrl + uploadFile.getStoreFileName()))
                .collect(Collectors.toList());

        return new BoardDetailResponseDto(board, uploadFileDtos);
    }


    public List<BoardResponseDto> findList(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotExistException(ResponseMessage.BOARD_FIND_BY_ID_FAIL));

        fileService.deleteBoardFile(board);
        boardRepository.delete(board);
    }
}
