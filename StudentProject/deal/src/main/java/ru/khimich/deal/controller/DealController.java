package ru.khimich.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khimich.deal.dto.FinishRegistrationRequestDTO;
import ru.khimich.deal.dto.LoanApplicationRequestDTO;
import ru.khimich.deal.dto.LoanOfferDTO;
import ru.khimich.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Deal API")
public class DealController {
    private final DealService dealService;

    @PostMapping("/application")
    @Operation(summary = "Application preliminary analysis and credit conditions suggestion",
            description = "Returns list with 4 LoanOfferDTO based on input LoanApplicationRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
    })
        public ResponseEntity<List<LoanOfferDTO>> application(@RequestBody @Parameter(description =
            "Input parameters to calculate loan conditions") LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Request is received to the controller to calculate loan conditions based on loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        return new ResponseEntity<>(dealService.application(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Application update. Save credit offer and application history into database.",
            description = "Search application in database using LoanOfferDTO and application update," +
                    " LoanOfferDTO saved in \"appliedOffer\" field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Application is not found in database")
    })
    public void offer(@RequestBody @Parameter(description =
            "Input parameters representing credit offer") LoanOfferDTO loanOfferDTO) {
        log.info("Request is received to the controller to save into database application history and credit offer based on loanOfferDTO:" +
                " {} for application having id: {}", loanOfferDTO, loanOfferDTO.getApplicationId());
        dealService.offer(loanOfferDTO);
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Credit parameters calculation",
            description = "ScoringDataDTO structure filling based on input FinishRegistrationRequestDTO and " +
                    "application saved in database before. Then ScoringDataDTO is sent to Credit Conveyor" +
                    " using POST request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Application is not found in database")
    })
        public void calculate(@RequestBody @Parameter(description =
            "Input parameter requested for ScoringDataDTO filling") FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                              @PathVariable Long applicationId) {
        log.info("Request is received to the controller to fill ScoringDataDTO with input data: {} и applicationId: {}",
                finishRegistrationRequestDTO, applicationId);
        dealService.calculate(finishRegistrationRequestDTO, applicationId);
    }
}
