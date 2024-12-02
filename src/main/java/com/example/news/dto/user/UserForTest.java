package com.example.news.dto.user;

import com.example.news.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForTest {

    private String login;
    private String password;
    private RoleType role;

}
