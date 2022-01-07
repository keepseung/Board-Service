package hello.boardservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API 성공했을 경우 기본 응답 데이터
 * @param <T>: 응답 값 json에서 키가 "data"에 해당하는 값 객체이다.
 * 값이 없을 경우 null,
 * 리스트 조회할 떄는 빈 리스트를 가진다.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiResponse<T> {

    private int code;
    private T data;

    public ApiResponse(int code) {
        this.code = code;
        this.data = null;
    }

    public static<T> ApiResponse<T> setResponse(int statusCode) {
        return setResponse(statusCode, null);
    }

    public static<T> ApiResponse<T> setResponse(int statusCode, T t) {
        return ApiResponse.<T>builder()
                .data(t)
                .code(statusCode)
                .build();
    }
}
