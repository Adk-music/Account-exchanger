package com.kutyrina.accountexchanger.account.service;

import com.kutyrina.accountexchanger.dto.AccountResponse;
import com.kutyrina.accountexchanger.dto.TransferMoneyRequest;
import com.kutyrina.accountexchanger.dto.TransferMoneyResponse;
import com.kutyrina.accountexchanger.entity.Account;
import com.kutyrina.accountexchanger.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse getAccounts(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    public AccountResponse addNewAccount() {
        Account account = new Account();
        accountRepository.save(account);
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    @Transactional
    public AccountResponse withdrawFromAccount(Long accountId, BigDecimal amount) {
        Account account = getAccountIfPresentWithLock(accountId);
        BigDecimal balance = account.getBalance();
        BigDecimal newBalance = balance.add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account balance less than amount to withdraw");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
        return new AccountResponse(account.getAccountNumber(), account.getBalance());
    }

    @Transactional
    public TransferMoneyResponse transferMoneyToAnotherUser(TransferMoneyRequest transferMoneyRequest) {
        Long accountFrom = transferMoneyRequest.getAccountFrom();
        Long accountTo = transferMoneyRequest.getAccountTo();
        Account accountFirst;
        Account accountSecond;

        if (accountFrom > accountTo) {
            accountFirst = getAccountIfPresentWithLock(accountFrom);
            accountSecond = getAccountIfPresentWithLock(accountTo);
            return accountsTransferMoney(accountFirst, accountSecond, transferMoneyRequest.getAmount());
        } else {
            accountFirst = getAccountIfPresentWithLock(accountTo);
            accountSecond = getAccountIfPresentWithLock(accountFrom);
            return accountsTransferMoney(accountSecond, accountFirst, transferMoneyRequest.getAmount());
        }
    }

    private TransferMoneyResponse accountsTransferMoney(Account accountFrom, Account accountTo, BigDecimal amount) {
        BigDecimal balanceFrom = accountFrom.getBalance();
        BigDecimal balanceTo = accountTo.getBalance();

        if (balanceFrom.compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account balance less than amount to withdraw");
        }
        accountFrom.setBalance(balanceFrom.subtract(amount));
        accountTo.setBalance(balanceTo.add(amount));
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        return new TransferMoneyResponse(
                new AccountResponse(accountFrom.getAccountNumber(), accountFrom.getBalance()),
                new AccountResponse(accountTo.getAccountNumber(), accountTo.getBalance()));
    }

    private Account getAccountIfPresentWithLock(Long accountId) {
        return accountRepository.findByAccountNumber(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!"));
    }

}
