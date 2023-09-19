package ru.khimich.deal.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khimich.deal.dto.*;
import ru.khimich.deal.entity.Application;
import ru.khimich.deal.entity.Client;
import ru.khimich.deal.entity.Credit;
import ru.khimich.deal.entity.enums.ApplicationStatus;
import ru.khimich.deal.entity.enums.ChangeType;
import ru.khimich.deal.entity.enums.CreditStatus;
import ru.khimich.deal.model.Passport;
import ru.khimich.deal.repository.AppRepository;
import ru.khimich.deal.repository.ClientRepository;
import ru.khimich.deal.repository.CreditRepository;
import ru.khimich.deal.service.DealService;
import ru.khimich.deal.service.FeignConveyorCalc;
import ru.khimich.deal.service.FeignConveyorOffers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements DealService {
    private final ClientRepository clientRepository;
    private final AppRepository appRepository;
    private final CreditRepository creditRepository;
    private final FeignConveyorOffers feignConveyorOffers;
    private final FeignConveyorCalc feignConveyorCalc;

    @Transactional
    @Override
    public List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO)
    {
        Application app = addApplication(loanApplicationRequestDTO);

        //RestTemplate restTemplate = new RestTemplate();

        List<LoanOfferDTO> result = feignConveyorOffers.getOffers(loanApplicationRequestDTO);
        log.info("Credit offers request is sent to Credit Conveyor using Feign Client with input loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        for (LoanOfferDTO loanOfferDTO : result) {
            loanOfferDTO.setApplicationId(app.getApplication_id());
        }
        log.info("Application ID is updated in credit offers. ID value " + app.getApplication_id() +
        " is received from application saved into database before.");

        return result;
    }

    @Transactional
    @Override
    public void offer(LoanOfferDTO loanOfferDTO) {

        Application app = appRepository.getReferenceById(loanOfferDTO.getApplicationId());
        log.info("Application data are read from database: {}", app);
        app.setStatus(ApplicationStatus.APPROVED);
        app.setApplied_offer(loanOfferDTO);
        app.setStatus_history(updateStatusHistory(app, ApplicationStatus.APPROVED,
                LocalDateTime.now(), ChangeType.AUTOMATIC));
        appRepository.saveAndFlush(app);
        log.info("Application data are updated and saved into database after credit offer approving: {}", app);
    }

    @Transactional
    @Override
    public void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {

        ScoringDataDTO scoringDataDTO = getScoringData(finishRegistrationRequestDTO, applicationId);

        CreditDTO creditDTO = feignConveyorCalc.getCalc(scoringDataDTO);
        log.info("Scoring request is sent to Credit Conveyor using Feign Client with input scoringDataDTO: {}",
                scoringDataDTO);
        Credit credit = addCredit(creditDTO);
        Application app = updateApplication(credit, applicationId);
    }


    private Application addApplication(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        // Add client into database
        Client client = getClient(loanApplicationRequestDTO);
        clientRepository.saveAndFlush(client);
        log.info("Client data are are added and saved into database: {}", client);

        // Add application into database
        Application app = new Application();
        app.setClient_id(client);
        app.setStatus(ApplicationStatus.PREAPPROVAL);
        app.setCreation_date(LocalDateTime.now());
        app.setStatus_history(updateStatusHistory(app, ApplicationStatus.PREAPPROVAL,
                LocalDateTime.now(), ChangeType.AUTOMATIC));
        appRepository.saveAndFlush(app);
        log.info("Application data are added and saved into database: {}", app);
        return app;
    }

    private Client getClient(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        Passport passport = new Passport();
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());

        Client client = new Client();
        client.setLast_name(loanApplicationRequestDTO.getLastName());
        client.setFirst_name(loanApplicationRequestDTO.getFirstName());
        client.setMiddle_name(loanApplicationRequestDTO.getMiddleName());
        client.setBirth_date(loanApplicationRequestDTO.getBirthdate());
        client.setEmail(loanApplicationRequestDTO.getEmail());
        client.setPassport(passport);
        return client;
    }

    private ScoringDataDTO getScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId) {

        Application app = appRepository.getReferenceById(applicationId);
        log.info("Application data are read from database: {}", app);

        Client client = updateClient(finishRegistrationRequestDTO, app);

        return getScoringDataDTO(app, client);
    }

    private Client updateClient(FinishRegistrationRequestDTO finishReg, Application app) {

        Client client = clientRepository.getReferenceById(app.getClient_id().getClient_id());
        log.info("Client data are read from database: {}", client);

        client.setGender(finishReg.getGender());
        client.setMarital_status(finishReg.getMaritalStatus());
        client.setDependent_amount(finishReg.getDependentAmount());
        client.setEmployment(finishReg.getEmployment());
        client.setAccount(finishReg.getAccount());
        client.getPassport().setIssueDate(finishReg.getPassportIssueDate());
        client.getPassport().setIssueBranch(finishReg.getPassportIssueBranch());

        clientRepository.saveAndFlush(client); //Save client to database
        log.info("Client data are updated and saved into database before credit calculation: {}", client);
        return client;
    }

    private ScoringDataDTO getScoringDataDTO(Application app, Client client) {
        ScoringDataDTO scoring = new ScoringDataDTO();
        scoring.setAmount(app.getApplied_offer().getRequestedAmount());
        scoring.setTerm(app.getApplied_offer().getTerm());
        scoring.setFirstName(client.getFirst_name());
        scoring.setLastName(client.getLast_name());
        scoring.setMiddleName(client.getMiddle_name());
        scoring.setGender(client.getGender());
        scoring.setBirthdate(client.getBirth_date());
        scoring.setPassportSeries(client.getPassport().getSeries());
        scoring.setPassportNumber(client.getPassport().getNumber());
        scoring.setPassportIssueDate(client.getPassport().getIssueDate());
        scoring.setPassportIssueBranch(client.getPassport().getIssueBranch());
        scoring.setMaritalStatus(client.getMarital_status());
        scoring.setDependentAmount(client.getDependent_amount());
        scoring.setEmployment(client.getEmployment());
        scoring.setAccount(client.getAccount());
        scoring.setIsInsuranceEnabled(app.getApplied_offer().getIsInsuranceEnabled());
        scoring.setIsSalaryClient(app.getApplied_offer().getIsSalaryClient());
        return scoring;
    }

    private Credit addCredit(CreditDTO creditDTO) {

        if(creditDTO == null) return null;  // Credit refusing

        Credit credit = new Credit();
        credit.setAmount(creditDTO.getAmount());
        credit.setTerm(creditDTO.getTerm());
        credit.setMonthly_payment(creditDTO.getMonthlyPayment());
        credit.setRate(creditDTO.getRate());
        credit.setPsk(creditDTO.getPsk());
        credit.setPayment_schedule(creditDTO.getPaymentSchedule());
        credit.setInsurance_enable(creditDTO.getIsInsuranceEnabled());
        credit.setSalary_client(creditDTO.getIsSalaryClient());
        credit.setCredit_status(CreditStatus.CALCULATED);

        creditRepository.saveAndFlush(credit);
        log.info("Credit data are saved into database: {}", credit);
        return credit;
    }

    private Application updateApplication(Credit credit, Long applicationId) {

        Application app = appRepository.getReferenceById(applicationId);
        log.info("Application data are read from database: {}", app);

        if(credit != null) {
            app.setCredit_id(credit);
            app.setStatus(ApplicationStatus.CC_APPROVED);
            app.setStatus_history(updateStatusHistory(app, ApplicationStatus.CC_APPROVED,
                    LocalDateTime.now(), ChangeType.AUTOMATIC));
        }
        else {
            app.setStatus(ApplicationStatus.CC_DENIED);
            app.setStatus_history(updateStatusHistory(app, ApplicationStatus.CC_DENIED,
                    LocalDateTime.now(), ChangeType.AUTOMATIC));
        }

        appRepository.saveAndFlush(app);
        log.info("Application data are updated and saved into database after credit calculation: {}", app);
        return app;
    }

    private List<ApplicationStatusHistoryDTO> updateStatusHistory(Application app, ApplicationStatus status,
                                                                  LocalDateTime time,
                                                                  ChangeType changeType) {

        ApplicationStatusHistoryDTO statusHistory = new ApplicationStatusHistoryDTO(status, time, changeType);
        List<ApplicationStatusHistoryDTO> list = app.getStatus_history();
        if(list == null) list = new ArrayList<>();
        list.add(statusHistory);

        return list;
    }
}
