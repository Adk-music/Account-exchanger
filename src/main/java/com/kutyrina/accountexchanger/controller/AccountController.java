package com.kutyrina.accountexchanger.controller;

import com.kutyrina.accountexchanger.account.service.AccountService;
import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.dto.TransferMoneyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
public class AccountController {

    @Autowired
    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("account/{accountId}")
    public AccountResponse getAccounts(@PathVariable Long accountId) {
        return accountService.getAccounts(accountId);
    }


    @PostMapping("account")
    public AccountResponse addNewAccount() {
        return accountService.addNewAccount();
    }

    @PatchMapping("account/{accountId}")
    public AccountResponse withdrawFromAccount(@PathVariable Long accountId, @RequestBody BigDecimal amount) {
        return accountService.withdrawFromAccount(accountId, amount);
    }

    @PatchMapping("account/transfer")
    public TransferMoneyResponse transferMoneyToAnotherUser(@RequestBody @Valid TransferMoneyRequest transferMoneyRequest) {
        return accountService.transferMoneyToAnotherUser(transferMoneyRequest);
    }


}
