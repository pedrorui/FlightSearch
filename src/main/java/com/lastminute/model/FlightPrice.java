package com.lastminute.model;

import java.math.BigDecimal;

public class FlightPrice {
    private final String flightCode;
    private final BigDecimal price;

    public FlightPrice(String flightCode, BigDecimal price) {
        this.flightCode = flightCode;
        this.price = price;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
