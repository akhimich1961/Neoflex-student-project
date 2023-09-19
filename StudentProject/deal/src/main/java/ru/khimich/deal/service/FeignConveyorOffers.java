package ru.khimich.deal.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.khimich.deal.dto.LoanApplicationRequestDTO;
import ru.khimich.deal.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(value = "FeignConveyorOffers", url = "http://127.0.0.1:8081/conveyor/offers")
public interface FeignConveyorOffers {
        @RequestMapping(method = RequestMethod.POST)
        List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO loanRequest);
    }

