package ru.khimich.deal.service;

import ru.khimich.deal.dto.FinishRegistrationRequestDTO;
import ru.khimich.deal.dto.LoanApplicationRequestDTO;
import ru.khimich.deal.dto.LoanOfferDTO;

import java.util.List;

public interface DealService {
    List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void offer(LoanOfferDTO loanOfferDTO);

    void calculate(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long applicationId);
}
