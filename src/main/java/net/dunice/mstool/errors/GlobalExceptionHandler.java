package net.dunice.mstool.errors;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import net.dunice.mstool.DTO.response.common.BaseSuccessResponse;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.constants.ConstantFields;
import net.dunice.mstool.constants.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    ResponseEntity<CustomSuccessResponse<Integer>> handle(CustomException e) {
        Integer code = e.getErrorCodes().getCode();
        return ResponseEntity.badRequest()
                .header(ConstantFields.HEADER_ERROR_MSG, e.getErrorCodes().getMessage())
                .body(new CustomSuccessResponse<>(code, List.of(code)));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<CustomSuccessResponse<Integer>> handle(ExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header(ConstantFields.HEADER_ERROR_MSG, "JWT token has expired")
                .body(new CustomSuccessResponse<>(ErrorCodes.UNAUTHORISED.getCode(), 
                    List.of(ErrorCodes.UNAUTHORISED.getCode())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<CustomSuccessResponse<Integer>> handle(ConstraintViolationException e) {
        List<Integer> violationCodes = e.getConstraintViolations().stream()
                .map(objectError -> ErrorCodes.getCodeByMessage(objectError.getMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(new CustomSuccessResponse<>(violationCodes.get(0), violationCodes));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<CustomSuccessResponse<Integer>> handle(MethodArgumentNotValidException e) {
        List<Integer> codes = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> ErrorCodes.getCodeByMessage(objectError.getDefaultMessage()))
                .toList();
        return ResponseEntity.badRequest()
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(new CustomSuccessResponse<>(codes.get(0), codes));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<CustomSuccessResponse<Object>> handle(HttpMessageNotReadableException e) {
        CustomSuccessResponse<Object> response = new CustomSuccessResponse<>(Collections.singletonList(e.getMessage()));
        response.setStatusCode(ErrorCodes.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode());
        response.setSuccess(true);
        return ResponseEntity.badRequest()
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(response);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<BaseSuccessResponse> handleMultipartException(MultipartException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(new BaseSuccessResponse());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseSuccessResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        BaseSuccessResponse response = new BaseSuccessResponse();
        response.setSuccess(false);
        response.setStatusCode(ErrorCodes.getCodeByMessage(e.getMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(response);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<BaseSuccessResponse> handleFileNotFoundException(FileNotFoundException e) {
        BaseSuccessResponse response = new BaseSuccessResponse();
        response.setSuccess(false);
        response.setStatusCode(ErrorCodes.getCodeByMessage(e.getMessage()));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(ConstantFields.HEADER_ERROR_MSG, e.getMessage())
                .body(response);
    }
}
