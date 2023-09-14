package ru.khimich.deal.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.khimich.deal.dto.CreditDTO;
import ru.khimich.deal.dto.ScoringDataDTO;

@FeignClient(value = "FeignConveyorCalc", url = "http://127.0.0.1:8081/conveyor/calculation")
public interface FeignConveyorCalc {
    @RequestMapping(method = RequestMethod.POST)
    CreditDTO getCalc(@RequestBody ScoringDataDTO loanRequest);
}
