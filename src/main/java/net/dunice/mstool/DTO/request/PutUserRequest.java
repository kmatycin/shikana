package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.dunice.mstool.constants.ValidationConstants;

@Data
public class PutUserRequest {
    @NotBlank(message = ValidationConstants.USER_AVATAR_NOT_NULL)
    private String avatar;

    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @Size(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    private String name;

    @NotBlank(message = ValidationConstants.USER_ROLE_NULL)
    @Size(min = 3, max = 25, message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;
}
