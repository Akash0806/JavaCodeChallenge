package com.db.awmd.challenge.service.implementations;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferRequestBean;
import com.db.awmd.challenge.exception.InsufficientBalance;
import com.db.awmd.challenge.exception.SimilarAccountIdException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.services.FundTransferService;
import com.db.awmd.challenge.services.NotificationService;

@Service
public class FundTransferServiceImplementation implements FundTransferService {
	
	
	 private final AccountsRepository accountsRepository;
	 
	 
	 private final NotificationService notificationService; 
	 
	 
	  @Autowired
	  public FundTransferServiceImplementation(AccountsRepository accountsRepository,EmailNotificationService notificationService) {
	    this.accountsRepository = accountsRepository;
	    this.notificationService = notificationService;
	  }
	
	@Override
	public void fundTransfer(FundTransferRequestBean requestBean) throws SimilarAccountIdException, InsufficientBalance, InvaildAccount{
		
		String fromAccountId=requestBean.getAccountFromId();
		String toAccountId=requestBean.getAccountToId();
		Account fromAccount = getAccountDetails(fromAccountId);
		Account toAccount = getAccountDetails(toAccountId);
		if(fromAccountId.equals(toAccountId)){
			throw new SimilarAccountIdException(" From and To Account Id Can't be Similar");
		}
		BigDecimal balance = fromAccount.getBalance();
		BigDecimal transferAmount = requestBean.getAmount();
		if(balance.compareTo(new BigDecimal("0"))<1 || balance.compareTo(transferAmount)<1){
				throw new InsufficientBalance("Balance Can't be Zero or less then transfer amount");
		}
		if(transferMoney(fromAccount, toAccount, transferAmount)){
			sendNotification(fromAccount, toAccount, transferAmount);
		}
	}
	
    private void sendNotification(Account fromAccount,Account toAccount, BigDecimal amount){
	  String toNotificationMessage = "Amount ="+amount.toString()+" transferred from account"+fromAccount.getAccountId();
	  String fromNotificationMessage = "Amount ="+amount.toString()+" transferred to account"+toAccount.getAccountId();
	  notificationService.notifyAboutTransfer(fromAccount, fromNotificationMessage);
	  notificationService.notifyAboutTransfer(toAccount, toNotificationMessage);
	  
    }
	
   
	private boolean transferMoney(Account fromAccount,Account toAccount, BigDecimal amount){
	    boolean isTransferSuccess = false;
		BigDecimal creditBalance = accountsRepository.debitFromAccount(fromAccount, amount);
		BigDecimal debitBalance = accountsRepository.creditToAccount(toAccount, amount);
		if(creditBalance.compareTo(fromAccount.getBalance())<1 && debitBalance.compareTo(toAccount.getBalance())>0){
			fromAccount.setBalance(creditBalance);
			toAccount.setBalance(debitBalance);
			isTransferSuccess=true;
		}
		return isTransferSuccess;
	}
	
	private Account getAccountDetails(String accountId) throws InvaildAccount{
		Account accountObject= accountsRepository.getAccount(accountId);
		if(accountObject==null || StringUtils.isEmpty(accountObject.getAccountId())){
			throw new InvaildAccount("Invaild Account");
		}
		return accountObject;
	}

}
