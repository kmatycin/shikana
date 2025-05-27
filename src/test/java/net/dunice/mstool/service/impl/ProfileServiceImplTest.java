package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.request.ProfileDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private UserEntity userEntity;
    private ProfileDto profileDto;
    private UUID userId;
    private String token;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_USERNAME = "Test User";
    private static final String TEST_ROLE = "USER";
    private static final String TEST_AVATAR_URL = "https://example.com/avatar.jpg";
    private static final String TEST_BIO = "Test Bio";
    private static final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        token = TEST_TOKEN;

        // Setup user entity
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setUsername(TEST_USERNAME);
        userEntity.setRole(TEST_ROLE);
        userEntity.setAvatarUrl(TEST_AVATAR_URL);
        userEntity.setBio(TEST_BIO);

        // Setup profile DTO
        profileDto = new ProfileDto();
        profileDto.setId(userId);
        profileDto.setUsername(TEST_USERNAME);
        profileDto.setRole(TEST_ROLE);
        profileDto.setAvatarUrl(TEST_AVATAR_URL);
        profileDto.setBio(TEST_BIO);
    }

    @Test
    void getProfile_Success() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        ProfileDto result = profileService.getProfile(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_ROLE, result.getRole());
        assertEquals(TEST_AVATAR_URL, result.getAvatarUrl());
        assertEquals(TEST_BIO, result.getBio());
        verify(userRepository).findById(userId);
    }

    @Test
    void getProfile_NotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> profileService.getProfile(userId));
        assertEquals(ErrorCodes.CODE_NOT_NULL, exception.getErrorCodes());
        verify(userRepository).findById(userId);
    }

    @Test
    void updateProfile_Success() {
        // Arrange
        String newUsername = "New Username";
        String newAvatarUrl = "https://example.com/new-avatar.jpg";
        String newBio = "New Bio";

        ProfileDto updatedProfileDto = new ProfileDto();
        updatedProfileDto.setUsername(newUsername);
        updatedProfileDto.setAvatarUrl(newAvatarUrl);
        updatedProfileDto.setBio(newBio);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setId(userId);
        updatedUser.setEmail(TEST_EMAIL);
        updatedUser.setUsername(newUsername);
        updatedUser.setRole(TEST_ROLE);
        updatedUser.setAvatarUrl(newAvatarUrl);
        updatedUser.setBio(newBio);

        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);

        // Act
        ProfileDto result = profileService.updateProfile(updatedProfileDto, token);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(newUsername, result.getUsername());
        assertEquals(TEST_ROLE, result.getRole());
        assertEquals(newAvatarUrl, result.getAvatarUrl());
        assertEquals(newBio, result.getBio());
        verify(jwtService).getEmailFromToken(token);
        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void updateProfile_UserNotFound() {
        // Arrange
        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> profileService.updateProfile(profileDto, token));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(jwtService).getEmailFromToken(token);
        verify(userRepository).findByEmail(TEST_EMAIL);
        verify(userRepository, never()).save(any());
    }

    @Test
    void getProfileByToken_Success() {
        // Arrange
        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act
        ProfileDto result = profileService.getProfileByToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(TEST_USERNAME, result.getUsername());
        assertEquals(TEST_ROLE, result.getRole());
        assertEquals(TEST_AVATAR_URL, result.getAvatarUrl());
        assertEquals(TEST_BIO, result.getBio());
        verify(jwtService).getEmailFromToken(token);
        verify(userRepository).findByEmail(TEST_EMAIL);
    }

    @Test
    void getProfileByToken_UserNotFound() {
        // Arrange
        when(jwtService.getEmailFromToken(token)).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> profileService.getProfileByToken(token));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(jwtService).getEmailFromToken(token);
        verify(userRepository).findByEmail(TEST_EMAIL);
    }
} 