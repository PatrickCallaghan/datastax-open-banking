	package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "accounts")
public class Accounts{

	@PartitionKey
	@Column(name = "bank_id")
	private String bankId;
	
	@ClusteringColumn
	@Column(name = "account_id")
	private String accountId;
	private String iban;
	private String number;
	private String json;
	
	public Accounts() {
	}

	public Accounts(String bankId, String accountId, String iban, String number, String json) {
		super();
		this.bankId = bankId;
		this.accountId = accountId;
		this.iban = iban;
		this.number = number;
		this.json = json;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
