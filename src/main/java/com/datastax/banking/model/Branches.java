package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "branches")
public class Branches{

	@PartitionKey
	@Column(name = "bank_id")
	private String bankId;
	
	@ClusteringColumn
	@Column(name = "branch_id")
	private String branchId;
	private String json;
		
	public Branches() {
	}

	public Branches(String bankId, String branchId, String json) {
		super();
		this.bankId = bankId;
		this.branchId = branchId;
		this.json = json;
	}
	
	public String getBankId() {
		return bankId;
	}
	
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
