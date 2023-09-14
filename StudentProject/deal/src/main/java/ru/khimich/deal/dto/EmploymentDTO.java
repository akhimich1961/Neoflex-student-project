package ru.khimich.deal.dto;//package ru.khimich.conveyor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.khimich.deal.entity.enums.EmploymentPosition;
import ru.khimich.deal.entity.enums.EmploymentStatus;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDTO {
//    @JsonSerialize(using = EnumSerializer.class)
//    @JsonDeserialize(using = EnumDeserializer.class)
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
//    @JsonSerialize(using = EnumSerializer.class)
//    @JsonDeserialize(using = EnumDeserializer.class)
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
