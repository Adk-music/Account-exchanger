package com.kutyrina.accountexchanger.dto;

public class TransferMoneyResponse {

    private AccountResponse accountFrom;
    private AccountResponse accountTo;

    public TransferMoneyResponse(AccountResponse accountFrom, AccountResponse accountTo) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
    }

    public TransferMoneyResponse() {
    }

    public AccountResponse getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(AccountResponse accountFrom) {
        this.accountFrom = accountFrom;
    }

    public AccountResponse getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(AccountResponse accountTo) {
        this.accountTo = accountTo;
    }

    @Override
    public String toString() {
        return "TransferMoneyResponse{" +
                "accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                '}';
    }
}
