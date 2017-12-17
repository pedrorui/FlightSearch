package com.lastminute.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicketPriceRequest
{
    private final String flightCode;
    private final LocalDateTime departure;
    private final int passengers;
    private final Currency currency;

    public TicketPriceRequest(String flightCode, LocalDateTime departureDate, int passengers)
    {
        this(flightCode, departureDate, passengers, null);
    }

    public TicketPriceRequest(String flightCode, LocalDateTime departure, int passengers, Currency currency)
    {
        this.flightCode = flightCode;
        this.departure = departure;
        this.passengers = passengers;
        this.currency = currency;
    }

    public String getFlightCode()
    {
        return flightCode;
    }

    public LocalDateTime getDeparture()
    {
        return departure;
    }

    public int getPassengers()
    {
        return passengers;
    }

    public Optional<Currency> getCurrency()
    {
        return Optional.ofNullable(currency);
    }
}
