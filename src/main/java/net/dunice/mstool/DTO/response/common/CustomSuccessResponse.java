package net.dunice.mstool.DTO.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomSuccessResponse<T> {

    private T data;

    private Integer statusCode = 1;

    private Boolean success = true;

    private List<Integer> codes;

    public CustomSuccessResponse(T data) {
        this.data = data;
    }

    public CustomSuccessResponse(List<Integer> codes) {
        this.codes = codes;
    }

    public CustomSuccessResponse(Integer code, List<T> code1) {
        statusCode = code;
        codes = (List<Integer>) code1;
    }
}
