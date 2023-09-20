package ru.khimich.conveyor.service;

import ru.khimich.conveyor.dto.LoanApplicationRequestDTO;
import ru.khimich.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface CreditOffersService
{
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanRequest);
}
