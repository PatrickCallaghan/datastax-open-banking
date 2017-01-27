package com.datastax.banking.model;

import java.util.Date;

import org.codehaus.jackson.io.JsonStringEncoder;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Typical Transaction example
 *
 {
    "id":"ccda1c15-0e39-4f89-a46b-9fc989546c7e",
    "this_account":{
      "id":"05237266-b334-4704-a087-5b460a2ecf04",
      "bank":"psd201-bank-x--uk"
    },
    "counterparty":{
      "name":"British Gas"
    },
    "details":{
      "type":"10219",
      "description":"Gas/Elec",
      "posted":"2015-09-01T00:00:00.000Z",
      "completed":"2015-09-01T00:00:00.000Z",
      "new_balance":"6004.22",
      "value":"-114.55"
    }
  }
 */

@Table(keyspace = "ob", name = "transactions_by_account")
public class TransactionByAccount {

	@PartitionKey
	@Column(name = "account_id")
	private String id;

	@ClusteringColumn(0)
	private Date completed;

	@ClusteringColumn(1)
	@Column(name = "transaction_id")
	private String transactionId;

	private String json;

	public TransactionByAccount(){
		
	}
	
	public TransactionByAccount(String id, Date completed, String transactionId, String json) {
		super();
		this.id = id;
		this.completed = completed;
		this.transactionId = transactionId;
		this.json = json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCompleted() {
		return completed;
	}

	public void setCompleted(Date completed) {
		this.completed = completed;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getJson() {
		return JsonStringEncoder.getInstance().quoteAsString(json).toString();
	}

	public void setJson(String json) {
		this.json = json;
	}
}
