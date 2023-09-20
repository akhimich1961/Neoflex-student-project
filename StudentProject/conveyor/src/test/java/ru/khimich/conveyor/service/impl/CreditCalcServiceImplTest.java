package ru.khimich.conveyor.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.khimich.conveyor.dto.CreditDTO;
import ru.khimich.conveyor.dto.EmploymentDTO;
import ru.khimich.conveyor.dto.ScoringDataDTO;
import ru.khimich.conveyor.dto.enums.EmploymentPosition;
import ru.khimich.conveyor.dto.enums.EmploymentStatus;
import ru.khimich.conveyor.dto.enums.Gender;
import ru.khimich.conveyor.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditCalcServiceImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCalc() {
        ScoringDataDTO loanRequest = new ScoringDataDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDTO.setSalary(new BigDecimal(100000));
        employmentDTO.setPosition(EmploymentPosition.WORKER);
        employmentDTO.setWorkExperienceTotal(12);
        employmentDTO.setWorkExperienceCurrent(3);
        loanRequest.setAmount(new BigDecimal(100000));
        loanRequest.setTerm(18);
        loanRequest.setBirthdate(LocalDate.of(1961, 12, 24));
        loanRequest.setGender(Gender.MALE);
        loanRequest.setMaritalStatus(MaritalStatus.SINGLE);
        loanRequest.setDependentAmount(0);
        loanRequest.setEmployment(employmentDTO);
        loanRequest.setIsInsuranceEnabled(true);
        loanRequest.setIsSalaryClient(true);
        CreditCalcServiceImpl creditCalcService = new CreditCalcServiceImpl();

        CreditDTO creditDTO = creditCalcService.getCalc(loanRequest);

        assertNotEquals(null, creditDTO);

        //Credit parameters correction testing
        BigDecimal rate = creditDTO.getRate();
        employmentDTO.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED); // Self_employed correction testing
        loanRequest.setEmployment(employmentDTO);
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) > 0);

        rate = creditDTO.getRate();
        employmentDTO.setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER); // Business owner correction testing
        loanRequest.setEmployment(employmentDTO);
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) > 0);

        rate = creditDTO.getRate();
        employmentDTO.setPosition(EmploymentPosition.MID_MANAGER); // Middle manager discount testing
        loanRequest.setEmployment(employmentDTO);
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) < 0);

        rate = creditDTO.getRate();
        employmentDTO.setPosition(EmploymentPosition.TOP_MANAGER); // Top manager discount testing
        loanRequest.setEmployment(employmentDTO);
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) < 0);

        rate = creditDTO.getRate();
        loanRequest.setMaritalStatus(MaritalStatus.MARRIED); // Married status discount testing
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) < 0);

        rate = creditDTO.getRate();
        loanRequest.setMaritalStatus(MaritalStatus.DIVORCED); // Divorced status correction testing
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) > 0);

        rate = creditDTO.getRate();
        loanRequest.setDependentAmount(2); // Dependent amount correction testing
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) > 0);

        rate = creditDTO.getRate();
        loanRequest.setGender(Gender.NON_BINARY); // Non_binary correction testing
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) > 0);

        rate = creditDTO.getRate();
        loanRequest.setGender(Gender.FEMALE); // Woman average age discount testing
        loanRequest.setBirthdate(LocalDate.of(1980, 12, 24));
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertTrue(creditDTO.getRate().compareTo(rate) < 0);

        rate = creditDTO.getRate();
        loanRequest.setGender(Gender.MALE); // Man average age discount testing
        loanRequest.setBirthdate(LocalDate.of(1980, 12, 24));
        creditDTO = creditCalcService.getCalc(loanRequest);
        assertEquals(rate, creditDTO.getRate());
    }
}