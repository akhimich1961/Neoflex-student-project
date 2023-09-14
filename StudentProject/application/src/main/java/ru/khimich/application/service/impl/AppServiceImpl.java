package ru.khimich.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khimich.application.dto.LoanApplicationRequestDTO;
import ru.khimich.application.dto.LoanOfferDTO;
import ru.khimich.application.service.AppService;
import ru.khimich.application.service.FeignDealApplication;
import ru.khimich.application.service.FeignDealOffer;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppServiceImpl implements AppService {

    private final FeignDealApplication feignDealApplication;
    private final FeignDealOffer feignDealOffer;

    @Override
    public List<LoanOfferDTO> application(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        List<LoanOfferDTO> result = feignDealApplication.application(loanApplicationRequestDTO);
        log.info("Credit offers request is sent to deal application using Feign Client with input loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        return result;
    }

    @Override
    public void offer(LoanOfferDTO loanOfferDTO) {

        feignDealOffer.offer(loanOfferDTO);
        log.info("Application update request is sent to deal application using Feign Client with input loanApplicationRequestDTO: {}",
                loanOfferDTO);
    }
}
