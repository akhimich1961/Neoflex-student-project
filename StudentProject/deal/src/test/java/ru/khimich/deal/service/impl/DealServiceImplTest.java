package ru.khimich.deal.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khimich.deal.dto.EmploymentDTO;
import ru.khimich.deal.dto.FinishRegistrationRequestDTO;
import ru.khimich.deal.dto.LoanApplicationRequestDTO;
import ru.khimich.deal.dto.LoanOfferDTO;
import ru.khimich.deal.entity.Application;
import ru.khimich.deal.entity.Client;
import ru.khimich.deal.entity.enums.*;
import ru.khimich.deal.model.Passport;
import ru.khimich.deal.repository.AppRepository;
import ru.khimich.deal.repository.ClientRepository;
import ru.khimich.deal.repository.CreditRepository;
import ru.khimich.deal.service.FeignConveyorCalc;
import ru.khimich.deal.service.FeignConveyorOffers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

@Mock
private ClientRepository clientRepository;
@Mock
private AppRepository appRepository;
@Mock
private  CreditRepository creditRepository;
@Mock
private  FeignConveyorOffers feignConveyorOffers;
@Mock
private  FeignConveyorCalc feignConveyorCalc;

@InjectMocks
private DealServiceImpl dealService;

    @Test
    void application() {
        LoanApplicationRequestDTO loanRequest = new LoanApplicationRequestDTO();

        List<LoanOfferDTO> offersList = dealService.application(loanRequest);

        assertEquals(0, offersList.size());
        verify(appRepository, times(0)).getReferenceById(any());
        verify(clientRepository, times(0)).getReferenceById(any());
        verify(appRepository, times(1)).saveAndFlush(any());
        verify(clientRepository, times(1)).saveAndFlush(any());
        verify(creditRepository, times(0)).saveAndFlush(any());
        verify(feignConveyorOffers, times(1)).getOffers(any());
        verify(feignConveyorCalc, times(0)).getCalc(any());
    }

    @Test
    void offer() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setApplicationId((long)1);
        loanOfferDTO.setRequestedAmount(new BigDecimal(100000));
        loanOfferDTO.setTotalAmount(new BigDecimal(102000));
        loanOfferDTO.setTerm(18);
        loanOfferDTO.setRate(new BigDecimal(15));
        loanOfferDTO.setMonthlyPayment(new BigDecimal(6300));
        loanOfferDTO.setIsInsuranceEnabled(true);
        loanOfferDTO.setIsSalaryClient(true);

        Application app = new Application();
        app.setApplication_id(loanOfferDTO.getApplicationId());
        when(appRepository.getReferenceById(any())).thenReturn(app);

        dealService.offer(loanOfferDTO);

        assertEquals(app.getApplication_id(), loanOfferDTO.getApplicationId());
        assertEquals(app.getStatus(), ApplicationStatus.APPROVED);
        assertNotEquals(app.getApplied_offer(), null);
        assertEquals(app.getApplied_offer().getApplicationId(), loanOfferDTO.getApplicationId());
        assertEquals(app.getApplied_offer().getRequestedAmount(), loanOfferDTO.getRequestedAmount());
        assertEquals(app.getApplied_offer().getTotalAmount(), loanOfferDTO.getTotalAmount());
        assertEquals(app.getApplied_offer().getTerm(), loanOfferDTO.getTerm());
        assertEquals(app.getApplied_offer().getRate(), loanOfferDTO.getRate());
        assertEquals(app.getApplied_offer().getMonthlyPayment(), loanOfferDTO.getMonthlyPayment());
        assertEquals(app.getApplied_offer().getIsInsuranceEnabled(), loanOfferDTO.getIsInsuranceEnabled());
        assertEquals(app.getApplied_offer().getIsSalaryClient(), loanOfferDTO.getIsSalaryClient());
        assertNotEquals(app.getStatus_history(), null);
        assertEquals(app.getStatus_history().size(), 1);
        assertEquals(app.getStatus_history().get(0).getStatus(), ApplicationStatus.APPROVED);
        assertEquals(app.getStatus_history().get(0).getChangeType(), ChangeType.AUTOMATIC);
        assertNotEquals(app.getStatus_history().get(0).getTime(), null);

        verify(appRepository, times(1)).getReferenceById(any());
        verify(clientRepository, times(0)).getReferenceById(any());
        verify(appRepository, times(1)).saveAndFlush(any());
        verify(clientRepository, times(0)).saveAndFlush(any());
        verify(creditRepository, times(0)).saveAndFlush(any());
        verify(feignConveyorOffers, times(0)).getOffers(any());
        verify(feignConveyorCalc, times(0)).getCalc(any());
    }

    @Test
    void calculate() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();
        finishRegistrationRequestDTO.setGender(Gender.MALE);
        finishRegistrationRequestDTO.setMaritalStatus(MaritalStatus.SINGLE);
        finishRegistrationRequestDTO.setDependentAmount(0);
        finishRegistrationRequestDTO.setPassportIssueDate(LocalDate.of(2007, 1, 27));
        finishRegistrationRequestDTO.setPassportIssueBranch("NN");
        EmploymentDTO employmentDTO = new EmploymentDTO();
        employmentDTO.setEmploymentStatus(EmploymentStatus.EMPLOYED);
        employmentDTO.setEmployerINN("123456");
        employmentDTO.setSalary(new BigDecimal(100000));
        employmentDTO.setPosition(EmploymentPosition.WORKER);
        employmentDTO.setWorkExperienceTotal(12);
        employmentDTO.setWorkExperienceCurrent(3);
        finishRegistrationRequestDTO.setEmployment(employmentDTO);
        finishRegistrationRequestDTO.setAccount("testAccount");
        Long applicationId = (long)1;

        Application app = new Application();
        app.setApplication_id(applicationId);
        Client client = new Client();
        client.setClient_id((long)1);
        Passport passport = new Passport();
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        passport.setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
        client.setPassport(passport);
        app.setClient_id(client);
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setTotalAmount(new BigDecimal(102000));
        loanOfferDTO.setTerm(18);
        loanOfferDTO.setIsInsuranceEnabled(true);
        loanOfferDTO.setIsSalaryClient(true);
        app.setApplied_offer(loanOfferDTO);
        when(appRepository.getReferenceById(any())).thenReturn(app);
        when(clientRepository.getReferenceById(any())).thenReturn(client);

        dealService.calculate(finishRegistrationRequestDTO, applicationId);

        assertEquals(app.getClient_id().getGender(), finishRegistrationRequestDTO.getGender());
        assertEquals(app.getClient_id().getMarital_status(), finishRegistrationRequestDTO.getMaritalStatus());
        assertEquals(app.getClient_id().getDependent_amount(), finishRegistrationRequestDTO.getDependentAmount());
        assertEquals(app.getClient_id().getEmployment(), finishRegistrationRequestDTO.getEmployment());
        assertEquals(app.getClient_id().getAccount(), finishRegistrationRequestDTO.getAccount());
        assertNotEquals(app.getStatus(), null);
        assertEquals(app.getStatus_history().size(), 1);
        assertEquals(app.getStatus(), app.getStatus_history().get(0).getStatus());
        assertEquals(app.getStatus_history().get(0).getChangeType(), ChangeType.AUTOMATIC);

        verify(appRepository, times(2)).getReferenceById(any());
        verify(clientRepository, times(1)).getReferenceById(any());
        verify(appRepository, times(1)).saveAndFlush(any());
        verify(clientRepository, times(1)).saveAndFlush(any());
        verify(feignConveyorOffers, times(0)).getOffers(any());
        verify(feignConveyorCalc, times(1)).getCalc(any());
    }
}