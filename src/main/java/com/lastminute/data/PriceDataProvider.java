package com.lastminute.data;

import java.math.BigDecimal;
import java.util.Optional;

public interface PriceDataProvider
{
    Optional<BigDecimal> getPriceForFlight(final String flightCode);
}
