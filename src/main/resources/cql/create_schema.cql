create keyspace if not exists ob WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

use ob;


create table if not exists transactions_by_account(
	account_id text,
	completed timestamp,
 	transaction_id text,
	json text,
	PRIMARY KEY ((account_id), completed, transaction_id)
)WITH CLUSTERING  order by (completed desc, transaction_id ASC); 

create table if not exists transactions(
 	transaction_id text,
	json text,
	PRIMARY KEY (transaction_id)
);


