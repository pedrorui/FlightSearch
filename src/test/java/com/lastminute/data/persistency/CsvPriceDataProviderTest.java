package com.lastminute.data.persistency;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CsvPriceDataProviderTest
{
    private CsvPriceDataProvider priceDataProvider;

    @BeforeClass
    public void setup()
    {
        Stream<List<String>> contentStream = Stream.of(
            Arrays.asList("IB2818", "186"),
            Arrays.asList("U23631", "152"),
            Arrays.asList("IB8482", "295"),
            Arrays.asList("FR7521", "150"),
            Arrays.asList("TK4667", "137"),
            Arrays.asList("U24631", "268.9"));

        priceDataProvider = new CsvPriceDataProvider(contentStream);
    }

    @Test
    public void getPriceForKnownFlightReturnOptionalNotEmpty()
    {
        Optional<BigDecimal> flightPrice = priceDataProvider.getPriceForFlight("FR7521");

        assertTrue(flightPrice.isPresent());
        assertThat(flightPrice.get(), is(equalTo(new BigDecimal("150"))));
    }

    @Test
    public void getPriceForFirstFlightAdded()
    {
        Optional<BigDecimal> flightPrice = priceDataProvider.getPriceForFlight("IB2818");

        assertTrue(flightPrice.isPresent());
        assertThat(flightPrice.get(), is(equalTo(new BigDecimal("186"))));
    }

    @Test
    public void getPriceForLastFlightAdded()
    {
        Optional<BigDecimal> flightPrice = priceDataProvider.getPriceForFlight("U24631");

        assertTrue(flightPrice.isPresent());
        assertThat(flightPrice.get(), is(equalTo(new BigDecimal("268.9"))));
    }

    @Test
    public void getPriceForUnknownFlightReturnOptionalEmpty()
    {
        Optional<BigDecimal> flightPrice = priceDataProvider.getPriceForFlight("JK3456");

        assertFalse(flightPrice.isPresent());
    }
}
