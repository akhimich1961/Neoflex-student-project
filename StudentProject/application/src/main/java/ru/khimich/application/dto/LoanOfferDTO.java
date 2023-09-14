package ru.khimich.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanOfferDTO {
    @NotNull(message = "Application Id field can not be null")
    @Min(1)
    private Long applicationId;
    @NotNull(message = "Requested amount field can not be null")
    @Min(10000)
    private BigDecimal requestedAmount;
    @NotNull(message = "Total amount field can not be null")
    private BigDecimal totalAmount;
    @NotNull(message = "Term field can not be null")
    @Min(6)
    private Integer term;
    @NotNull(message = "Monthly payment field can not be null")
    private BigDecimal monthlyPayment;
    @NotNull(message = "Rate field can not be null")
    private BigDecimal rate;
    @NotNull(message = "Insurance enabled field can not be null")
    private Boolean isInsuranceEnabled;
    @NotNull(message = "Salary client field can not be null")
    private Boolean isSalaryClient;
}
