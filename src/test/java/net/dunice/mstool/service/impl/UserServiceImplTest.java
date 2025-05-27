package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.response.UserProfileResponse;
import net.dunice.mstool.DTO.response.UserSearchResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.UserProfileMapper;
import net.dunice.mstool.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private UserProfileResponse userProfileResponse;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_AVATAR_URL = "https://example.com/avatar.jpg";
    private static final String TEST_ROLE = "USER";
    private static final String TEST_BIO = "Test Bio";

    @BeforeEach
    void setUp() {
        // Setup user entity
        userEntity = new UserEntity();
        userEntity.setUsername(TEST_USERNAME);
        userEntity.setRole(TEST_ROLE);
        userEntity.setAvatarUrl(TEST_AVATAR_URL);
        userEntity.setBio(TEST_BIO);

        // Setup user profile response
        userProfileResponse = new UserProfileResponse();
        userProfileResponse.setUsername(TEST_USERNAME);
        userProfileResponse.setRole(TEST_ROLE);
        userProfileResponse.setAvatarUrl(TEST_AVATAR_URL);
        userProfileResponse.setBio(TEST_BIO);
    }

    @Test
    void searchUsers_Success() {
        // Arrange
        String query = "test";
        when(userRepository.findByUsernameContainingIgnoreCase(query)).thenReturn(List.of(userEntity));

        // Act
        List<UserSearchResponse> result = userService.searchUsers(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        UserSearchResponse searchResponse = result.get(0);
        assertEquals(TEST_USERNAME, searchResponse.getUsername());
        assertEquals(TEST_AVATAR_URL, searchResponse.getAvatarUrl());
        verify(userRepository).findByUsernameContainingIgnoreCase(query);
    }

    @Test
    void searchUsers_EmptyQuery() {
        // Act
        List<UserSearchResponse> result = userService.searchUsers(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, never()).findByUsernameContainingIgnoreCase(any());
    }

    @Test
    void searchUsers_BlankQuery() {
        // Act
        List<UserSearchResponse> result = userService.searchUsers("   ");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, never()).findByUsernameContainingIgnoreCase(any());
    }

    @Test
    void searchUsers_UserWithoutAvatar() {
        // Arrange
        String query = "test";
        UserEntity userWithoutAvatar = new UserEntity();
        userWithoutAvatar.setUsername(TEST_USERNAME);
        userWithoutAvatar.setAvatarUrl(null);
        when(userRepository.findByUsernameContainingIgnoreCase(query)).thenReturn(List.of(userWithoutAvatar));

        // Act
        List<UserSearchResponse> result = userService.searchUsers(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        UserSearchResponse searchResponse = result.get(0);
        assertEquals(TEST_USERNAME, searchResponse.getUsername());
        assertEquals("", searchResponse.getAvatarUrl());
        verify(userRepository).findByUsernameContainingIgnoreCase(query);
    }

    @Test
    void getUserProfile_Success() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));
        when(userProfileMapper.toProfileResponse(userEntity)).thenReturn(userProfileResponse);

        // Act
        UserProfileResponse result = userService.getUserProfile(TEST_USERNAME);

        // Assert
        assertNotNull(result);
        assertEquals(userProfileResponse, result);
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userProfileMapper).toProfileResponse(userEntity);
    }

    @Test
    void getUserProfile_NotFound() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.getUserProfile(TEST_USERNAME));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userProfileMapper, never()).toProfileResponse(any());
    }

    @Test
    void updateUserProfile_Success() {
        // Arrange
        String newBio = "New Bio";
        UserProfileResponse updatedProfile = new UserProfileResponse();
        updatedProfile.setUsername(TEST_USERNAME);
        updatedProfile.setRole(TEST_ROLE);
        updatedProfile.setAvatarUrl(TEST_AVATAR_URL);
        updatedProfile.setBio(newBio);

        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername(TEST_USERNAME);
        updatedUser.setRole(TEST_ROLE);
        updatedUser.setAvatarUrl(TEST_AVATAR_URL);
        updatedUser.setBio(newBio);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(updatedUser);
        when(userProfileMapper.toProfileResponse(updatedUser)).thenReturn(updatedProfile);

        // Act
        UserProfileResponse result = userService.updateUserProfile(TEST_USERNAME, updatedProfile);

        // Assert
        assertNotNull(result);
        assertEquals(updatedProfile, result);
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userProfileMapper).updateEntityFromProfile(updatedProfile, userEntity);
        verify(userRepository).save(userEntity);
        verify(userProfileMapper).toProfileResponse(updatedUser);
    }

    @Test
    void updateUserProfile_NotFound() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class,
                () -> userService.updateUserProfile(TEST_USERNAME, userProfileResponse));
        assertEquals(ErrorCodes.USER_NOT_FOUND, exception.getErrorCodes());
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(userProfileMapper, never()).updateEntityFromProfile(any(), any());
        verify(userRepository, never()).save(any());
        verify(userProfileMapper, never()).toProfileResponse(any());
    }
} 