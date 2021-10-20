package com.kutyrina.accountexchanger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.dto.TransferMoneyResponse;
import com.kutyrina.accountexchanger.extention.DataBaseCleanerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DataBaseCleanerExtension.class)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAccounts() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/account/{accountId}", 1))
                .andExpect(status().isOk())
                .andReturn();

        AccountResponse accountResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountResponse.class);

        Assertions.assertThat(accountResponse.getBalance()).isEqualByComparingTo(new BigDecimal("150.00"));
        Assertions.assertThat(accountResponse.getId()).isEqualTo(1);

    }

    @Test
    void addNewAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/account"))
                .andExpect(status().isOk())
                .andReturn();

        AccountResponse accountResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountResponse.class);

        Assertions.assertThat(accountResponse.getBalance()).isEqualByComparingTo(new BigDecimal("0"));
        Assertions.assertThat(accountResponse.getId()).isNotZero();

    }

    @Test
    void withdrawFromAccount() throws Exception {
        MockHttpServletRequestBuilder builder = patch("/account/{accountId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("200");

        MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        AccountResponse accountResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AccountResponse.class);

        Assertions.assertThat(accountResponse.getId()).isEqualTo(1);
        Assertions.assertThat(accountResponse.getBalance()).isEqualByComparingTo(new BigDecimal("300"));

        System.out.println(accountResponse.getId());
        System.out.println(accountResponse.getBalance());

    }

    @Test
    void transferMoneyToAnotherUser() throws Exception {
        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(1L);
        transferMoneyRequest.setAccountTo(2L);
        transferMoneyRequest.setAmount(new BigDecimal("100"));

        MockHttpServletRequestBuilder builder = patch("/account/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(transferMoneyRequest));

        MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        TransferMoneyResponse transferMoneyResponse =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TransferMoneyResponse.class);

        Assertions.assertThat(transferMoneyResponse.getAccountFrom().getId().toString()).isEqualTo(transferMoneyRequest.getAccountFrom().toString());
        Assertions.assertThat(transferMoneyResponse.getAccountFrom().getBalance()).isEqualByComparingTo(new BigDecimal("200"));
        Assertions.assertThat(transferMoneyResponse.getAccountTo().getId().toString()).isEqualTo(transferMoneyRequest.getAccountTo().toString());
        Assertions.assertThat(transferMoneyResponse.getAccountTo().getBalance()).isEqualByComparingTo(new BigDecimal("300"));


    }
}