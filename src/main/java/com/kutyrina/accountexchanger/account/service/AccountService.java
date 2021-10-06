package com.kutyrina.accountexchanger.account.service;

import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.dto.TransferMoneyResponse;
import com.kutyrina.accountexchanger.entity.Account;
import com.kutyrina.accountexchanger.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse getAccounts(Long accountId) {
        Account account = getAccountIfPresent(accountId);
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    public AccountResponse addNewAccount() {
        Account account = new Account();
        accountRepository.save(account);
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    public AccountResponse withdrawFromAccount(Long accountId, BigDecimal amount) {
        Account account = getAccountIfPresent(accountId);
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = balance.add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account balance less than amount to withdraw");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    public TransferMoneyResponse transferMoneyToAnotherUser(TransferMoneyRequest transferMoneyRequest) {
        Account account = getAccountIfPresent(transferMoneyRequest.getAccountFrom());
        BigDecimal balance = account.getBalance();
        if (balance.compareTo(transferMoneyRequest.getAmount()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account balance less than amount to withdraw");
        }
        BigDecimal newBalance = balance.subtract(transferMoneyRequest.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        Account transferToAccount = getAccountIfPresent(transferMoneyRequest.getAccountTo());
        BigDecimal transferToAccountBalance = transferToAccount.getBalance();
        BigDecimal newAccountBalance = transferToAccountBalance.add(transferMoneyRequest.getAmount());
        transferToAccount.setBalance(newAccountBalance);
        accountRepository.save(transferToAccount);
        return new TransferMoneyResponse(
                new AccountResponse(account.getAccountNumber(), account.getBalance()),
                new AccountResponse(transferToAccount.getAccountNumber(), transferToAccount.getBalance()));
    }

    private Account getAccountIfPresent(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
    }

}
