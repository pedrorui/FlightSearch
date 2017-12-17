package com.lastminute.model;

public class FlightPrice
{
    private final String flightCode;
    private final TicketPrice price;

    public FlightPrice(String flightCode, TicketPrice price)
    {
        this.flightCode = flightCode;
        this.price = price;
    }

    public String getFlightCode()
    {
        return flightCode;
    }

    public TicketPrice getPrice()
    {
        return price;
    }
}
