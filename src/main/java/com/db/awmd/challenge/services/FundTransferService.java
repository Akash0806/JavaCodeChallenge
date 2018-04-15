package com.db.awmd.challenge.services;

import com.db.awmd.challenge.domain.FundTransferRequestBean;
import com.db.awmd.challenge.exception.InsufficientBalance;
import com.db.awmd.challenge.exception.SimilarAccountIdException;
import com.db.awmd.challenge.service.implementations.InvaildAccount;

public interface FundTransferService {

	void fundTransfer(FundTransferRequestBean requestBean) throws SimilarAccountIdException, InsufficientBalance, InvaildAccount;

}
