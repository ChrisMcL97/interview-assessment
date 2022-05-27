package com.zinkwork.Atm.service;

import java.sql.SQLException;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zinkwork.Atm.dao.AccountDao;
import com.zinkwork.Atm.model.Account;

@Service(value = "InitService")
public class InitService {
	
	@Resource(name = "AccountDao")
	private AccountDao accountDao;
	
	public HashMap<String, Integer> initialiseATM() throws SQLException {
		HashMap<String, Integer> atmNotes = new HashMap<String, Integer>();
		atmNotes.put("50", 10);
		atmNotes.put("20", 30);
		atmNotes.put("10", 30);
		atmNotes.put("5", 20);
		
		System.out.println(atmNotes);
		
		//createAccount(123456789, 1234, 800, 200);
		//createAccount(987654321, 4321, 1230, 150);
		
		return atmNotes;
	}
	
	private long createAccount(int accountNumber, int pin, double balance, double overdraft) throws SQLException, ClassNotFoundException {
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setPin(pin);
		account.setBalance(balance);
		account.setOverdraft(overdraft);
		
		return accountDao.createAccount(account);
	}
}
