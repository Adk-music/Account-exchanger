package com.kutyrina.accountexchanger.account.service;

import com.kutyrina.accountexchanger.component.SessionHolderComponent;
import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.entity.Account;
import com.kutyrina.accountexchanger.repository.AccountRepository;
import com.kutyrina.accountexchanger.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    AccountService accountServiceMock;

    @Mock
    AccountRepository accountRepository;
    @Mock
    SessionHolderComponent sessionHolderComponent;


    @Test
    void getAccounts() {
        long accountId = 1L;
        Account account = new Account();
        account.setAccountNumber(accountId);
        doReturn(Optional.of(account)).when(accountRepository).findById(accountId);
        AccountResponse accounts = accountServiceMock.getAccounts(accountId);
        Assertions.assertThat(accounts.getId()).isEqualTo(accountId);

        verify(accountRepository).findById(accountId);
    }

    @Test
    void addNewAccount() {

        Account account = new Account();
        doReturn(account).when(accountRepository).save(any());

        accountServiceMock.addNewAccount();

        verify(accountRepository).save(any());
    }

    @Test
    void withdrawFromAccount() {
        long accountId = 1L;

        Account account = new Account();
        account.setAccountNumber(accountId);
        doReturn(Optional.of(account)).when(accountRepository).findById(accountId);
        doReturn(account).when(accountRepository).save(any());

        accountServiceMock.changeAccountBalance(accountId, new BigDecimal("100"));

        verify(accountRepository).save(any());
    }

    @Test
    void transferMoneyToAnotherUser() {
        long accountFromId = 1L;
        long accountToId = 2L;

        Account accountFrom = new Account();
        accountFrom.setAccountNumber(accountFromId);
        accountFrom.setBalance(new BigDecimal("100"));

        doReturn(Optional.of(accountFrom)).when(accountRepository).findByAccountNumber(accountFromId);

        Account accountTo = new Account();
        accountTo.setAccountNumber(accountToId);
        accountTo.setBalance(new BigDecimal("50"));

        doReturn(Optional.of(accountTo)).when(accountRepository).findByAccountNumber(accountToId);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(accountFromId);
        transferMoneyRequest.setAccountTo(accountToId);
        transferMoneyRequest.setAmount(new BigDecimal("10"));

        accountServiceMock.transferMoneyToAnotherUser(transferMoneyRequest);

        verify(accountRepository).findByAccountNumber(accountFromId);
        verify(accountRepository).findByAccountNumber(accountToId);
        verify(accountRepository, times(2)).save(any());
    }
}