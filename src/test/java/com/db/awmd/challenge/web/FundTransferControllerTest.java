package com.db.awmd.challenge.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.FundTransferRequestBean;
import com.db.awmd.challenge.service.implementations.AccountsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FundTransferControllerTest {
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private AccountsService accountsService;
	
	
	
	@Before
	  public void prepareMockMvc() {
	    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

	    // Reset the existing accounts before each test.
	    accountsService.getAccountsRepository().clearAccounts();
	  }

	@Test
	  public void fundTransfer_whenToAccountIdBlank_ReturnBadRequest() throws Exception {
		String accountFromId = "Id-123";
		String amount = "100";
		String accountToId = "";
		FundTransferRequestBean bean = getRequestBean(accountFromId, amount, accountToId);
	    this.mockMvc.perform(post("/v1/fundtransfer")
	    		.contentType(MediaType.APPLICATION_JSON)
	    		.content(asJsonString(bean)))
	    .andExpect(status().isBadRequest());
	  }

	private FundTransferRequestBean getRequestBean(String accountFromId, String amount, String accountToId) {
		FundTransferRequestBean bean =new FundTransferRequestBean();
		bean.setAccountFromId(accountFromId);
		bean.setAccountToId(accountToId);
		bean.setAmount(new BigDecimal(amount));
		return bean;
	}
	
	 /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
