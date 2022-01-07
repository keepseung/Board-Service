package hello.boardservice.exception;

/**
 * 사용자가 요청한 아이디 값에 대한 공지사항이 존재하지 않는 예외사항을 처리함
 */
public class BoardNotExistException extends IllegalArgumentException{
    public BoardNotExistException() {
    }

    public BoardNotExistException(String s) {
        super(s);
    }

    public BoardNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardNotExistException(Throwable cause) {
        super(cause);
    }
}
