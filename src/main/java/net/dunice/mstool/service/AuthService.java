package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.AuthorizationUserRequest;
import net.dunice.mstool.DTO.request.RegisterUserRequest;
import net.dunice.mstool.DTO.response.LoginUserResponse;

public interface AuthService {

    LoginUserResponse register(RegisterUserRequest request);

    LoginUserResponse login(AuthorizationUserRequest request);
}
