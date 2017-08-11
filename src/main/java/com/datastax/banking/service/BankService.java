package com.datastax.banking.service;

import java.util.List;

import com.datastax.banking.dao.BankDao;
import com.datastax.banking.model.Account;
import com.datastax.banking.model.Atm;
import com.datastax.banking.model.Bank;
import com.datastax.banking.model.Branch;
import com.datastax.banking.model.Permission;
import com.datastax.banking.model.Product;
import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.User;
import com.datastax.demo.utils.PropertyHelper;

public class BankService {

	private static String contactPointsStr = PropertyHelper.getProperty("contactPoints", "localhost");
	private static BankService bankService = new BankService();
	private BankDao dao;

	private BankService() {
		dao = new BankDao(contactPointsStr.split(","));
	}

	public static BankService getInstance() {
		return bankService;
	}

	public List<Transaction> getTransactions(String accountId) {

		return dao.getTransactions(accountId);
	}

	public Transaction getTransaction(String transactionId) {

		return dao.getTransaction(transactionId);
	}

	public void saveBranches(List<Branch> branches) {
		for (Branch branch : branches) {
			this.dao.saveBranch(branch);
		}
	}

	public void saveBranch(Branch branch) {
		this.dao.saveBranch(branch);
	}

	public void saveBank(Bank bank) {
		this.dao.saveBank(bank);
	}

	public void saveAtm(Atm atm) {
		this.dao.saveAtm(atm);
	}

	public void saveTransacton(Transaction transaction) {
		this.dao.saveTransaction(transaction);
	}

	public void saveProduct(Product product) {
		this.dao.saveProduct(product);
	}

	public void saveAccount(Account account) {
		this.dao.saveAccount(account);
	}

	public void saveUser(User user) {
		this.dao.saveUser(user);
	}

	public void savePermission(Permission permission) {
		this.dao.savePermission(permission);
	}
}
