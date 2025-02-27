package net.dunice.mstool.service.impl;

import net.dunice.mstool.DTO.request.ProfileDto;
import net.dunice.mstool.constants.ErrorCodes;
import net.dunice.mstool.entity.UserEntity;
import net.dunice.mstool.errors.CustomException;
import net.dunice.mstool.repository.UserRepository;
import net.dunice.mstool.security.JwtService;
import net.dunice.mstool.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    @Override
    public ProfileDto getProfile(UUID id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCodes.CODE_NOT_NULL));
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(user.getId());
        profileDto.setUsername(user.getUsername());
        profileDto.setRole(user.getRole());
        profileDto.setAvatarUrl(user.getAvatarUrl());
        profileDto.setBio(user.getBio());
        return profileDto;
    }

    @Override
    public ProfileDto updateProfile(ProfileDto profileDto, String token) {
        UUID userId = jwtService.extractUserId(token);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCodes.CODE_NOT_NULL));

        user.setUsername(profileDto.getUsername());
        user.setAvatarUrl(profileDto.getAvatarUrl());
        user.setBio(profileDto.getBio());

        UserEntity updatedUser = userRepository.save(user);

        ProfileDto updatedProfileDto = new ProfileDto();
        updatedProfileDto.setId(updatedUser.getId());
        updatedProfileDto.setUsername(updatedUser.getUsername());
        updatedProfileDto.setRole(updatedUser.getRole());
        updatedProfileDto.setAvatarUrl(updatedUser.getAvatarUrl());
        updatedProfileDto.setBio(updatedUser.getBio());

        return updatedProfileDto;
    }

    @Override
    public ProfileDto getProfileByToken(String token) {
        System.out.println("Токен в сервисе: " + token);
        UUID userId = jwtService.extractUserId(token); // Токен уже должен быть чистым
        System.out.println("Извлечённый userId: " + userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCodes.USER_NOT_FOUND));
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(user.getId());
        profileDto.setUsername(user.getUsername());
        profileDto.setRole(user.getRole());
        profileDto.setAvatarUrl(user.getAvatarUrl());
        profileDto.setBio(user.getBio());
        return profileDto;
    }
}
