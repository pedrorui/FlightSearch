package com.lastminute.services;

import com.lastminute.core.TimeProvider;
import com.lastminute.data.PriceDataProvider;
import com.lastminute.model.Currency;
import com.lastminute.model.Money;
import com.lastminute.model.PriceCalculationRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.NavigableMap;
import java.util.TreeMap;

public class PriceCalculatorService
{
    private static final NavigableMap<Long, BigDecimal> DAYS_TO_FLIGHT_PRICE_MULTIPLIERS = new TreeMap<>();

    private final TimeProvider timeProvider;
    private final PriceDataProvider priceDataProvider;

    static
    {
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(0L, new BigDecimal(1.5));  // 0..2
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(3L, new BigDecimal(1.2));  // 3..15
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(16L, BigDecimal.ONE);      //16..30
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(31L, new BigDecimal(0.8)); // > 30
    }

    public PriceCalculatorService(TimeProvider timeProvider, PriceDataProvider priceDataProvider)
    {
        this.timeProvider = timeProvider;
        this.priceDataProvider = priceDataProvider;
    }

    public Money calculate(PriceCalculationRequest request)
    {
        validateRequest(request);

        BigDecimal totalPrice = calculateTotalPrice(request);

        return new Money(totalPrice, request.getCurrency().orElse(Currency.EURO));
    }

    private BigDecimal calculateTotalPrice(PriceCalculationRequest request)
    {
        long daysToFlight = ChronoUnit.DAYS.between(timeProvider.getCurrentDateTime(), request.getDepartureDate());

        BigDecimal basePrice = priceDataProvider.getPriceForFlight(request.getFlightCode());
        BigDecimal priceMultiplier = DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.floorEntry(daysToFlight).getValue();

        return basePrice.multiply(priceMultiplier).multiply(new BigDecimal(request.getPassengers())).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateRequest(PriceCalculationRequest request)
    {
        if (request.getFlightCode() == null || request.getFlightCode().isEmpty())
        {
            throw new IllegalArgumentException("Flight code must be supplied");
        }

        if (request.getPassengers() < 1)
        {
            throw new IllegalArgumentException("The number of passengers must be greater than 0");
        }

        if (request.getDepartureDate() == null)
        {
            throw new IllegalArgumentException("Departure data must be supplied");
        }

        if (ChronoUnit.DAYS.between(timeProvider.getCurrentDateTime(), request.getDepartureDate()) < 0)
        {
            throw new IllegalArgumentException("Departure data cannot be in the past");
        }
    }
}