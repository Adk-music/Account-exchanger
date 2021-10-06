package com.kutyrina.accountexchanger.dto;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private BigDecimal balance;

    public AccountResponse(Long id, BigDecimal balance) {
        this.id = id;
        this.balance = balance;
    }

    public AccountResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
