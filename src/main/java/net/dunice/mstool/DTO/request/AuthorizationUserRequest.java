package net.dunice.mstool.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import net.dunice.mstool.constants.ValidationConstants;


@Data
public class AuthorizationUserRequest {

    @Size(min = 3, max = 100, message = ValidationConstants.EMAIL_SIZE_NOT_VALID)
    @NotBlank(message = ValidationConstants.USER_EMAIL_NOT_NULL)
    @Email(message = ValidationConstants.USER_EMAIL_NOT_VALID)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_VALID)
    private String password;
}
