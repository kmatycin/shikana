package net.dunice.mstool.controller;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.AuthorizationUserRequest;
import net.dunice.mstool.DTO.request.RegisterUserRequest;
import net.dunice.mstool.DTO.response.LoginUserResponse;
import net.dunice.mstool.DTO.response.common.CustomSuccessResponse;
import net.dunice.mstool.service.impl.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> register(@RequestBody RegisterUserRequest user) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.register(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<CustomSuccessResponse<LoginUserResponse>> login(@RequestBody AuthorizationUserRequest request) {
        return ResponseEntity.ok(new CustomSuccessResponse<>(authService.login(request)));
    }
}
