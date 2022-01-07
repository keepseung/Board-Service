package hello.boardservice.controller;

import hello.boardservice.domain.board.Board;
import hello.boardservice.domain.board.UploadFile;
import hello.boardservice.dto.ApiResponse;
import hello.boardservice.dto.board.BoardDetailResponseDto;
import hello.boardservice.dto.board.BoardResponseDto;
import hello.boardservice.dto.board.BoardSaveRequestDto;
import hello.boardservice.dto.board.BoardUpdateRequestDto;
import hello.boardservice.exception.FileDownloadException;
import hello.boardservice.service.BoardService;
import hello.boardservice.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    private final FileService fileService;

    // 공지사항 등록
    @PostMapping(value = "/api/v1/board", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> save(@ModelAttribute BoardSaveRequestDto board) {

        try {

            // 공지사항 저장하기
            Board saveBoard = boardService.save(board);
            // 파일을 서버에 저장하기
            List<UploadFile> uploadFiles = fileService.storeFiles(board.getFiles(), saveBoard);
            // 업로드한 파일 정보를 서버에 저장하기
            fileService.saveFileAll(uploadFiles);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ApiResponse.setResponse(HttpStatus.CREATED.value());
    }

    // 공지사항 수정
    @PutMapping("/api/v1/board/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody @Valid BoardUpdateRequestDto requestDto) {
        boardService.update(id, requestDto);
        return ApiResponse.setResponse(HttpStatus.OK.value());
    }

    // 공지사항 아이디 조회
    @GetMapping("/api/v1/board/{id}")
    public ApiResponse<BoardDetailResponseDto> findById(@PathVariable Long id) {
        BoardDetailResponseDto boardDetailResponseDto = boardService.findById(id);
        return ApiResponse.setResponse(HttpStatus.OK.value(), boardDetailResponseDto);
    }

    // 공지사항 리스트 조회
    @GetMapping("/api/v1/board")
    public ApiResponse<List<BoardResponseDto>> findList(Pageable pageable) {
        List<BoardResponseDto> boardDetailResponseDtoList = boardService.findList(pageable);
        return ApiResponse.setResponse(HttpStatus.OK.value(), boardDetailResponseDtoList);
    }

    // 공지사항 삭제
    @DeleteMapping("/api/v1/board/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        boardService.deleteById(id);
        return ApiResponse.setResponse(HttpStatus.OK.value());
    }

    // 첨부파일 다운로드 기능
    @ResponseBody
    @GetMapping("/upload-file/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws MalformedURLException {
        Resource resource = new UrlResource("file:" + fileService.getFullPath(filename));

        // 파일이 존재하지 않은 경우
        if(!resource.exists()) {
            throw new FileDownloadException(filename + " 파일을 찾을 수 없습니다.");
        }else

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
