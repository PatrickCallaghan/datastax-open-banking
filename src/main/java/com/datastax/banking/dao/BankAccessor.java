package com.datastax.banking.dao;

import com.datastax.banking.model.Accounts;
import com.datastax.banking.model.Atms;
import com.datastax.banking.model.Branches;
import com.datastax.banking.model.Products;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface BankAccessor {

	@Query("SELECT * FROM products where bank_id = ?")
    Result<Products> getAllProducts();

	@Query("SELECT * FROM accounts where bank_id = ?")
    Result<Accounts> getAllAccounts();

	@Query("SELECT * FROM atms where bank_id = ?")
    Result<Atms> getAllAtms();

	@Query("SELECT * FROM branches where bank_id = ?")
    Result<Branches> getAllBranches();

	@Query("SELECT * FROM transactions where bank_id = ?")
    Result<Products> getAllTransactions();

	@Query("SELECT * FROM banks where bank_id = ?")
    Result<Products> getAllBanks();	
}
