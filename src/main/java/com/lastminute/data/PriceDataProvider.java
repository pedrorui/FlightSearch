package com.lastminute.data;

import java.math.BigDecimal;

public interface PriceDataProvider {
   BigDecimal getPriceForFlight(final String flightCode);
}
