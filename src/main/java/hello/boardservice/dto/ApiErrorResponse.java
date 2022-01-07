package hello.boardservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse<T> {

    @NotEmpty
    private int code;
    @NotEmpty
    private String message;

    public static<T> ApiErrorResponse<T> setResponse(int statusCode, String responseMessage) {
        return ApiErrorResponse.<T>builder()
                .code(statusCode)
                .message(responseMessage)
                .build();
    }
}