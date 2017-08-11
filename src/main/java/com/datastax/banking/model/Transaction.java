	package com.datastax.banking.model;

import java.util.Date;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "transaction")
public class Transaction{

	@PartitionKey
	@Column(name = "transaction_id")
	private String transactionId;
	
	@Column(name = "account_id")
	private String id;	
	private Date completed;
	private String json;

	private String counterparty;
	private double value;
	
	public Transaction() {
	}
	
	public Transaction(String transactionId, Date completed, String accountId, String counterpartyName, double value, String json) {
		this.id = accountId; 
		this.completed = completed;
		this.setCounterparty(counterpartyName);
		this.setValue(value);
		this.transactionId = transactionId;
		this.json = json;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}


	public String getCounterparty() {
		return counterparty;
	}


	public void setCounterparty(String counterparty) {
		this.counterparty = counterparty;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}	
}
