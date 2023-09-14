package ru.khimich.application.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.khimich.application.dto.LoanOfferDTO;

@FeignClient(value = "FeignDealOffer", url = "http://127.0.0.1:8083/deal/offer")
public interface FeignDealOffer {
    @RequestMapping(method = RequestMethod.PUT)
    public void offer(@RequestBody LoanOfferDTO loanOfferDTO);
}
