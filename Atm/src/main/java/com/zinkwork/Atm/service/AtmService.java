package com.zinkwork.Atm.service;

import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zinkwork.Atm.dao.AccountDao;
import com.zinkwork.Atm.model.Account;

@Service(value = "AtmService")
public class AtmService {
	
	@Resource(name = "AccountDao")
	private AccountDao accountDao;
	
	public int createAccount(Account account) throws SQLException, ClassNotFoundException {
		return accountDao.createAccount(account);
	}
	
    public Account getAccount(Integer accountId) throws SQLException, ClassNotFoundException {
        return accountDao.getAccount(accountId);
    }
    
    public int updateAccount(Account account) throws SQLException, ClassNotFoundException {
    	return accountDao.updateAccount(account);
    }
}
