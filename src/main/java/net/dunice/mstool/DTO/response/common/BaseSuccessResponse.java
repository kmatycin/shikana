package net.dunice.mstool.DTO.response.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class BaseSuccessResponse {

    private Integer statusCode = 0;

    private Boolean success = true;
}
