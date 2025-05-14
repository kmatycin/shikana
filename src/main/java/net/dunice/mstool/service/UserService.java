package net.dunice.mstool.service;

import net.dunice.mstool.DTO.response.UserProfileResponse;
import net.dunice.mstool.DTO.response.UserSearchResponse;
import java.util.List;

public interface UserService {
    List<UserSearchResponse> searchUsers(String query);
    UserProfileResponse getUserProfile(String username);
    UserProfileResponse updateUserProfile(String username, UserProfileResponse profile);
} 