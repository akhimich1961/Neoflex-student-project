package ru.khimich.conveyor.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khimich.conveyor.dto.LoanApplicationRequestDTO;
import ru.khimich.conveyor.dto.LoanOfferDTO;
import ru.khimich.conveyor.service.CreditCalcService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditOffersServiceImplTest {




    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

@Mock
private CreditCalcService creditCalcService;

@InjectMocks
private CreditOffersServiceImpl creditOffersService;

    @Test
    void getOffers() {
        LoanApplicationRequestDTO loanRequest = new LoanApplicationRequestDTO();
        BigDecimal amount = new BigDecimal(100000);
        int term = 18;
        loanRequest.setAmount(amount);
        loanRequest.setTerm(term);

        BigDecimal totalAmountInsFalse = new BigDecimal(100050);
        BigDecimal totalAmountInsTrue = new BigDecimal(100020);
        when(creditCalcService.totalAmountCount(any(), any())).thenReturn(totalAmountInsFalse, totalAmountInsTrue,
                totalAmountInsFalse, totalAmountInsTrue);

        List<LoanOfferDTO> offersList = creditOffersService.getOffers(loanRequest);

        assertEquals(4, offersList.size());
        assertEquals(amount, offersList.get(0).getRequestedAmount());
        assertEquals(term, offersList.get(0).getTerm());
        verify(creditCalcService, times(4)).monthPayCount(any(), any(), any());

        assertEquals(totalAmountInsFalse, offersList.get(0).getTotalAmount());
        assertEquals(totalAmountInsTrue, offersList.get(1).getTotalAmount());
        assertEquals(totalAmountInsFalse, offersList.get(2).getTotalAmount());
        assertEquals(totalAmountInsTrue, offersList.get(3).getTotalAmount());
    }
}