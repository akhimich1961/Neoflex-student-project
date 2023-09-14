package ru.khimich.deal.dto;//package ru.khimich.conveyor.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.khimich.deal.entity.enums.Gender;
import ru.khimich.deal.entity.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoringDataDTO {
private BigDecimal amount;
private Integer term;
private String firstName;
private String lastName;
private String middleName;
private Gender gender;
@JsonSerialize(using = LocalDateSerializer.class)
@JsonDeserialize(using = LocalDateDeserializer.class)
private LocalDate birthdate;
private String passportSeries;
private String passportNumber;
@JsonSerialize(using = LocalDateSerializer.class)
@JsonDeserialize(using = LocalDateDeserializer.class)
private LocalDate passportIssueDate;
private String passportIssueBranch;
private MaritalStatus maritalStatus;
private Integer dependentAmount;
private EmploymentDTO employment;
private String account;
private Boolean isInsuranceEnabled;
private Boolean isSalaryClient;
}
