package ru.khimich.conveyor.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.khimich.conveyor.dto.CreditDTO;
import ru.khimich.conveyor.dto.ScoringDataDTO;
import ru.khimich.conveyor.service.CreditCalcService;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
//@Log4j2
@Slf4j
@Tag(name = "Conveyor API")
public class CreditCalcController {
    private final CreditCalcService creditCalcService;

    @PostMapping("/calculation")
    @Operation(summary = "Credit parameters calculation",
            description = "Credit parameters calculation based on input ScoringDataDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
                })
    public ResponseEntity<CreditDTO> getCalc(@Valid @RequestBody @Parameter(description =
            "Input parameters for credit calculation") ScoringDataDTO loanRequest) {

        log.info("Request is received to the controller to calculate credit parameters based on based on ScoringDataDTO: {}",
                loanRequest);

        return new ResponseEntity<>(creditCalcService.getCalc(loanRequest), HttpStatus.OK);
    }
}
