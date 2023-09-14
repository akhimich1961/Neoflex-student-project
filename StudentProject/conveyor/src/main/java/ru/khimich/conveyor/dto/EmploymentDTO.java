package ru.khimich.conveyor.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.khimich.conveyor.dto.enums.EmploymentPosition;
import ru.khimich.conveyor.dto.enums.EmploymentStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDTO {
    @NotNull(message = "Employment status field can not be null")
    private EmploymentStatus employmentStatus;
    @NotNull(message = "Employer INN field can not be null")
    @NotBlank(message = "Employer INN field can not be empty")
    private String employerINN;
    @NotNull(message = "Salary field can not be null")
    @Min(0)
    private BigDecimal salary;
    @NotNull(message = "Employment position field can not be null")
    private EmploymentPosition position;
    @NotNull(message = "Work experience total field can not be null")
    @Min(0)
    private Integer workExperienceTotal;
    @NotNull(message = "Work experience current field can not be null")
    @Min(0)
    private Integer workExperienceCurrent;
}
