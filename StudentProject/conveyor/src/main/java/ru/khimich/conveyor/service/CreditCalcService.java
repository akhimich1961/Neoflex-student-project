package ru.khimich.conveyor.service;

import ru.khimich.conveyor.dto.CreditDTO;
import ru.khimich.conveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface CreditCalcService {
    CreditDTO getCalc(ScoringDataDTO loanRequest);
    BigDecimal monthPayCount(BigDecimal amount, Integer term, BigDecimal rate);
    BigDecimal totalAmountCount(BigDecimal amount, Boolean isInsuranceEnabled);
    BigDecimal getRate(Boolean isInsuranceEnabled, Boolean isSalaryClient);
}
