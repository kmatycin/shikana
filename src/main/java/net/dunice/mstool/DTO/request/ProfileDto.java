package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfileDto {
    private UUID id;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    private String role;
    private String avatarUrl;
    private String bio;
}
