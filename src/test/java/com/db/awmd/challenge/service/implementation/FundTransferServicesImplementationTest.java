package com.db.awmd.challenge.service.implementation;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferRequestBean;
import com.db.awmd.challenge.exception.InsufficientBalance;
import com.db.awmd.challenge.exception.SimilarAccountIdException;
import com.db.awmd.challenge.service.implementations.AccountsService;
import com.db.awmd.challenge.service.implementations.FundTransferServiceImplementation;
import com.db.awmd.challenge.service.implementations.InvaildAccount;
import com.db.awmd.challenge.services.FundTransferService;
import com.db.awmd.challenge.services.NotificationService;
@RunWith(SpringRunner.class)
@SpringBootTest
public class FundTransferServicesImplementationTest {

  @Resource(type=FundTransferServiceImplementation.class)
  private FundTransferService fundTransferService;
  
  @Autowired
  private  AccountsService accountsService;
  
  @Mock
  private NotificationService notificationService;
  
  @Before
  public void prepareMockMvc() {

    // Reset the existing accounts before each test.
    accountsService.getAccountsRepository().clearAccounts();
  }
 
	

	@Test(expected=SimilarAccountIdException.class)
	public void fundTransfer_WhenFromAndToAccountAreSame_ReturnSimilarBothAccountIdException() throws SimilarAccountIdException, InsufficientBalance, InvaildAccount {
		FundTransferRequestBean request = new FundTransferRequestBean();
		Account accountDetails = new Account("Id-123", new BigDecimal("0"));
		this.accountsService.createAccount(accountDetails);
		request.setAccountFromId("Id-123");
		request.setAccountToId("Id-123");
		fundTransferService.fundTransfer(request);
     }
	
	@Test(expected=InsufficientBalance.class)
	public void fundTransfer_WhenBalanceIsZero_ReturnInsufficientBalanceException() throws SimilarAccountIdException, InsufficientBalance, InvaildAccount {
		FundTransferRequestBean request = new FundTransferRequestBean();
		request.setAccountFromId("Id-123");
		Account accountDetails = new Account("Id-123", new BigDecimal("0"));
		this.accountsService.createAccount(accountDetails);
		fundTransferService.fundTransfer(request);
     }
	
	@Test(expected=InsufficientBalance.class)
	public void fundTransfer_WhenBalanceIsLessThenTransferAmount_ReturnInsufficientBalanceException() throws SimilarAccountIdException, InsufficientBalance, InvaildAccount {
		FundTransferRequestBean request = new FundTransferRequestBean();
		request.setAccountFromId("Id-123");
		request.setAmount(new BigDecimal("100"));
		Account accountDetails = new Account("Id-123", new BigDecimal("10"));
		this.accountsService.createAccount(accountDetails);
		fundTransferService.fundTransfer(request);
     }

	@Test(expected=InvaildAccount.class)
	public void fundTransfer_WhenAccountIdDifferent_ReturnInvaildAccount() throws SimilarAccountIdException, InsufficientBalance, InvaildAccount {
		FundTransferRequestBean request = new FundTransferRequestBean();
		request.setAccountFromId("Id-125");
		request.setAmount(new BigDecimal("100"));
		fundTransferService.fundTransfer(request);
     }
	
@Test
	public void fundTransfer_WhenTransferAmount_ReturnRemainingAmount() throws SimilarAccountIdException, InsufficientBalance, InvaildAccount {
		FundTransferRequestBean request = new FundTransferRequestBean();
		Account fromAccountDetails = new Account("Id-123", new BigDecimal("100"));
		this.accountsService.createAccount(fromAccountDetails);
		Account toAccountDetails = new Account("Id-124", new BigDecimal("0"));
		this.accountsService.createAccount(toAccountDetails);
		request.setAccountFromId("Id-123");
		request.setAccountToId("Id-124");
		request.setAmount(new BigDecimal("20"));
		fundTransferService.fundTransfer(request);
		assertThat(this.accountsService.getAccount("Id-123").getBalance()).isEqualTo(new BigDecimal("80"));
		assertThat(this.accountsService.getAccount("Id-124").getBalance()).isEqualTo(new BigDecimal("20"));
		
     }
}
