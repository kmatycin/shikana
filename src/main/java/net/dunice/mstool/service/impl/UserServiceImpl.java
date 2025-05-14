package net.dunice.mstool.service.impl;

import lombok.RequiredArgsConstructor;
import net.dunice.mstool.DTO.response.UserProfileResponse;
import net.dunice.mstool.DTO.response.UserSearchResponse;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.mappers.UserProfileMapper;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserProfileMapper userProfileMapper;

    @Override
    public List<UserSearchResponse> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }
        
        return userRepository.findByUsernameContainingIgnoreCase(query.trim())
                .stream()
                .map(user -> new UserSearchResponse(
                    user.getUsername(),
                    user.getAvatarUrl() != null ? user.getAvatarUrl() : ""
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        return userProfileMapper.toProfileResponse(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(String username, UserProfileResponse profile) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        
        userProfileMapper.updateEntityFromProfile(profile, user);
        UserEntity updatedUser = userRepository.save(user);
        return userProfileMapper.toProfileResponse(updatedUser);
    }
} 