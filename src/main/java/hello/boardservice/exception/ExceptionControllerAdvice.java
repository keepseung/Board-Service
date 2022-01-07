package hello.boardservice.exception;

import hello.boardservice.dto.ApiErrorResponse;
import hello.boardservice.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 4xx, 5xx 에러 발생시 클라이언트에게 전달할 응답, 상태코드를 처리함
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    public ApiErrorResponse<?> illegalExceptionHandle() {
        return ApiErrorResponse.setResponse(
                HttpStatus.BAD_REQUEST.value(),
                ResponseMessage.BAD_REQUEST
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BoardNotExistException.class)
    public ApiErrorResponse<?> boardNotExistExceptionHandle() {
        return ApiErrorResponse.setResponse(
                HttpStatus.BAD_REQUEST.value(),
                ResponseMessage.BOARD_FIND_BY_ID_FAIL
        );
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ApiErrorResponse<?> mediaTypeException() {
        return ApiErrorResponse.setResponse(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiErrorResponse<?> maxUploadSizeExceededException() {
        return ApiErrorResponse.setResponse(
                HttpStatus.BAD_REQUEST.value(),
                ResponseMessage.EXCEED_FILE_SIZE
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiErrorResponse<?> exceptionHandle(Exception e) {
        log.error("error " + e);
        return ApiErrorResponse.setResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ResponseMessage.INTERNAL_SERVER_ERROR
        );
    }
}
