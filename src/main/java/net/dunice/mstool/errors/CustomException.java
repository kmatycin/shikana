package net.dunice.mstool.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dunice.mstool.constants.ErrorCodes;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCodes errorCodes;

}
