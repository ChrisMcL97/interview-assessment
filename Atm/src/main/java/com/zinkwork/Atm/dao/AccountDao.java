package com.zinkwork.Atm.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.zinkwork.Atm.model.Account;

@Repository("AccountDao")
public class AccountDao {
	
	public int createAccount(Account account) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zinkworks");
		
		String query = "INSERT INTO Account (accountNumber,pin,balance,overdraft)" +
						"VALUES (?, ?, ?, ?)";

		PreparedStatement preparedStatement = con.prepareStatement(query);
		
		preparedStatement.setInt(1, account.getAccountNumber());
		preparedStatement.setInt(2,  account.getPin());
		preparedStatement.setDouble(3,  account.getBalance());
		preparedStatement.setDouble(4,  account.getOverdraft());
		
		return preparedStatement.executeUpdate();
	}
	
	public int updateAccount(Account account) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zinkworks");
		
		String query = "UPDATE ACCOUNT SET pin = ?, balance = ?, overdraft = ? " + 
						"WHERE accountNumber = ?";
		
		PreparedStatement preparedStatement = con.prepareStatement(query);
		
		preparedStatement.setInt(1, account.getPin());
		preparedStatement.setDouble(2, account.getBalance());
		preparedStatement.setDouble(3, account.getOverdraft());
		preparedStatement.setInt(4, account.getAccountNumber());
		
		return preparedStatement.executeUpdate();
	}
	
	public Account getAccount(Integer accountId) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zinkworks");
		 
        String query = "SELECT * FROM account WHERE accountNumber = ?";
        
        PreparedStatement preparedStatement = con.prepareStatement(query);

        preparedStatement.setInt(1, accountId);
        
        ResultSet rs = preparedStatement.executeQuery();
        
        Account account = new Account();
		account.setAccountNumber(rs.getInt("accountNumber"));
		account.setPin(rs.getInt("pin"));
		account.setBalance(rs.getDouble("balance"));
		account.setOverdraft(rs.getDouble("overdraft"));
		
		return account;
    }
}
