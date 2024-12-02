package com.example.news.dto.user;

import com.example.news.model.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank(message = "First name must be")
    @Size(min = 3, max = 30, message = "First name must be from {min} to {max} symbols")
    private String firstName;

    @NotBlank(message = "Second name must be")
    @Size(min = 3, max = 30, message = "Second name must be from {min} to {max} symbols")
    private String secondName;

    private String login;

    private String password;

    private RoleType roleType;
}
