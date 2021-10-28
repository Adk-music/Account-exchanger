package com.kutyrina.accountexchanger.account.service;

import com.kutyrina.accountexchanger.component.SessionHolderComponent;
import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.entity.Account;
import com.kutyrina.accountexchanger.entity.Client;
import com.kutyrina.accountexchanger.repository.AccountRepository;
import com.kutyrina.accountexchanger.repository.ClientRepository;
import com.kutyrina.accountexchanger.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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

    @Mock
    ClientRepository clientRepository;


    @Test
    void getAccounts() {
        long accountId = 1L;
        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());
        Account account = new Account();
        account.setAccountNumber(accountId);
        doReturn(Optional.of(account)).when(accountRepository).findById(accountId);

        AccountResponse accounts = accountServiceMock.getAccounts(accountId);

        Assertions.assertThat(accounts.getId()).isEqualTo(accountId);
        verify(sessionHolderComponent).validateAccessToAccount(anyLong());
        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccounts_Error_NotFound() {
        long accountId = 1L;
        doReturn(Optional.empty()).when(accountRepository).findById(accountId);

        Assertions.assertThatThrownBy(() -> accountServiceMock.getAccounts(accountId))
                .isExactlyInstanceOf(ResponseStatusException.class);

        verify(accountRepository).findById(accountId);
    }

    @Test
    void addNewAccount() {
        String login = "TestLogin";
        Account account = new Account();
        Client client = new Client();
        client.setLogin(login);

        doReturn(login)
                .when(sessionHolderComponent).getCurrentUserLogin();

        doReturn(Optional.of(client))
                .when(clientRepository).findByLogin(login);

        doReturn(account).when(accountRepository).save(any());

        accountServiceMock.addNewAccount();

        verify(accountRepository).save(any());
        verify(clientRepository).findByLogin(login);
        verify(sessionHolderComponent).getCurrentUserLogin();
    }

    @Test
    void changeAccountBalance() {
        long accountId = 1L;

        Account account = new Account();
        account.setAccountNumber(accountId);

        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());
        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber(accountId);
        doReturn(account).when(accountRepository).save(any());

        accountServiceMock.changeAccountBalance(accountId, new BigDecimal("100"));

        verify(accountRepository).save(any());
        verify(accountRepository).findByAccountNumber(accountId);
        verify(accountRepository).save(any());
    }

    @Test
    void changeAccountBalance_Error_Balance_Less_Amount() {
        long accountId = 1L;

        Account account = new Account();
        account.setAccountNumber(accountId);
        account.setBalance(new BigDecimal(500));

        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());
        doReturn(Optional.of(account)).when(accountRepository).findByAccountNumber(accountId);


        Assertions.assertThatThrownBy(() -> accountServiceMock.changeAccountBalance(accountId, new BigDecimal("-600")))
                .isExactlyInstanceOf(ResponseStatusException.class);

        verify(sessionHolderComponent).validateAccessToAccount(anyLong());
        verify(accountRepository).findByAccountNumber(accountId);
    }

    @Test
    void transferMoneyToAnotherUser() {
        long accountFromId = 1L;
        long accountToId = 2L;

        Account accountFrom = new Account();
        accountFrom.setAccountNumber(accountFromId);
        accountFrom.setBalance(new BigDecimal("100"));

        Account accountTo = new Account();
        accountTo.setAccountNumber(accountToId);
        accountTo.setBalance(new BigDecimal("50"));

        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());

        doReturn(Optional.of(accountFrom)).when(accountRepository).findByAccountNumber(accountFromId);

        doReturn(Optional.of(accountTo)).when(accountRepository).findByAccountNumber(accountToId);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(accountFromId);
        transferMoneyRequest.setAccountTo(accountToId);
        transferMoneyRequest.setAmount(new BigDecimal("10"));

        accountServiceMock.transferMoneyToAnotherUser(transferMoneyRequest);

        InOrder inOrder = inOrder(accountRepository);

        inOrder.verify(accountRepository).findByAccountNumber(accountToId);
        inOrder.verify(accountRepository).findByAccountNumber(accountFromId);

        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void transferMoneyToAnotherUser_Verify_Reverse_Order() {
        long accountFromId = 2L;
        long accountToId = 1L;

        Account accountFrom = new Account();
        accountFrom.setAccountNumber(accountFromId);
        accountFrom.setBalance(new BigDecimal("100"));

        Account accountTo = new Account();
        accountTo.setAccountNumber(accountToId);
        accountTo.setBalance(new BigDecimal("50"));

        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());

        doReturn(Optional.of(accountFrom)).when(accountRepository).findByAccountNumber(accountFromId);

        doReturn(Optional.of(accountTo)).when(accountRepository).findByAccountNumber(accountToId);

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(accountFromId);
        transferMoneyRequest.setAccountTo(accountToId);
        transferMoneyRequest.setAmount(new BigDecimal("10"));

        accountServiceMock.transferMoneyToAnotherUser(transferMoneyRequest);

        InOrder inOrder = inOrder(accountRepository);

        inOrder.verify(accountRepository).findByAccountNumber(accountFromId);
        inOrder.verify(accountRepository).findByAccountNumber(accountToId);

        verify(accountRepository, times(2)).save(any());
    }

    @Test
    void transferMoneyToAnotherUser_Error_Same_Account() {
        long accountFromId = 1L;
        long accountToId = 1L;

        doNothing()
                .when(sessionHolderComponent).validateAccessToAccount(anyLong());

        TransferMoneyRequest transferMoneyRequest = new TransferMoneyRequest();
        transferMoneyRequest.setAccountFrom(accountFromId);
        transferMoneyRequest.setAccountTo(accountToId);
        transferMoneyRequest.setAmount(new BigDecimal("10"));

        Assertions.assertThatThrownBy(() -> accountServiceMock.transferMoneyToAnotherUser(transferMoneyRequest))
                .isExactlyInstanceOf(ResponseStatusException.class);

        verify(sessionHolderComponent).validateAccessToAccount(anyLong());

    }
}