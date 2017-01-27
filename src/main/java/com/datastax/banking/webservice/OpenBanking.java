package com.datastax.banking.webservice;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.banking.model.Transaction;
import com.datastax.banking.model.TransactionByAccount;
import com.datastax.banking.service.BankService;

@WebService
@Path("/")
public class OpenBanking {

	private Logger logger = LoggerFactory.getLogger(OpenBanking.class);
	private SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
	
	@GET
	@Path("/my/banks/{BANK_ID}/accounts/{ACCOUNT_ID}/transactions")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("BANK_ID") String bankId, @PathParam("ACCOUNT_ID") String accountId) {
		
		List<TransactionByAccount> transactions = BankService.getInstance().getTransactions(accountId);
		
		return Response.status(Status.OK).entity(transactions).build();
	}
	
	@GET
	@Path("/my/banks/{BANK_ID}/accounts/{ACCOUNT_ID}/{VIEW_ID}/transactions/{TRANSACTION_ID}/transaction")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAccounts(@PathParam("BANK_ID") String bankId, @PathParam("ACCOUNT_ID") String accountId,
			@PathParam("TRANSACTION_ID") String transactionId) {
		
		Transaction transaction = BankService.getInstance().getTransaction(transactionId);
		
		return Response.status(Status.OK).entity(transaction).build();
	}
	
}
