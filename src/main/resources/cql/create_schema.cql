create keyspace if not exists ob WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use ob;


create table if not exists transactions_by_account(
	account_id text,
	completed timestamp,
 	transaction_id text,
	json text,
	PRIMARY KEY ((account_id), completed, transaction_id)
)WITH CLUSTERING  order by (completed desc, transaction_id ASC); 

create keyspace if not exists openb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use openb;

create table if not exists transactions(
 	transaction_id text,
	json text,
	PRIMARY KEY (transaction_id)
);

create table if not exists banks(
	banks text, 
	bank_id text,
	json text,
	PRIMARY KEY (banks, bank_id)
);

create table if not exists branches(
	bank_id text,
	branch_id text,
	json text,
	PRIMARY KEY (bank_id, branch_id)
);

create table if not exists atms(
	bank_id text, 
	atm_id text,
	latlon text,
	json text,
	PRIMARY KEY (bank_id, atm_id)
);

create table if not exists products(
	bank_id text, 
	code text,
	json text,
	PRIMARY KEY (bank_id, code)
);

create table if not exists accounts(
	bank_id text, 
	account_id text,
	iban text,
	number text,
	json text,
	PRIMARY KEY (bank_id, account_id)
);