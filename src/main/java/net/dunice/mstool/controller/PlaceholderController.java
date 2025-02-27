package net.dunice.mstool.controller;

import net.dunice.mstool.DTO.response.common.BaseSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceholderController {

    @GetMapping("/messenger")
    public ResponseEntity<BaseSuccessResponse> getMessenger() {
        BaseSuccessResponse response = new BaseSuccessResponse();
        response.setStatusCode(0);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/top32")
    public ResponseEntity<BaseSuccessResponse> getTop32() {
        BaseSuccessResponse response = new BaseSuccessResponse();
        response.setStatusCode(0);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
