package com.lastminute.model;

import java.time.LocalDateTime;
import java.util.Optional;

public class PriceCalculationRequest
{
    private final String flightCode;
    private final LocalDateTime departureDate;
    private final int passengers;
    private final Currency currency;

    public PriceCalculationRequest(String flightCode, LocalDateTime departureDate, int passengers)
    {
        this(flightCode, departureDate, passengers, null);
    }

    public PriceCalculationRequest(String flightCode, LocalDateTime departureDate, int passengers, Currency currency)
    {
        this.flightCode = flightCode;
        this.departureDate = departureDate;
        this.passengers = passengers;
        this.currency = currency;
    }

    public String getFlightCode()
    {
        return flightCode;
    }

    public LocalDateTime getDepartureDate()
    {
        return departureDate;
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
