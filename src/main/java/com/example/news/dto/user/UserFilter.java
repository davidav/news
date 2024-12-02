package com.example.news.dto.user;

import com.example.news.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@UserFilterValid
public class UserFilter {

    private Integer pageSize;
    private Integer pageNumber;
    private String firstName;
    private String secondName;
    private Instant createBefore;
    private Instant updateBefore;
    private Long userId;

}
