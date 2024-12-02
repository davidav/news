package com.example.news.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpsertCommentRequest {

    @NotNull(message = "newsId должен быть заполнен")
    @Positive(message = "newsId больше нуля")
    private Long newsId;

    @NotBlank(message = "comment должен быть")
    @Size(min = 3, max = 300, message = "comment должен быть от {min} до {max} символов")
    private String comment;

}
