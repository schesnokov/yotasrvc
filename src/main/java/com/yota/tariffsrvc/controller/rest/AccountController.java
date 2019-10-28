package com.yota.tariffsrvc.controller.rest;

import com.yota.tariffsrvc.exception.YotaCustomException;
import com.yota.tariffsrvc.model.dto.PackagesDTO;
import com.yota.tariffsrvc.model.entity.Account;
import com.yota.tariffsrvc.service.api.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Chesnokov Sergei
 */
@RestController
@RequestMapping("/api/accounts")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Info successfully fetched"),
        @ApiResponse(code = 500, message = "Internal server error"),
})
public class AccountController {

    private static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/{accId}")
    @ApiOperation(value = "Find acc by id", response = Object.class)
    @ResponseBody
    public ResponseEntity<Account> getAccById(@PathVariable(name = "accId") long accId) {
        return new ResponseEntity<>(accountService.findById(accId), HttpStatus.OK);
    }

    @PostMapping(value = "/packages/{phoneNumber}")
    @ApiOperation(value = "Add packages to account. expirationDate fields should be set to null. " +
            "Leave amount and daysCount of one of the packages equals 0 if only another is needed",
            response = Object.class)
    @ResponseBody
    public ResponseEntity<PackagesDTO> addMinutesAndGigsPackagesToAcc(@PathVariable(name = "phoneNumber") String phoneNumber,
                                                                      @RequestBody PackagesDTO packages) {
        try {
            return new ResponseEntity<>(accountService.addPackages(phoneNumber, packages.getMinutesPackage(), packages.getGigabytesPackage()), HttpStatus.OK);
        } catch (IllegalArgumentException | YotaCustomException e) {
            LOGGER.error(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/changestatus/{phoneNumber}")
    @ApiOperation(value = "Change account status", response = Object.class)
    @ResponseBody
    public ResponseEntity<Boolean> changeAccStatus(@PathVariable(name = "phoneNumber") String phoneNumber) {
        try {
            return new ResponseEntity<>(accountService.changeStatus(phoneNumber), HttpStatus.OK);
        } catch (YotaCustomException e) {
            LOGGER.error(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/minutes/spend/{phoneNumber}/{minutesAmount}")
    @ApiOperation(value = "Spend minutes from account", response = Object.class)
    @ResponseBody
    public ResponseEntity<Integer> spendMinutes(@PathVariable(name = "phoneNumber") String phoneNumber,
                                                @PathVariable(name = "minutesAmount") int minutesAmount) {
        try {
            return new ResponseEntity<>(accountService.spendMinutes(phoneNumber, minutesAmount), HttpStatus.OK);
        } catch (IllegalArgumentException | YotaCustomException e) {
            LOGGER.error(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PostMapping(value = "/gigabytes/spend/{phoneNumber}/{gigabytesAmount}")
    @ApiOperation(value = "Spend gigabytes from account", response = Object.class)
    @ResponseBody
    public ResponseEntity<Integer> spendGigabytes(@PathVariable(name = "phoneNumber") String phoneNumber,
                                                  @PathVariable(name = "gigabytesAmount") int gigabytesAmount) {
        try {
            return new ResponseEntity<>(accountService.spendGigabytes(phoneNumber, gigabytesAmount), HttpStatus.OK);
        } catch (IllegalArgumentException | YotaCustomException e) {
            LOGGER.error(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/available/{phoneNumber}")
    @ApiOperation(value = "Get available minutes and gigabytes from account", response = Object.class)
    @ResponseBody
    public ResponseEntity<PackagesDTO> getAvailable(@PathVariable(name = "phoneNumber") String phoneNumber) {
        try {
            return new ResponseEntity<>(accountService.getAvailableMinsAndGigs(phoneNumber), HttpStatus.OK);
        } catch (IllegalArgumentException | YotaCustomException e) {
            LOGGER.error(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}