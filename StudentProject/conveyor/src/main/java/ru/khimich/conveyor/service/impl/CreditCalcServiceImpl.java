package ru.khimich.conveyor.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.khimich.conveyor.dto.CreditDTO;
import ru.khimich.conveyor.dto.PaymentScheduleElement;
import ru.khimich.conveyor.dto.ScoringDataDTO;
import ru.khimich.conveyor.service.CreditCalcService;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static ru.khimich.conveyor.dto.enums.EmploymentPosition.MID_MANAGER;
import static ru.khimich.conveyor.dto.enums.EmploymentPosition.TOP_MANAGER;
import static ru.khimich.conveyor.dto.enums.EmploymentStatus.*;
import static ru.khimich.conveyor.dto.enums.Gender.*;
import static ru.khimich.conveyor.dto.enums.MaritalStatus.DIVORCED;
import static ru.khimich.conveyor.dto.enums.MaritalStatus.MARRIED;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCalcServiceImpl implements CreditCalcService {
    @Value("${bank.rate}")
    private BigDecimal bankRate;

        public CreditDTO getCalc(ScoringDataDTO loanRequest) {

            if(!CreditNotRefused(loanRequest)) return null;

            BigDecimal rate = getRate(loanRequest.getIsInsuranceEnabled(), loanRequest.getIsSalaryClient());
            BigDecimal amount = totalAmountCount(loanRequest.getAmount(), loanRequest.getIsInsuranceEnabled());

            //Rate correction. The correction depends on client parameters.
            rate = rateCorrection(rate, loanRequest);

            // Initialize output credit data
            CreditDTO creditDTO = new CreditDTO();
            creditDTO.setTerm(loanRequest.getTerm());
            creditDTO.setRate(rate);
            creditDTO.setAmount(amount);
            creditDTO.setIsInsuranceEnabled(loanRequest.getIsInsuranceEnabled());
            creditDTO.setIsSalaryClient(loanRequest.getIsSalaryClient());
            creditDTO.setMonthlyPayment(monthPayCount(amount, loanRequest.getTerm(), rate));
            creditDTO.setPsk(creditDTO.getMonthlyPayment().multiply(new BigDecimal(loanRequest.getTerm()), MathContext.DECIMAL128));
            creditDTO.setPaymentSchedule(getPaySchedule(creditDTO));

        return creditDTO;
    }

    private List<PaymentScheduleElement> getPaySchedule(CreditDTO creditDTO) {

        LocalDate currentDate = LocalDate.now();
        BigDecimal monthlyPayment = creditDTO.getMonthlyPayment();
        BigDecimal remainingDebt = creditDTO.getAmount(); // Start value
        BigDecimal monthRate = creditDTO.getRate().divide(new BigDecimal(1200), MathContext.DECIMAL128);

        List<PaymentScheduleElement> list = new ArrayList<>();

        for (int i = 1; i <= creditDTO.getTerm(); i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthRate, MathContext.DECIMAL128);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, MathContext.DECIMAL128);
            if(i != creditDTO.getTerm()) remainingDebt = remainingDebt.subtract(debtPayment, MathContext.DECIMAL128);
            else remainingDebt = new BigDecimal(0);

            list.add(new PaymentScheduleElement(i, currentDate.plusMonths(i), monthlyPayment,
                    interestPayment, debtPayment, remainingDebt));
        }

        return list;
    }

    public BigDecimal monthPayCount(BigDecimal amount, Integer term, BigDecimal rate) {

        if(rate.equals(new BigDecimal(0))) { return amount.divide(new BigDecimal(term), MathContext.DECIMAL128);}

        BigDecimal monthRate = rate.divide(new BigDecimal(1200), MathContext.DECIMAL128);
        BigDecimal one = new BigDecimal(1);
        BigDecimal helpVar = one.add(monthRate, MathContext.DECIMAL128);
        BigDecimal helpVar1 = helpVar.pow(-term, MathContext.DECIMAL128);
        BigDecimal divisor = one.subtract(helpVar1, MathContext.DECIMAL128);
        return amount.multiply(monthRate).divide(divisor, MathContext.DECIMAL128);
    }

    private Boolean CreditNotRefused(ScoringDataDTO loanRequest) {

        int age = Period.between(loanRequest.getBirthdate(), LocalDate.now()).getYears();
        int minAge = 18;
        int maxAge = 70;
        int minTotalExperience = 12;
        int minCurrenExperience = 3;
        BigDecimal salaryCriterion = new BigDecimal(20);

        if(age < minAge || age > maxAge) {
            log.info("Credit request denied: age is less than" + minAge + "or more than" + maxAge);
            return false;
        }
        if(loanRequest.getEmployment().getEmploymentStatus() == UNEMPLOYED) {
            log.info("Credit request denied: Unemployment");
            return false;
        }
        if(loanRequest.getEmployment().getWorkExperienceTotal() < minTotalExperience) {
            log.info("Credit request denied: Work Experience Total is less than" + minTotalExperience);
            return false;
        }
        if(loanRequest.getEmployment().getWorkExperienceCurrent() < minCurrenExperience) {
            log.info("Credit request denied: Work Experience Current is less than" + minCurrenExperience);
            return false;
        }
        if((loanRequest.getEmployment().getSalary().multiply(salaryCriterion, MathContext.DECIMAL128).compareTo(loanRequest.getAmount()) < 0)) {
            log.info("Credit request denied: Low salary, credit amount is more than" + salaryCriterion + "salary");
            return false;
        }

        return true;
    }

    public BigDecimal totalAmountCount(BigDecimal amount, Boolean isInsuranceEnabled) {

        if(!isInsuranceEnabled) return amount;

        BigDecimal insuranceAmount = (new BigDecimal("0.02")).multiply(amount, MathContext.DECIMAL128);
        return amount.add(insuranceAmount, MathContext.DECIMAL128);
    }

    public BigDecimal getRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        BigDecimal baseRate = bankRate;
        if(!isInsuranceEnabled && !isSalaryClient) return baseRate;

        BigDecimal insuranceDiscount = new BigDecimal(3);
        BigDecimal salaryClientDiscount = new BigDecimal(1);
        if(isInsuranceEnabled && !isSalaryClient) return baseRate.subtract(insuranceDiscount, MathContext.DECIMAL128);
        if(!isInsuranceEnabled) return baseRate.subtract(salaryClientDiscount, MathContext.DECIMAL128);
        return baseRate.subtract(insuranceDiscount, MathContext.DECIMAL128).subtract(salaryClientDiscount, MathContext.DECIMAL128);
    }

    private BigDecimal rateCorrection(BigDecimal rate, ScoringDataDTO loanRequest) {

        int age = Period.between(loanRequest.getBirthdate(), LocalDate.now()).getYears();
        BigDecimal selfEmployedCorrection = new BigDecimal(1);
        BigDecimal businessOwnerCorrection = new BigDecimal(3);
        BigDecimal midManagerCorrection = new BigDecimal(2);
        BigDecimal topManagerCorrection = new BigDecimal(4);
        BigDecimal marriedStatusCorrection = new BigDecimal(3);
        BigDecimal divorcedStatusCorrection = new BigDecimal(1);
        BigDecimal dependentAmountCorrection = new BigDecimal(1);
        BigDecimal nonBinaryCorrection = new BigDecimal(3);
        BigDecimal averageAgeCorrection = new BigDecimal(3);
        int lowManAverageAge = 30;
        int upperManAverageAge = 55;
        int lowWomenAverageAge = 35;
        int upperWomenAverageAge = 60;
        int dependentAmountCriterion = 1;

        if(loanRequest.getEmployment().getEmploymentStatus().equals(SELF_EMPLOYED)) {
            rate = rate.add(selfEmployedCorrection, MathContext.DECIMAL128);
            log.info("Self employed rate increasing: " + selfEmployedCorrection);
        }
        if(loanRequest.getEmployment().getEmploymentStatus().equals(BUSINESS_OWNER)) {
            rate = rate.add(businessOwnerCorrection, MathContext.DECIMAL128);
            log.info("Business owner rate increasing: " + businessOwnerCorrection);
        }
        if(loanRequest.getEmployment().getPosition().equals(MID_MANAGER)) {
            rate = rate.subtract(midManagerCorrection, MathContext.DECIMAL128);
            log.info("Middle manager rate discount: " + midManagerCorrection);
        }
        if(loanRequest.getEmployment().getPosition().equals(TOP_MANAGER)) {
            rate = rate.subtract(topManagerCorrection, MathContext.DECIMAL128);
            log.info("Top manager rate discount: " + topManagerCorrection);
        }
        if(loanRequest.getMaritalStatus().equals(MARRIED)) {
            rate = rate.subtract(marriedStatusCorrection, MathContext.DECIMAL128);
            log.info("Married status rate discount: " + marriedStatusCorrection);
        }
        if(loanRequest.getMaritalStatus().equals(DIVORCED)) {
            rate = rate.add(divorcedStatusCorrection, MathContext.DECIMAL128);
            log.info("Divorced status rate increasing: " + divorcedStatusCorrection);
        }
        if(loanRequest.getDependentAmount() > dependentAmountCriterion) {
            rate = rate.add(dependentAmountCorrection, MathContext.DECIMAL128);
            log.info("Dependent amount rate increasing: " + dependentAmountCorrection);
        }
        if(loanRequest.getGender().equals(NON_BINARY)) {
            rate = rate.add(nonBinaryCorrection, MathContext.DECIMAL128);
            log.info("Non binary rate increasing: " + nonBinaryCorrection);
        }
        if(loanRequest.getGender().equals(FEMALE) && age >= lowWomenAverageAge &&
                age < upperWomenAverageAge) {
            rate = rate.subtract(averageAgeCorrection, MathContext.DECIMAL128);
            log.info("Woman average age discount: " + averageAgeCorrection);
        }
        if(loanRequest.getGender().equals(MALE) && age >= lowManAverageAge &&
                age < upperManAverageAge) {
            rate = rate.subtract(averageAgeCorrection, MathContext.DECIMAL128);
            log.info("Man average age discount: " + averageAgeCorrection);
        }

        return rate;
    }
}
