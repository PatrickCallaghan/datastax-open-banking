package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "openb", name = "products")
public class Products{

	@PartitionKey
	@Column(name = "bank_id")
	private String id = "bankId";
	
	@ClusteringColumn
	private String code;
	private String json;
	
	public Products() {
	}

	public Products(String id, String code, String json) {
		super();
		this.id = id;
		this.code = code;
		this.json = json;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
