package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "banks")
public class Banks{

	@PartitionKey
	@Column(name = "banks")
	private String bank = "banks";
	
	@ClusteringColumn
	@Column(name = "bank_id")
	private String bankId;
	private String json;
	
	public Banks() {
	}
	
	public Banks(String bankId, String json) {
		super();
		this.bankId = bankId;
		this.json = json;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	
}
