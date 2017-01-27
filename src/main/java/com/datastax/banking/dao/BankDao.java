package com.datastax.banking.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.TransactionByAccount;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
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

	private static String keyspaceName = "ob";

	private static String transactionsTable = keyspaceName + ".transactions";
	private static String transactionsByAccountTable = keyspaceName + ".transactions_by_account";
		
	private static final String GET_TRANSACTIONS_BY_ACCOUNT_ID = "select * from " + transactionsByAccountTable + " where account_id = ?";
	
	private static final String GET_TRANSACTIONS_BY_TIMES = "select * from " + transactionsByAccountTable
			+ " where account_id = ? and completed >= ? and completed < ?";
	private static final String GET_TRANSACTIONS_SINCE = "select * from " + transactionsByAccountTable
			+ " where account_id = ? and completed >= ?";
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	private PreparedStatement getTransactionByAccountId;
	private PreparedStatement getTransactionBetweenTimes;
	private PreparedStatement getTransactionSinceTime;
	private PreparedStatement getTransactionById;
	private PreparedStatement getCustomerAccounts;
	
	private AtomicLong count = new AtomicLong(0);
	private Mapper<Transaction> transactionMapper;
	private Mapper<TransactionByAccount> transactionByAccountMapper;
	

	public BankDao(String[] contactPoints) {

		Cluster cluster = Cluster.builder().addContactPoints(contactPoints).build();

		this.session = cluster.connect();

		this.getTransactionByAccountId = session.prepare(GET_TRANSACTIONS_BY_ACCOUNT_ID);
		this.getTransactionBetweenTimes = session.prepare(GET_TRANSACTIONS_BY_TIMES);
		this.getTransactionSinceTime = session.prepare(GET_TRANSACTIONS_SINCE);
		
		transactionMapper = new MappingManager(this.session).mapper(Transaction.class);
		transactionByAccountMapper = new MappingManager(this.session).mapper(TransactionByAccount.class);
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

		if (total % 10000 == 0) {
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
}
