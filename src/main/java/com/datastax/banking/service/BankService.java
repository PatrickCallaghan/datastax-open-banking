package com.datastax.banking.service;

import java.util.List;

import com.datastax.banking.dao.BankDao;
import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.TransactionByAccount;
import com.datastax.demo.utils.PropertyHelper;

public class BankService {

	private static String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
	private static BankService bankService = new BankService();
	private BankDao dao;
	
	private BankService(){
		dao = new BankDao(contactPointsStr.split(","));
	}
	
	public static BankService getInstance(){
		return bankService;		
	}
	
	public List<TransactionByAccount> getTransactions(String accountId) {
		
		return dao.getTransactions(accountId);
	}

	public Transaction getTransaction(String transactionId) {
		
		return dao.getTransaction(transactionId);
	}
}
