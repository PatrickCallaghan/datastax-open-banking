package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "ob", name = "transactions")
public class Transaction{

	@PartitionKey
	@Column(name = "transaction_id")
	private String transactionId;	
	private String json;
	
	public Transaction() {
	}
	
	public Transaction(String transactionId, String json) {
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
	
}
