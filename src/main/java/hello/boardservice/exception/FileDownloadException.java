package hello.boardservice.exception;

/**
 * 파일 다운로드시 발생하는 예외사항
 *
 */
public class FileDownloadException extends RuntimeException {
    public FileDownloadException(String message) {
        super(message);
    }
    public FileDownloadException(String message, Throwable cause) {
        super(message, cause);
    }
}