package net.dunice.mstool.service;

import net.dunice.mstool.DTO.request.ProfileDto;
import java.util.UUID;

public interface ProfileService {
    ProfileDto getProfile(UUID id);
    ProfileDto updateProfile(ProfileDto profileDto, String token);

    ProfileDto getProfileByToken(String token);
}
