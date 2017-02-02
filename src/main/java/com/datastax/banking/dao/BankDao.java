package com.datastax.banking.dao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Accounts;
import com.datastax.banking.model.Atms;
import com.datastax.banking.model.Banks;
import com.datastax.banking.model.Branches;
import com.datastax.banking.model.Products;
import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.TransactionByAccount;
import com.datastax.banking.model.Transactions;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;

/**
 * Inserts into 2 tables
 * 
 * @author patrickcallaghan
 *
 */
public class BankDao {

	private static Logger logger = LoggerFactory.getLogger(BankDao.class);
	private Session session;

	private static String keyspaceName = "ob";

	private static String transactionsTable = keyspaceName + ".transactions";
	private static String transactionsByAccountTable = keyspaceName + ".transactions_by_account";
		
	private static final String GET_TRANSACTIONS_BY_ACCOUNT_ID = "select * from " + transactionsByAccountTable + " where account_id = ?";
	
	private static final String GET_TRANSACTIONS_BY_TIMES = "select * from " + transactionsByAccountTable
			+ " where account_id = ? and completed >= ? and completed < ?";
	private static final String GET_TRANSACTIONS_SINCE = "select * from " + transactionsByAccountTable
			+ " where account_id = ? and completed >= ?";
	
	private static final String GET_ALL_PRODUCTS_BY_BANK_ID = "SELECT * FROM openb.products where bank_id = ?";
	private static final String GET_ALL_ACCOUNTS_BY_BANK_ID = "SELECT * FROM openb.accounts where bank_id = ?";
	private static final String GET_ALL_ATMS_BY_BANK_ID = "SELECT * FROM openb.atms where bank_id = ?";
	private static final String GET_ALL_BRANCHES_BY_BANK_ID = "SELECT * FROM openb.branches where bank_id = ?";
	private static final String GET_TRANSACTION_BY_ID = "SELECT * FROM openb.transactions where transaction_id = ?";
	private static final String GET_ALL_BANKS = "SELECT * FROM openb.banks where banks = ?";
 
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	private PreparedStatement getTransactionByAccountId;
	private PreparedStatement getTransactionBetweenTimes;
	private PreparedStatement getTransactionSinceTime;
	private PreparedStatement getTransactionById;
	private PreparedStatement getCustomerAccounts;

	private PreparedStatement getProducts;
	private PreparedStatement getAccounts;
	private PreparedStatement getAtms;
	private PreparedStatement getBranches;
	private PreparedStatement getTransaction;
	private PreparedStatement getBanks;

	
	private AtomicLong count = new AtomicLong(0);
	private Mapper<Transaction> transactionMapper;
	private Mapper<TransactionByAccount> transactionByAccountMapper;
	private Mapper<Transactions> transactionsMapper;
	private Mapper<Banks> banksMapper;
	private Mapper<Atms> atmsMapper;
	private Mapper<Accounts> accountsMapper;
	private Mapper<Products> productsMapper;
	private Mapper<Branches> branchesMapper;

	public BankDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();

		this.session = cluster.connect();

		this.getTransactionByAccountId = session.prepare(GET_TRANSACTIONS_BY_ACCOUNT_ID);
		this.getTransactionBetweenTimes = session.prepare(GET_TRANSACTIONS_BY_TIMES);
		this.getTransactionSinceTime = session.prepare(GET_TRANSACTIONS_SINCE);

		this.getProducts = session.prepare(GET_ALL_PRODUCTS_BY_BANK_ID);
		this.getAccounts = session.prepare(GET_ALL_ACCOUNTS_BY_BANK_ID);
		this.getAtms = session.prepare(GET_ALL_ATMS_BY_BANK_ID);
		this.getBranches = session.prepare(GET_ALL_BRANCHES_BY_BANK_ID);
		this.getTransaction = session.prepare(GET_TRANSACTION_BY_ID);
		this.getBanks = session.prepare(GET_ALL_BANKS);

		
		transactionMapper = new MappingManager(this.session).mapper(Transaction.class);
		transactionByAccountMapper = new MappingManager(this.session).mapper(TransactionByAccount.class);
		
		transactionsMapper = new MappingManager(this.session).mapper(Transactions.class);
		banksMapper = new MappingManager(this.session).mapper(Banks.class);
		atmsMapper = new MappingManager(this.session).mapper(Atms.class);
		productsMapper = new MappingManager(this.session).mapper(Products.class);
		accountsMapper = new MappingManager(this.session).mapper(Accounts.class);
		branchesMapper = new MappingManager(this.session).mapper(Branches.class);
	}
	
	public void saveTransaction(TransactionByAccount transaction) {
		insertTransactionAsync(transaction);		
	}

	public void insertTransactionsAsync(List<TransactionByAccount> transactions) {
		
		for (TransactionByAccount transaction : transactions) {
			transactionMapper.save(new Transaction(transaction.getId(), transaction.getJson()));
			transactionByAccountMapper.save(transaction);

			long total = count.incrementAndGet();
			if (total % 10000 == 0) {
				logger.info("Total transactions processed : " + total);
			}
		}
	}

	public void insertTransactionAsync(TransactionByAccount transaction) {

		transactionMapper.save(new Transaction(transaction.getId(), transaction.getJson()));
		transactionByAccountMapper.save(transaction);
		long total = count.incrementAndGet();

		if (total % 10 == 0) {
			logger.info("Total transactions processed : " + total);
		}
	}

	public List<TransactionByAccount> getTransactions(String accountId) {

		ResultSetFuture rs = this.session.executeAsync(this.getTransactionByAccountId.bind(accountId));
		
		return this.processResultSet(rs.getUninterruptibly(), null);
	}

	public Transaction getTransaction(String transactionId) {
		
		return this.transactionMapper.get(transactionId, Option.consistencyLevel(ConsistencyLevel.ONE));
	}

	public List<TransactionByAccount> getTransactionsSinceTime(String acountNo, DateTime from) {
		ResultSet resultSet = this.session.execute(getTransactionSinceTime.bind(acountNo, from.toDate()));

		return processResultSet(resultSet, null);
	}

	private List<TransactionByAccount> processResultSet(ResultSet resultSet, Set<String> tags) {
		Result<TransactionByAccount> transactions = transactionByAccountMapper.map(resultSet);
		
		return transactions.all();
	}

	public void saveBranch(Branches branch) {
		this.branchesMapper.save(branch);
	}
	
	public void saveAtm(Atms atm) {
		this.atmsMapper.save(atm);
	}

	public void saveBank(Banks bank) {
		this.banksMapper.save(bank);
	}

	public void saveProduct(Products product) {
		this.productsMapper.save(product);
	}

	public void saveAccount(Accounts account) {
		this.accountsMapper.save(account);
	}

	public void saveTransaction(Transactions transaction) {
		this.transactionsMapper.save(transaction);
	}
	
	public List<Branches> getBranchesByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getBranches.bind(bankId));		
		return this.branchesMapper.map(resultSet).all();
	}
	
	public List<Accounts> getAccountsByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getAccounts.bind(bankId));		
		return this.accountsMapper.map(resultSet).all();
	}
	
	public List<Products> getProductsByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getProducts.bind(bankId));		
		return this.productsMapper.map(resultSet).all();
	}
	
//	public List<Atms> getAtmsByBankId(String bankId){
//		ResultSet resultSet = session.execute(this.getAtms.bind(bankId));		
//		return this.atmsMapper.map(resultSet).all();
//	}
	

}
