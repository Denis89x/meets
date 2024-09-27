package dev.lebenkov.meets.storage.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotBlank(message = "Username must be not empty")
    @Size(min = 3, max = 16, message = "Username must be between 3 and 16 characters long")
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9_]*$", message = "Username may contain only letters, numbers, underscores, and dashes")
    private String username;

    @NotBlank(message = "Password must be not empty")
    @Size(min = 3, max = 16, message = "Password must be between 3 and 16 characters long")
    private String password;

    @Email(message = "Email must be correct")
    @Size(max = 30, message = "Email must be up to 30 characters long")
    private String email;
}