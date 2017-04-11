package com.datastax.banking.dao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Account;
import com.datastax.banking.model.Atm;
import com.datastax.banking.model.Bank;
import com.datastax.banking.model.Branch;
import com.datastax.banking.model.Product;
import com.datastax.banking.model.Transaction;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
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

	private static String keyspaceName = "openb";

	private static final String GET_ALL_PRODUCTS_BY_BANK_ID = "SELECT * FROM openb.product where bank_id = ?";
	private static final String GET_ALL_ACCOUNTS_BY_BANK_ID = "SELECT * FROM openb.account where bank_id = ?";
	private static final String GET_ALL_ATMS_BY_BANK_ID = "SELECT * FROM openb.atm where bank_id = ?";
	private static final String GET_ALL_BRANCHES_BY_BANK_ID = "SELECT * FROM openb.branch where bank_id = ?";
	private static final String GET_ALL_BANKS = "SELECT * FROM openb.bank where banks = ?";
 
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	private PreparedStatement getTransactionSinceTime;

	private PreparedStatement getProducts;
	private PreparedStatement getAccounts;
	private PreparedStatement getAtms;
	private PreparedStatement getBranches;
	private PreparedStatement getBanks;

	
	private AtomicLong count = new AtomicLong(0);
	private Mapper<Transaction> transactionMapper;
	private Mapper<Bank> banksMapper;
	private Mapper<Atm> atmsMapper;
	private Mapper<Account> accountsMapper;
	private Mapper<Product> productsMapper;
	private Mapper<Branch> branchesMapper;

	public BankDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();

		this.session = cluster.connect();

		this.getProducts = session.prepare(GET_ALL_PRODUCTS_BY_BANK_ID);
		this.getAccounts = session.prepare(GET_ALL_ACCOUNTS_BY_BANK_ID);
		this.getAtms = session.prepare(GET_ALL_ATMS_BY_BANK_ID);
		this.getBranches = session.prepare(GET_ALL_BRANCHES_BY_BANK_ID);
		this.getBanks = session.prepare(GET_ALL_BANKS);

		
		transactionMapper = new MappingManager(this.session).mapper(Transaction.class);
		banksMapper = new MappingManager(this.session).mapper(Bank.class);
		atmsMapper = new MappingManager(this.session).mapper(Atm.class);
		productsMapper = new MappingManager(this.session).mapper(Product.class);
		accountsMapper = new MappingManager(this.session).mapper(Account.class);
		branchesMapper = new MappingManager(this.session).mapper(Branch.class);
	}
		
	public void saveTransaction(Transaction transaction) {
		insertTransactionAsync(transaction);		
	}

	public void insertTransactionsAsync(List<Transaction> transactions) {
		
		for (Transaction transaction : transactions) {
			transactionMapper.save(transaction);

			long total = count.incrementAndGet();
			if (total % 10000 == 0) {
				logger.info("Total transactions processed : " + total);
			}
		}
	}

	public void insertTransactionAsync(Transaction transaction) {

		transactionMapper.saveAsync(transaction);		
		
		long total = count.incrementAndGet();

		if (total % 10000 == 0) {
			logger.info("Total transactions processed : " + total);
		}
	}

	public List<Transaction> getTransactions(String accountId) {

		String cql = String.format("select * from %s.transaction where solr_query = 'account_id:%s'", keyspaceName, accountId);
		
		ResultSetFuture rs = this.session.executeAsync(cql);
		
		return this.processResultSet(rs.getUninterruptibly(), null);
	}

	public Transaction getTransaction(String transactionId) {
		
		String cql = String.format("select * from %s.transaction where solr_query = 'transaction_id:%s'", keyspaceName, transactionId);
		ResultSet rs = this.session.execute(cql);

		Result<Transaction> map = transactionMapper.map(rs);
		return map.one();
	}

	public List<Transaction> getTransactionsSinceTime(String acountNo, DateTime from) {
		ResultSet resultSet = this.session.execute(getTransactionSinceTime.bind(acountNo, from.toDate()));

		return processResultSet(resultSet, null);
	}

	private List<Transaction> processResultSet(ResultSet resultSet, Set<String> tags) {
		Result<Transaction> transactions = transactionMapper.map(resultSet);
		
		return transactions.all();
	}

	public void saveBranch(Branch branch) {
		this.branchesMapper.save(branch);
	}
	
	public void saveAtm(Atm atm) {
		this.atmsMapper.save(atm);
	}

	public void saveBank(Bank bank) {
		this.banksMapper.save(bank);
	}

	public void saveProduct(Product product) {
		this.productsMapper.save(product);
	}

	public void saveAccount(Account account) {
		this.accountsMapper.save(account);
	}

	public List<Branch> getBranchesByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getBranches.bind(bankId));		
		return this.branchesMapper.map(resultSet).all();
	}
	
	public List<Account> getAccountsByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getAccounts.bind(bankId));		
		return this.accountsMapper.map(resultSet).all();
	}
	
	public List<Product> getProductsByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getProducts.bind(bankId));		
		return this.productsMapper.map(resultSet).all();
	}
	
	public List<Atm> getAtmsByBankId(String bankId){
		ResultSet resultSet = session.execute(this.getAtms.bind(bankId));		
		return this.atmsMapper.map(resultSet).all();
	}
}
