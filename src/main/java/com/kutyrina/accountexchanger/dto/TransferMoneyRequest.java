package com.kutyrina.accountexchanger.dto;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class TransferMoneyRequest {

    private Long accountFrom;
    private Long accountTo;
    @DecimalMin("0.00")
    private BigDecimal amount;

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
