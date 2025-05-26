package com.system.card.auth;

import com.system.card.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    private Role role;

}
