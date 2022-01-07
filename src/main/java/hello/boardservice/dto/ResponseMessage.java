package hello.boardservice.dto;

public class ResponseMessage {
    public static final String BOARD_FIND_BY_ID_FAIL = "아이디에 해당하는 공지사항이 없습니다.";
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String BAD_REQUEST = "잘못된 요청입니다.";
    public static final String EXCEED_FILE_SIZE = "10M 이하인 파일만 업로드 가능합니다.";
}
