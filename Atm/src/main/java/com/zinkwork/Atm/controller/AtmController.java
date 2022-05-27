package com.zinkwork.Atm.controller;

import com.zinkwork.Atm.exception.AtmException;
import com.zinkwork.Atm.model.Account;
import com.zinkwork.Atm.service.AtmService;
import com.zinkwork.Atm.service.InitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.annotation.Resource;

@Tag(name="atm-rest-controller")
@RestController
@RequestMapping("/atm")
@Validated
@RequiredArgsConstructor
public class AtmController {
	@Resource(name = "AtmService")
	private AtmService atmService;
	
	@Resource(name = "InitService")
	private InitService initService = null;
	
	private HashMap<String, Integer> atmNotes = null;

    @Operation(tags="atm-rest-controller",summary = "getBalance",description = "rest call to return the balance")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/balance/{accountId}/{pin}",produces = APPLICATION_JSON_VALUE)
    public Double getBalance(@PathVariable("accountId") int accountId, @PathVariable("pin") int pin)
    		throws SQLException, ClassNotFoundException{
    	if(atmNotes == null) {
    		atmNotes = initService.initialiseATM();
    	}
    	
    	Account account = null;
        try {
            account = atmService.getAccount(accountId);
        } catch (NoSuchElementException ex){
            throw new AtmException("Existing account ID not provided");
        }
        
        if(pin != account.getPin()) {
        	throw new AtmException("PIN is incorrect.");
        }

        return account.getBalance();
    }
    
    @GetMapping(path = "/max-withdrawal-amount/{accountId}/{pin}",produces = APPLICATION_JSON_VALUE)
    public Double getMaxWithdrawalAmount(@PathVariable("accountId") int accountId, @PathVariable("pin") int pin) 
    		throws SQLException, ClassNotFoundException{
    	if(atmNotes == null) {
    		atmNotes = initService.initialiseATM();
    	}
    	
    	Account account = null;
        try {
            account = atmService.getAccount(accountId);
        } catch (NoSuchElementException ex){
            throw new AtmException("Existing account ID not provided");
        }
        
        if(pin != account.getPin()) {
        	throw new AtmException("PIN is incorrect.");
        }

        return (account.getBalance() + account.getOverdraft());
    }
    
    @Operation(tags="atm-rest-controller",summary = "getWithdrawal",description = "rest call to return the cash withdrawal")
    @ApiResponses(value = @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(mediaType = APPLICATION_JSON_VALUE)))

    @GetMapping(path = "/withdraw/{accountId}/{pin}",produces = APPLICATION_JSON_VALUE)
    public HashMap<String, String> getWithdrawal(@PathVariable("accountId") int accountId, @PathVariable("pin") int pin,
    		@RequestParam(required = true) double amount) throws SQLException, ClassNotFoundException {
    	if(atmNotes == null) {
    		atmNotes = initService.initialiseATM();
    	}
    	
    	Account account = null;
        try {
            account = atmService.getAccount(accountId);
        } catch (NoSuchElementException ex){
            throw new AtmException("Existing account ID not provided");
        }
        
        if(pin != account.getPin()) {
        	throw new AtmException("PIN is incorrect.");
        }
        
        Double withdrawalAmount = getMaxWithdrawalAmount(accountId, pin);
        
        if(amount <= withdrawalAmount) {
        	if(amount <= account.getBalance()) {
        		account.setBalance(account.getBalance() - amount);
        	} else {
        		amount =- account.getBalance();
        		account.setBalance(0);
        		account.setOverdraft(account.getOverdraft() - amount);
        	}
        	
        	atmService.updateAccount(account);
        	
        	int numberOfFives = 0;
        	int numberOfTens = 0;
        	int numberOfTwenties = 0;
        	int numberOfFifties = 0;
        	
        	while(amount >= 5) {
        		if(amount >= 50) {
        			amount -= 50;
        			numberOfFifties++;
        		} else if(amount >= 20) {
        			amount -= 20;
        			numberOfTwenties++;
        		} else if(amount >= 10) {
        			amount -= 10;
        			numberOfTens++;
        		} else if(amount >= 5) {
        			amount -= 5;
        			numberOfFives++;
        		}
        	}
        	
        	if((atmNotes.get("50") - numberOfFifties <= 0) || (atmNotes.get("50") - numberOfFifties <= 0) ||
        			(atmNotes.get("50") - numberOfFifties <= 0) || (atmNotes.get("50") - numberOfFifties <= 0)) {
        		throw new AtmException("Not enough money in ATM to complete transaction.");
        	}
        	
        	atmNotes.put("50", atmNotes.get("50") - numberOfFifties);
        	atmNotes.put("20", atmNotes.get("20") - numberOfTwenties);
        	atmNotes.put("10", atmNotes.get("10") - numberOfTens);
        	atmNotes.put("5", atmNotes.get("5") - numberOfFives);
        	
        	System.out.println(atmNotes);
        	
        	HashMap<String, String> notesReceived = new HashMap<String, String>();
        	notesReceived.put("€50", Integer.toString(numberOfFifties));
        	notesReceived.put("€20", Integer.toString(numberOfTwenties));
        	notesReceived.put("€10", Integer.toString(numberOfTens));
        	notesReceived.put("€5", Integer.toString(numberOfFives));
        	notesReceived.put("Balance", Double.toString(account.getBalance()));
        	
        	return notesReceived;
        } else {
        	throw new AtmException("Not enough money in account.");
        }
    }
}
