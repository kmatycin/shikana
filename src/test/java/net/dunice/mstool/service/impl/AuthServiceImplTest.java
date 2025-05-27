package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.request.AuthorizationUserRequest;
import net.dunice.mstool.DTO.request.RegisterUserRequest;
import net.dunice.mstool.DTO.response.LoginUserResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.UserMappers;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserMappers userMappers;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterUserRequest registerRequest;
    private AuthorizationUserRequest loginRequest;
    private UserEntity userEntity;
    private LoginUserResponse loginUserResponse;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        // Setup register request
        registerRequest = new RegisterUserRequest();
        registerRequest.setEmail(TEST_EMAIL);
        registerRequest.setPassword(TEST_PASSWORD);

        // Setup login request
        loginRequest = new AuthorizationUserRequest();
        loginRequest.setEmail(TEST_EMAIL);
        loginRequest.setPassword(TEST_PASSWORD);

        // Setup user entity
        userEntity = new UserEntity();
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setPassword(TEST_PASSWORD);

        // Setup login response
        loginUserResponse = new LoginUserResponse();
        loginUserResponse.setEmail(TEST_EMAIL);
    }

    @Test
    void register_Success() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
        when(userMappers.registerUserRequestToUserEntity(registerRequest)).thenReturn(userEntity);
        when(encoder.encode(TEST_PASSWORD)).thenReturn("encodedPassword");
        when(userMappers.toLoginUserResponse(userEntity)).thenReturn(loginUserResponse);
        when(jwtService.generateToken(TEST_EMAIL)).thenReturn(TEST_TOKEN);

        // Act
        LoginUserResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(TEST_TOKEN, response.getToken());
        verify(userRepository).save(userEntity);
    }

    @Test
    void register_UserAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.register(registerRequest));
        assertEquals(ErrorCodes.USER_ALREADY_EXISTS, exception.getErrorCodes());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_Success() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        when(userMappers.toLoginUserResponse(userEntity)).thenReturn(loginUserResponse);
        when(jwtService.generateToken(TEST_EMAIL)).thenReturn(TEST_TOKEN);

        // Act
        LoginUserResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(TEST_TOKEN, response.getToken());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.login(loginRequest));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
    }

    @Test
    void login_InvalidPassword() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(userEntity));
        when(encoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(false);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> authService.login(loginRequest));
        assertEquals(ErrorCodes.PASSWORD_NOT_VALID, exception.getErrorCodes());
    }
} 