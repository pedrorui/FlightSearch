package com.lastminute.model;

import java.time.LocalDateTime;

public class FlightSearchCriteria
{
    private final String origin;
    private final String destination;
    private final LocalDateTime departure;
    private final int passengers;

    public FlightSearchCriteria(String origin, String destination, LocalDateTime departure, int passengers)
    {
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.passengers = passengers;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getDestination()
    {
        return destination;
    }

    public LocalDateTime getDeparture()
    {
        return departure;
    }

    public int getPassengers()
    {
        return passengers;
    }
}
