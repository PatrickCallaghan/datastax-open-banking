package com.datastax.banking.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * create table if not exists permissions ( bank_id text, account_id text,
 * user_id text, provider_id text, view_id text, view_json text, PRIMARY KEY
 * ((bank_id, account_id), user_id, provider_id, view_id)
 * 
 * @author patrickcallaghan
 *
 */
@Table(keyspace = "openb", name = "permission")
public class Permission {

	@PartitionKey(0)
	@Column(name = "bank_id")
	private String bankId;

	@PartitionKey(1)
	@Column(name = "account_id")
	private String accountId;

	@ClusteringColumn(0)
	@Column(name = "user_id")
	private String userId;

	@ClusteringColumn(1)
	@Column(name = "provider_id")
	private String providerId;

	@ClusteringColumn(2)
	@Column(name = "view_id")
	private String viewId;

	@Column(name = "view_json")
	private String viewJson;

	public Permission(){}
	
	public Permission(String bankId, String accountId, String userId, String providerId, String viewId,
			String viewJson) {
		super();
		this.bankId = bankId;
		this.accountId = accountId;
		this.userId = userId;
		this.providerId = providerId;
		this.viewId = viewId;
		this.viewJson = viewJson;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getViewJson() {
		return viewJson;
	}

	public void setViewJson(String viewJson) {
		this.viewJson = viewJson;
	}

	@Override
	public String toString() {
		return "Permission [bankId=" + bankId + ", accountId=" + accountId + ", userId=" + userId + ", providerId="
				+ providerId + ", viewId=" + viewId + ", viewJson=" + viewJson + "]";
	}
}
