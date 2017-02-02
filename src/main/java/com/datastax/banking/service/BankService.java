package com.datastax.banking.service;

import java.util.List;

import com.datastax.banking.dao.BankDao;
import com.datastax.banking.model.Accounts;
import com.datastax.banking.model.Atms;
import com.datastax.banking.model.Banks;
import com.datastax.banking.model.Branches;
import com.datastax.banking.model.Products;
import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.TransactionByAccount;
import com.datastax.banking.model.Transactions;
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
	
	public void saveBranches(List<Branches> branches){
		for (Branches branch : branches){
			this.dao.saveBranch(branch);			
		}
	}

	
	public void saveBranch(Branches branch){
		this.dao.saveBranch(branch);			
	}

	public void saveUser(String string, String jsonString) {
		
	}

	public void saveBank(Banks bank) {
		this.dao.saveBank(bank);		
	}
	
	public void saveAtm(Atms atm) {
		this.dao.saveAtm(atm);		
	}
	
	public void saveTransacton(Transactions transaction) {
		this.dao.saveTransaction(transaction);		
	}
	
	public void saveProduct(Products product) {
		this.dao.saveProduct(product);		
	}

	public void saveAccount(Accounts account) {
		this.dao.saveAccount(account);
	}

	public void saveTransactonByAccount(TransactionByAccount transactionByAccount) {
		this.dao.saveTransaction(transactionByAccount);
		
	}
	


	
}
