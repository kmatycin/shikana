package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.request.AuthorizationUserRequest;
import net.dunice.mstool.DTO.request.RegisterUserRequest;
import net.dunice.mstool.DTO.response.LoginUserResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.UserMappers;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final UserMappers userMappers;

    @Override
    public LoginUserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCodes.USER_ALREADY_EXISTS);
        }
        UserEntity user = userMappers.registerUserRequestToUserEntity(request);
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
        LoginUserResponse loginUser = userMappers.toLoginUserResponse(user);
        loginUser.setToken(jwtService.generateToken(user.getEmail()));
        return loginUser;
    }

    @Override
    public LoginUserResponse login(AuthorizationUserRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new CustomException(ErrorCodes.USER_NOT_FOUND));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCodes.PASSWORD_NOT_VALID);
        }
        LoginUserResponse loginUser = userMappers.toLoginUserResponse(user);
        loginUser.setToken(jwtService.generateToken(user.getEmail()));
        return loginUser;
    }
}
