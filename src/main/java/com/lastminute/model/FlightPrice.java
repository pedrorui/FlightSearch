package com.lastminute.model;

public class FlightPrice
{
    private final String flightCode;
    private final Money price;

    public FlightPrice(String flightCode, Money price)
    {
        this.flightCode = flightCode;
        this.price = price;
    }

    public String getFlightCode()
    {
        return flightCode;
    }

    public Money getPrice()
    {
        return price;
    }
}
