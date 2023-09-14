package ru.khimich.application.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.khimich.application.dto.LoanApplicationRequestDTO;
import ru.khimich.application.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(value = "FeignDealApplication", url = "http://127.0.0.1:8083/deal/application")
public interface FeignDealApplication {
        @RequestMapping(method = RequestMethod.POST)
        List<LoanOfferDTO> application(@RequestBody LoanApplicationRequestDTO loanRequest);
}