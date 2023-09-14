package ru.khimich.application.service;

import ru.khimich.application.dto.LoanApplicationRequestDTO;
import ru.khimich.application.dto.LoanOfferDTO;

import java.util.List;

public interface AppService {
    List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO);

    void offer(LoanOfferDTO loanOfferDTO);
}
