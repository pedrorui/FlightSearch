package com.lastminute.exceptions;

public class PriceCalculationException extends RuntimeException
{
    public PriceCalculationException(String message)
    {
        super(message);
    }
}
