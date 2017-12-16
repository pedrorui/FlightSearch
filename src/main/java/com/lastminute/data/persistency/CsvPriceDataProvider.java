package com.lastminute.data.persistency;

import com.lastminute.data.PriceDataProvider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CsvPriceDataProvider implements PriceDataProvider
{
    private final Map<String, BigDecimal> flightPrices = new HashMap<>();

    public CsvPriceDataProvider(Stream<List<String>> source)
    {
        source.forEach(item -> flightPrices.put(item.get(0), new BigDecimal(item.get(1))));
    }

    @Override
    public BigDecimal getPriceForFlight(String flightCode)
    {
        return flightPrices.get(flightCode);
    }
}
