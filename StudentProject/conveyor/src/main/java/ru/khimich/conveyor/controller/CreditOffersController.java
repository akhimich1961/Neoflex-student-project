package ru.khimich.conveyor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.khimich.conveyor.dto.LoanApplicationRequestDTO;
import ru.khimich.conveyor.dto.LoanOfferDTO;
import ru.khimich.conveyor.service.CreditOffersService;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
//@Log4j2
@Slf4j
@Tag(name = "Conveyor API")
public class CreditOffersController {
    private final CreditOffersService creditOffersService;

    @PostMapping("/offers")
    @Operation(summary = "Application preliminary analysis and credit conditions suggestion",
            description = "Returns list with 4 LoanOfferDTO based on input LoanApplicationRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
    })
    public ResponseEntity<List<LoanOfferDTO>> getOffers(@RequestBody @Parameter(description =
            "Input parameters to calculate loan conditions") LoanApplicationRequestDTO loanRequest) {

        log.info("Request is received to the controller to calculate loan conditions based on loanApplicationRequestDTO: {}",
                loanRequest);

        return new ResponseEntity<>(creditOffersService.getOffers(loanRequest), HttpStatus.OK);
    }
}
