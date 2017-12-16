package com.lastminute.model;

import java.math.BigDecimal;

public class Money
{
    private final BigDecimal amount;
    private final Currency currency;

    public Money(BigDecimal amount, Currency currency)
    {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public Currency getCurrency()
    {
        return currency;
    }
}
