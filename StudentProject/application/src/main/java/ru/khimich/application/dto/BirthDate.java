package ru.khimich.application.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
    String message() default "Age can not be less than 18";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
