package ru.khimich.conveyor.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.khimich.conveyor.dto.enums.Gender;
import ru.khimich.conveyor.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDataDTO {
@NotNull(message = "Amount field can not be null")
@Min(10000)
private BigDecimal amount;
@NotNull(message = "Term field can not be null")
@Min(6)
private Integer term;
@NotNull(message = "FirstName field can not be null")
@Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid firstName")
private String firstName;
@NotNull(message = "FastName field can not be null")
@Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid lastName")
private String lastName;
@Pattern(regexp = "[A-Z][a-z]{1,29}|[А-ЯЁ][а-яё]{1,29}", message="Invalid middleName")
private String middleName;
@NotNull(message = "Gender field can not be null")
private Gender gender;
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
@NotNull(message = "Passport issue date field can not be null")
@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
@Past(message = "Passport issue date can not be before current date")
private LocalDate passportIssueDate;
@NotNull(message = "Passport issue branch field can not be null")
@NotBlank(message = "Passport issue branch field can not be empty")
private String passportIssueBranch;
@NotNull(message = "Marital status field can not be null")
private MaritalStatus maritalStatus;
@NotNull(message = "Dependent amount field can not be null")
@Min(0)
private Integer dependentAmount;
@NotNull(message = "Employment field can not be null")
private EmploymentDTO employment;
@NotNull(message = "Account field can not be null")
@NotBlank(message = "Account field can not be empty")
private String account;
@NotNull(message = "Is insurance enabled field can not be null")
private Boolean isInsuranceEnabled;
@NotNull(message = "Is salary client field can not be null")
private Boolean isSalaryClient;
}
