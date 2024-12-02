package com.example.news.dto;

import com.example.news.validation.PagesFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PagesFilterValid
public class PagesRequest {

    private Integer pageSize;

    private Integer pageNumber;
}
