package com.kutyrina.accountexchanger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kutyrina.accountexchanger.dto.ClientChangePasswordRequest;
import com.kutyrina.accountexchanger.dto.ClientRequest;
import com.kutyrina.accountexchanger.dto.ClientResponse;
import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.extention.DataBaseCleanerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(DataBaseCleanerExtension.class)
public class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void addNewAccount() throws Exception {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setLogin("TestLogin2");
        clientRequest.setPassword("$2a$10$BFa7UWLx7pVPlyUWSGdCiePfyLi4.noJoNXoQIoZCoQkqG5P43FBa");

        MockHttpServletRequestBuilder builder = post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(clientRequest));


        MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        ClientResponse clientResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ClientResponse.class);

        Assertions.assertThat(clientResponse.getLogin()).isEqualTo(clientRequest.getLogin());

    }

    @Test
    void changePassword() throws Exception {

        Client client = new Client();
        client.setLogin("TestLogin");
        ClientChangePasswordRequest clientRequest = new ClientChangePasswordRequest("TestPassword123");

        MockHttpServletRequestBuilder builder = patch("/client/change-password")
                .header(HttpHeaders.AUTHORIZATION, "Basic VGVzdExvZ2luOlRlc3RQYXNzd29yZA==")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(clientRequest.getNewPassword()));

        MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.getMassage(mvcResult.getResponse().getContentAsString());

        Assertions.assertThat(clientResponse.getMassage(mvcResult.getResponse().getContentAsString())).contains(client.getLogin());


    }
}
