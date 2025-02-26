package net.dunice.mstool.DTO.response;

import lombok.Data;
import java.util.UUID;

@Data
public class LoginUserResponse {

    private UUID id;

    private String username;

    private String email;

    private String password;

    private String role;

    private String avatarUrl;

    private String bio;

    private String token;
}
