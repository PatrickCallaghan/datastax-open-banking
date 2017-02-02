package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "atms")
public class Atms{

	@PartitionKey
	@Column(name = "bank_id")
	private String bank = "bankId";
	
	@ClusteringColumn
	@Column(name = "atm_id")
	private String atmId;
	private String latlon; 
	private String json;
	
	public Atms() {
	}
	
	public Atms(String bank, String atmId, String latlon, String json) {
		super();
		this.bank = bank;
		this.atmId = atmId;
		this.latlon = latlon;
		this.json = json;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAtmId() {
		return atmId;
	}

	public void setAtmId(String atmId) {
		this.atmId = atmId;
	}

	public String getLatlon() {
		return latlon;
	}

	public void setLatlon(String latlon) {
		this.latlon = latlon;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
