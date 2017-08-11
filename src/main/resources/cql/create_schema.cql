create keyspace if not exists openb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use openb;

create table if not exists transaction(
 	transaction_id text,
	completed timestamp,
	account_id text,
	value double,
	counterparty text	,
	json text,
	PRIMARY KEY (account_id, completed, transaction_id)
) with clustering order by (completed desc, transaction_id asc);
	
create table if not exists bank(
	banks text, 
	bank_id text,
	json text,
	PRIMARY KEY (banks, bank_id)
);

create table if not exists branch(
	bank_id text,	
	branch_id text,
	json text,
	PRIMARY KEY (bank_id, branch_id)
);

create table if not exists atm(
	bank_id text, 
	atm_id text,
	latlon text,
	json text,
	PRIMARY KEY (bank_id, atm_id)
);

create table if not exists product(
	bank_id text, 
	code text,
	json text,
	PRIMARY KEY (bank_id, code)
);

create table if not exists account	(
	bank_id text, 
	account_id text,
	iban text,
	number text,
	json text,
	PRIMARY KEY (bank_id, account_id)
);

create table if not exists user(
	user_name text PRIMARY KEY,
	email text,
	password text,
	display_name text
);

create table if not exists permission (
	bank_id text,
	account_id text,
	user_id text,
	provider_id text,
	view_id text,
	view_json text,
	PRIMARY KEY ((bank_id, account_id), user_id, provider_id, view_id)
);

truncate table transaction;
truncate table atm;
truncate table branch;
truncate table bank;
truncate table product;
truncate table account;
truncate table user;
truncate table permission;
