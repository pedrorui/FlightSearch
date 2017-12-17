package com.lastminute.services;

import com.lastminute.core.TimeProvider;
import com.lastminute.core.Validator;
import com.lastminute.data.PriceDataProvider;
import com.lastminute.exceptions.PriceCalculationException;
import com.lastminute.model.Currency;
import com.lastminute.model.TicketPrice;
import com.lastminute.model.TicketPriceRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public class PriceCalculatorService
{
    private static final NavigableMap<Long, BigDecimal> DAYS_TO_FLIGHT_PRICE_MULTIPLIERS = new TreeMap<>();

    private final PriceDataProvider priceDataProvider;
    private final TimeProvider timeProvider;
    private final Validator<TicketPriceRequest> priceCalculationRequestValidator;

    static
    {
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(0L, new BigDecimal(1.5));  // 0..2
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(3L, new BigDecimal(1.2));  // 3..15
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(16L, BigDecimal.ONE);      // 16..30
        DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.put(31L, new BigDecimal(0.8)); // > 30
    }

    public PriceCalculatorService(PriceDataProvider priceDataProvider, TimeProvider timeProvider, Validator<TicketPriceRequest> priceCalculationRequestValidator)
    {
        this.priceDataProvider = priceDataProvider;
        this.timeProvider = timeProvider;
        this.priceCalculationRequestValidator = priceCalculationRequestValidator;
    }

    public TicketPrice calculateTicketPrice(TicketPriceRequest request)
    {
        priceCalculationRequestValidator.validate(request);

        Optional<BigDecimal> basePrice = priceDataProvider.getPriceForFlight(request.getFlightCode());
        if (!basePrice.isPresent())
        {
            throw new PriceCalculationException("Unable to find a base price for the flight");
        }

        long daysToFLight = ChronoUnit.DAYS.between(timeProvider.getCurrentDateTime(), request.getDeparture());
        if (daysToFLight < 0)
        {
            throw new PriceCalculationException("Unable to calculate a price for a departure date in the past");
        }

        BigDecimal totalPrice = calculateTotalPrice(basePrice.get(), daysToFLight, request.getPassengers());
        return new TicketPrice(totalPrice, request.getCurrency().orElse(Currency.EURO));
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, long daysToFlight, int passengers)
    {
        BigDecimal priceMultiplier = DAYS_TO_FLIGHT_PRICE_MULTIPLIERS.floorEntry(daysToFlight).getValue();
        return basePrice.multiply(priceMultiplier).multiply(new BigDecimal(passengers)).setScale(2, RoundingMode.HALF_UP);
    }
}