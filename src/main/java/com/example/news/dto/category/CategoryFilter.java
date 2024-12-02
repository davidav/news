package com.example.news.dto.category;

import com.example.news.validation.CategoryFilterValid;
import com.example.news.validation.UserFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@CategoryFilterValid
public class CategoryFilter {

    private Integer pageSize;
    private Integer pageNumber;
    private String name;
    private Instant createBefore;
    private Instant updateBefore;
    private Long id;

}
