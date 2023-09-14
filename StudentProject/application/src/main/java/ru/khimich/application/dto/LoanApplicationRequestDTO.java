package ru.khimich.application.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationRequestDTO {
    @NotNull(message = "Amount field can not be null")
    @Min(10000)
    private BigDecimal amount;
    @NotNull(message = "Term field can not be null")
    @Min(6)
    private Integer term;
    @NotNull(message = "FirstName field can not be null")
    @Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid firstName")
    private String firstName;
    @NotNull(message = "LastName field can not be null")
    @Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid lastName")
    private String lastName;
    @Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid middleName")
    private String middleName;
    @NotNull(message = "Email field can not be null")
    @Email(message = "Invalid email address")
    private String email;
    @NotNull(message = "Birthdate field can not be null")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Past(message = "Birthdate can not be before current date")
    @BirthDate
    private LocalDate birthdate;
    @NotNull(message = "PassportSeries field can not be null")
    @Pattern(regexp = "[0-9]{4}", message="Invalid passportSeries, should be 4 digits")
    private String passportSeries;
    @NotNull(message = "PassportNumber field can not be null")
    @Pattern(regexp = "[0-9]{6}", message="Invalid passportNumber, should be 6 digits")
    private String passportNumber;
}
