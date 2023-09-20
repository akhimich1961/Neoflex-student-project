package ru.khimich.conveyor.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
}
