package com.lastminute.services;

import com.lastminute.core.TimeProvider;
import com.lastminute.data.PriceDataProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NavigableMap;
import java.util.TreeMap;

public class PriceCalculatorService {
    private static final NavigableMap<Long, BigDecimal> DAYS_TO_FLIGHT_PRICE_MULTIPLIERS = new TreeMap<>();

    private final TimeProvider timeProvider;
    private final PriceDataProvider priceDataProvider;

    static {
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(0L, new BigDecimal(1.5)); // 0..2
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(3L, new BigDecimal(1.2)); // 3..15
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(16L, new BigDecimal(1)); //16..30
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(31L, new BigDecimal(0.8)); // > 30
    }

    public PriceCalculatorService(TimeProvider timeProvider, PriceDataProvider priceDataProvider) {
        this.timeProvider = timeProvider;
        this.priceDataProvider = priceDataProvider;
    }

    public BigDecimal getPrice(String flightCode, int passengers, LocalDateTime departureDate) {
        if (passengers < 1) {
            throw new IllegalArgumentException("The number of passengers must be greater than 0");
        }

        long daysToFlight = ChronoUnit.DAYS.between(timeProvider.getCurrentDateTime(), departureDate);

        if (daysToFlight < 0) {
            throw new IllegalArgumentException("Departure data cannot be in the past");
        }

        return calculateFlightPrice(flightCode, passengers, daysToFlight);
    }

    private BigDecimal calculateFlightPrice(String flightCode, int passengers, long daysToFlight) {
        BigDecimal priceMultiplier = DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.floorEntry(daysToFlight).getValue();
        BigDecimal basePrice = priceDataProvider.getPriceForFlight(flightCode);

        return basePrice.multiply(priceMultiplier).multiply(new BigDecimal(passengers)).setScale(2, RoundingMode.HALF_UP);
    }
}
