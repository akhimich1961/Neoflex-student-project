package ru.khimich.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khimich.application.dto.LoanApplicationRequestDTO;
import ru.khimich.application.dto.LoanOfferDTO;
import ru.khimich.application.service.AppService;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Application API")
public class AppController {
    private final AppService appService;

    @PostMapping("")
    @Operation(summary = "Application preliminary analysis and credit conditions suggestion",
            description = "Return list with 4 LoanOfferDTO based on input LoanApplicationRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
    })
    public ResponseEntity<List<LoanOfferDTO>> application(@Valid @RequestBody @Parameter(description =
            "Input parameters to calculate loan conditions") LoanApplicationRequestDTO loanRequest) {

        log.info("Request is received to the controller to calculate loan conditions based on loanApplicationRequestDTO: {}",
                loanRequest);

        return new ResponseEntity<>(appService.application(loanRequest), HttpStatus.OK);
    }

    @PutMapping("/offer")
    @Operation(summary = "Application update. Save credit offer and application history into database",
            description = "Receive credit offer and send it to deal application using Feign Client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Application is not found in database")
    })
    public void offer(@RequestBody @Parameter(description =
            "Input parameters representing credit offer") LoanOfferDTO loanOfferDTO) {

        log.info("Request is received to the controller to save into database application history and credit offer based on loanOfferDTO:" +
                " {} for application having id: {}", loanOfferDTO, loanOfferDTO.getApplicationId());

        appService.offer(loanOfferDTO);
    }
}
