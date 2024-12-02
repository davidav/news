package com.example.news.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PagesFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PagesFilterValid {
    String message() default "Поля пагинации должны быть указаны";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
