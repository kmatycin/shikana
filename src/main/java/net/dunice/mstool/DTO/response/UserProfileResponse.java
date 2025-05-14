package net.dunice.mstool.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String username;
    private String nickname;
    private String avatarUrl;
    private String bio;
    private String role;
} 