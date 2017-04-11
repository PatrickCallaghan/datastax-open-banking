package com.datastax.banking.data;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Transaction;

public class BankGenerator {

	private static final Logger logger = LoggerFactory.getLogger(BankGenerator.class);
	private static final int BASE = 1000000;
	private static final int DAY_MILLIS = 1000 * 60 *60 * 24;
	private static List<String> accountTypes = Arrays.asList("Current", "Joint Current", "Saving", "Mortgage", "E-Saving", "Deposit");
	private static NumberFormat numFormatter = NumberFormat.getCurrencyInstance();
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	private static String transactionStr = "{\"id\":\"%s\",\"this_account\":{\"id\":\"%s\",\"bank\":\"psd201-bank-x--uk\"},\"counterparty\":{\"name\":\"%s\"}," + 
			"\"details\":{\"type\":\"10219\",\"description\":\"\",\"posted\":\"%s\",\"completed\":\"%s\",\"new_balance\":\"%s\",\"value\":\"%s\"}}";
	
	//We can change this from the Main
	public static DateTime date = new DateTime().withTimeAtStartOfDay();
	
	public static List<String> whiteList = new ArrayList<String>();
			

	public static String getRandomCustomerId(int noOfCustomers){
		return BASE + new Double(Math.random()*noOfCustomers).intValue() + "";
	}
	
	public static Transaction createRandomTransaction(int noOfDays, int noOfCustomers) {

		int noOfMillis = noOfDays * DAY_MILLIS;
		
		// create time by adding a random no of millis 
		DateTime newDate = date.minusMillis(new Double(Math.random() * noOfMillis).intValue() + 1);
		
		return createRandomTransaction(newDate, noOfCustomers);
	}

	public static Transaction createRandomTransaction(DateTime newDate, int noOfCustomers) {

		//Random account
		int customerId = new Double(Math.random() * noOfCustomers).intValue() + 1;
		String accountId = Integer.toString(getRandomAccountForCustomer(customerId));
		double amount = Math.random() * 100;
		String counterparty = counterpartys.get(new Double(Math.random()*counterpartys.size()).intValue());
		String transactionId = UUID.randomUUID().toString(); 
		Date completed = newDate.toDate();
		
		String json = String.format(transactionStr, transactionId, accountId, counterparty, dateFormatter.format(completed), 
				dateFormatter.format(completed), numFormatter.format(Math.random()*1000), numFormatter.format(amount));
			
		return new Transaction(transactionId, completed, accountId, counterparty, amount, json); 
	}
	
	public static int getRandomAccountForCustomer(int customerId){
		return (customerId * 10) + new Double(Math.random() * 3d).intValue(); 
	}
	
	public static List<String> locations = Arrays.asList("London", "Manchester", "Liverpool", "Glasgow", "Dundee",
			"Birmingham", "Dublin", "Devon");

	public static List<String> counterpartys = Arrays.asList("Tesco", "Sainsbury", "Asda Wal-Mart Stores", "Morrisons",
			"Marks & Spencer", "Boots", "John Lewis", "Waitrose", "Argos", "Co-op", "Currys", "PC World", "B&Q",
			"Somerfield", "Next", "Spar", "Amazon", "Costa", "Starbucks", "BestBuy", "Wickes", "TFL", "National Rail",
			"Pizza Hut", "Local Pub");

	public static List<String> notes = Arrays.asList("Shopping", "Shopping", "Shopping", "Shopping", "Shopping",
			"Pharmacy", "HouseHold", "Shopping", "Household", "Shopping", "Tech", "Tech", "Diy", "Shopping", "Clothes",
			"Shopping", "Amazon", "Coffee", "Coffee", "Tech", "Diy", "Travel", "Travel", "Eating out", "Eating out");

	public static List<String> tagList = Arrays.asList("Home", "Home", "Home", "Home", "Home", "Home", "Home", "Home",
			"Work", "Work", "Work", "Home", "Home", "Home", "Work", "Work", "Home", "Work", "Work", "Work", "Work",
			"Work", "Work", "Work", "Work", "Expenses", "Luxury", "Entertaining", "School");

}
