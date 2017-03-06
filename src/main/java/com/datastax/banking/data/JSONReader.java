package com.datastax.banking.data;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Account;
import com.datastax.banking.model.Atm;
import com.datastax.banking.model.Bank;
import com.datastax.banking.model.Branch;
import com.datastax.banking.model.Product;
import com.datastax.banking.model.Transaction;
import com.datastax.banking.service.BankService;
import com.datastax.demo.utils.FileUtils;

public class JSONReader {

	private static final Logger logger = LoggerFactory.getLogger(JSONReader.class);
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	public JSONReader() {
		JSONParser parser = new JSONParser();

		BankService bankService = BankService.getInstance();
		try {

			Object obj = parser.parse(FileUtils.readFileIntoString("open_banking.json"));

			JSONObject jsonObject = (JSONObject) obj;			
			JSONArray usersList = (JSONArray) jsonObject.get("users");			
			JSONArray banksList = (JSONArray) jsonObject.get("banks");			
			JSONArray accountsList = (JSONArray) jsonObject.get("accounts");			
			JSONArray transactionsList = (JSONArray) jsonObject.get("transactions");			
			JSONArray branchesList = (JSONArray) jsonObject.get("branches");			
			JSONArray atmsList = (JSONArray) jsonObject.get("atms");						
			JSONArray productsList = (JSONArray) jsonObject.get("products");
			
			Iterator<JSONObject> iterator = usersList.iterator();
            while (iterator.hasNext()) {
            	JSONObject user = iterator.next();
            	
            	//logger.info("Saving " + user.get("email").toString());
            	//bankService.saveUser(new User(user.get("email").toString(), user.toJSONString()));
            }            	

            
			iterator = accountsList.iterator();
            while (iterator.hasNext()) {
            	JSONObject account = iterator.next();
            	
            	logger.info("Saving Account " + account.get("id").toString());
            	bankService.saveAccount(new Account(account.get("bank").toString(), account.get("id").toString(), 
            			account.get("IBAN").toString(), account.get("number").toString(), account.toJSONString()));
            }            	

			iterator = banksList.iterator();
            while (iterator.hasNext()) {
            	JSONObject bank = iterator.next();
            	
            	logger.info("Saving Banks " + bank.get("id").toString());
            	bankService.saveBank(new Bank(bank.get("id").toString(), bank.toJSONString()));
            }            	

			iterator = branchesList.iterator();
            while (iterator.hasNext()) {
            	JSONObject branch = iterator.next();
            	
            	logger.info("Saving Branches " + branch.get("bank_id").toString());
            	bankService.saveBranch(new Branch(branch.get("bank_id").toString(), branch.get("id").toString(), branch.toJSONString()));
            }            	
		
			iterator = atmsList.iterator();
            while (iterator.hasNext()) {
            	JSONObject atm = iterator.next();
            	
            	logger.info("Saving Atms " + atm.get("bank_id").toString());
            	
            	JSONObject location = (JSONObject) atm.get("location");
            	String latlon = location.get("latitude") + "," + location.get("longitude");
            	
            	bankService.saveAtm(new Atm(atm.get("bank_id").toString(), atm.get("id").toString(), latlon,
            			atm.toJSONString()));
            } 
            
			iterator = productsList.iterator();
            while (iterator.hasNext()) {
            	JSONObject product = iterator.next();
            	
            	logger.info("Saving product " + product.get("bank_id").toString());
            	bankService.saveProduct(new Product(product.get("bank_id").toString(), product.get("code").toString(), product.toJSONString()));
            } 
            
			iterator = transactionsList.iterator();
            while (iterator.hasNext()) {
            	JSONObject transaction = iterator.next();
            	String transactionId = transaction.get("id").toString();
            	
            	JSONObject thisAccount = (JSONObject) transaction.get("this_account");
            	String accountId = thisAccount.get("id").toString();
            	
            	
            	JSONObject counterparty = (JSONObject) transaction.get("counterparty");
            	String counterpartyName = counterparty.get("name").toString();
            	
            	JSONObject details = (JSONObject) transaction.get("details");
            	Date completed = dateFormatter.parse(details.get("completed").toString());
            	double value = new Double(details.get("value").toString()).doubleValue();
            	
            	logger.info("Saving Transaction " + transaction.get("id").toString());
            	bankService.saveTransacton(new Transaction(transactionId, completed, accountId, counterpartyName, value, transaction.toJSONString()));
            } 
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		new JSONReader();
	}
}
