# datastax-open-banking

This requires DataStax Enterprise running in SearchAnalytics mode.

To create the schema, run the following

	mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup" -DcontactPoints=localhost

To create the transactions, run the following (note the create parameter to create customers and accounts as well)
	
	mvn clean compile exec:java -Dexec.mainClass="com.datastax.banking.Main"  -DcontactPoints=localhost -Dcreate=true

You can use the following parameters to change the default no of transactions, customers and no of days.
	
	-DnoOfTransactions=10000000 -DnoOfCustomers=1000000 -DnoOfDays=5

Create the search component for the transactions 

	dsetool create_core openb.transaction schema=src/main/resources/solr_schema.xml solrconfig=src/main/resources/solr_config.xml reindex=true	


RealTime transactions

When all historical transactions are loaded, the process will start creating random transactions for todays date and time. If you wish just to run real time transactions specify -DnoOfDays=0.

You can use the cql shell (cqlsh) to access data in the tables eg.

	select * from bank;
	
	select * from account;		

To use the search functionality of dse,

	select * from transaction where solr_query = 'account_id:05237266-b334-4704-a087-5b460a2ecf04';

Find transactions for this account where value is under 10

	select * from transaction where solr_query = '"q":"account_id:05237266-b334-4704-a087-5b460a2ecf04","fq":"value:[-10 TO 0]"'; 

To use the spark shell to run commands, run 'dse spark' from a dse node. To count all the transactions run

	val table = sc.cassandraTable("openb", "transaction"); 	
	table.count()

To use the web service run 

	mvn jetty:run -Djetty.port=8081
	
The api for the webservices are 

Get Transactions For Account 
	
	http://{server}:8081/rest/my/banks/{BANK_ID}/accounts/{ACCOUNT_ID}/transactions
	
	http://localhost:8081/rest/my/banks/{BANK_ID}/accounts/05237266-b334-4704-a087-5b460a2ecf04/transactions

To remove the tables and the schema, run the following.

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaTeardown"

