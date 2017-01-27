package com.datastax.banking.model;

import java.util.Map;
import java.util.Set;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "bank", name = "customer")
public class Customer {
	@PartitionKey
	@Column(name="customer_id")
	private String customerId;
	
	private String first;
	private String last;
	private Map<String,String> externalSystemIds;
	private Map<String,String> social;
	private Map<String,String> emails;
	private Map<String,String> phones;
	private Set<String> accounts;
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public Map<String, String> getExternalSystemIds() {
		return externalSystemIds;
	}
	public void setExternalSystemIds(Map<String, String> externalSystemIds) {
		this.externalSystemIds = externalSystemIds;
	}
	public Map<String, String> getSocial() {
		return social;
	}
	public void setSocial(Map<String, String> social) {
		this.social = social;
	}
	public Map<String, String> getEmails() {
		return emails;
	}
	public void setEmails(Map<String, String> emails) {
		this.emails = emails;
	}
	public Map<String, String> getPhones() {
		return phones;
	}
	public void setPhones(Map<String, String> phones) {
		this.phones = phones;
	}
	public Set<String> getAccounts() {
		return accounts;
	}
	public void setAccounts(Set<String> accounts) {
		this.accounts = accounts;
	}

}
