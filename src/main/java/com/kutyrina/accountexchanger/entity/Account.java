package com.kutyrina.accountexchanger.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * под вопросом? Работа с аккаунтом клиента.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private Client client;

    public Account() {

    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAllAccountInfo() {
        return getAccountNumber() + " " + getBalance();
    }
}
