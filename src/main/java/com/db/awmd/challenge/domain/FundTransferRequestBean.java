package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class FundTransferRequestBean {
@NotNull
@NotEmpty(message = "From account id can't be empty")
private String accountFromId;
@NotNull
@NotEmpty(message = "To account ID can't be empty")
private String accountToId;
@NotNull
@Min(value = 0, message = "Amount must be positive.")
private BigDecimal amount;
public String getAccountFromId() {
	return accountFromId;
}
public void setAccountFromId(String accountFromId) {
	this.accountFromId = accountFromId;
}
public String getAccountToId() {
	return accountToId;
}
public void setAccountToId(String accountToId) {
	this.accountToId = accountToId;
}
public BigDecimal getAmount() {
	return amount;
}
public void setAmount(BigDecimal amount) {
	this.amount = amount;
}


}
