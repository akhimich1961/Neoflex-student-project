package ru.khimich.conveyor.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khimich.conveyor.dto.LoanApplicationRequestDTO;
import ru.khimich.conveyor.dto.LoanOfferDTO;
import ru.khimich.conveyor.service.CreditCalcService;
import ru.khimich.conveyor.service.CreditOffersService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditOffersServiceImpl implements CreditOffersService {
    private final CreditCalcService creditCalcService;

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanRequest) {

        List<LoanOfferDTO> offersList = new ArrayList<>();
        offersList.add(createOffer(loanRequest, false, false));
        offersList.add(createOffer(loanRequest, false, true));
        offersList.add(createOffer(loanRequest, true, false));
        offersList.add(createOffer(loanRequest, true, true));

        return offersList;
    }

    private LoanOfferDTO createOffer(LoanApplicationRequestDTO loanRequest,
                                     Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient) {

        BigDecimal rate = creditCalcService.getRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = creditCalcService.totalAmountCount(loanRequest.getAmount(), isInsuranceEnabled);

        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        loanOfferDTO.setRequestedAmount(loanRequest.getAmount());
        loanOfferDTO.setTerm(loanRequest.getTerm());
        loanOfferDTO.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOfferDTO.setIsSalaryClient(isSalaryClient);
        loanOfferDTO.setRate(rate);
        loanOfferDTO.setTotalAmount(totalAmount);
        loanOfferDTO.setMonthlyPayment(creditCalcService.monthPayCount(loanOfferDTO.getTotalAmount(),
                loanOfferDTO.getTerm(), loanOfferDTO.getRate()));

        return loanOfferDTO;
    }
}
