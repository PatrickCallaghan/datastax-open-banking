package com.datastax.banking.dao;

import com.datastax.banking.model.Account;
import com.datastax.banking.model.Atm;
import com.datastax.banking.model.Branch;
import com.datastax.banking.model.Product;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface BankAccessor {

	@Query("SELECT * FROM products where bank_id = ?")
    Result<Product> getAllProducts();

	@Query("SELECT * FROM accounts where bank_id = ?")
    Result<Account> getAllAccounts();

	@Query("SELECT * FROM atms where bank_id = ?")
    Result<Atm> getAllAtms();

	@Query("SELECT * FROM branches where bank_id = ?")
    Result<Branch> getAllBranches();

	@Query("SELECT * FROM transactions where bank_id = ?")
    Result<Product> getAllTransactions();

	@Query("SELECT * FROM banks where bank_id = ?")
    Result<Product> getAllBanks();	
}
