package com.example.news.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpsertNewsRequest {

    @NotNull(message = "categoryId must be")
    @Positive(message = "id must be greater than zero")
    private Long categoryId;

    @NotBlank(message = "title must be")
    @Size(min = 3, max = 30, message = "title must be from {min} to {max} symbols")
    private String title;

    @NotBlank(message = "text must be")
    @Size(min = 30, max = 300, message = "text must be from {min} to {max} symbols")
    private String text;

}
