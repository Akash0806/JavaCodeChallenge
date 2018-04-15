package com.db.awmd.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.FundTransferRequestBean;
import com.db.awmd.challenge.services.FundTransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/fundtransfer")
@Slf4j
public class FundTransferController {
	
	 private final FundTransferService fundTransferService;

	  @Autowired
	  public FundTransferController(FundTransferService fundTransferService) {
	    this.fundTransferService = fundTransferService;
	  }

	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	  public ResponseEntity<Object> fundTransfer(@RequestBody @Valid FundTransferRequestBean fundTransferRequestBean){
		return new ResponseEntity<Object>(HttpStatus.CREATED);
		  
	  }

}
