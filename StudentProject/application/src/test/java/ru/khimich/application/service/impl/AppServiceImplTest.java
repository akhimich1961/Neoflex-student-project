package ru.khimich.application.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khimich.application.dto.LoanApplicationRequestDTO;
import ru.khimich.application.dto.LoanOfferDTO;
import ru.khimich.application.service.FeignDealApplication;
import ru.khimich.application.service.FeignDealOffer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AppServiceImplTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

@Mock
private FeignDealApplication feignDealApplication;
@Mock
private FeignDealOffer feignDealOffer;

@InjectMocks
private AppServiceImpl appService;

    @Test
    void application() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        List<LoanOfferDTO> offersList = appService.application(loanApplicationRequestDTO);

        assertEquals(0, offersList.size());
        verify(feignDealApplication, times(1)).application(any());
        verify(feignDealOffer, times(0)).offer(any());
    }

    @Test
    void offer() {
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        appService.offer(loanOfferDTO);
        verify(feignDealApplication, times(0)).application(any());
        verify(feignDealOffer, times(1)).offer(any());
    }
}