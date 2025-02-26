package net.dunice.mstool.DTO.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.dunice.mstool.constants.ValidationConstants;

@Data
public class RegisterUserRequest {

    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @Size(min = 3, max = 25, message = ValidationConstants.USERNAME_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_NAME_HAS_TO_BE_PRESENT)
    private String username;

    @NotBlank(message = ValidationConstants.USER_ROLE_NULL)
    @Size(min = 3, max = 25, message = ValidationConstants.ROLE_SIZE_NOT_VALID)
    private String role;

    @NotBlank(message = ValidationConstants.USER_PASSWORD_NULL)
    private String password;
}
