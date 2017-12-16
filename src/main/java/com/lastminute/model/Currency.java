package com.lastminute.model;

public enum Currency
{
    EURO("Eur", "Euro", "€");

    private final String code;
    private final String name;
    private final String symbol;

    Currency(String code, String name, String symbol)
    {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getSymbol()
    {
        return symbol;
    }
}
