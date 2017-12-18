package com.lastminute.data.persistency;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class CsvRouteDataProviderTest
{
    private CsvRouteDataProvider routeDataProvider;

    @BeforeClass
    public void setup()
    {
        Stream<List<String>> contentStream = Stream.of(
            Arrays.asList("BCN", "FRA", "FR7521"),
            Arrays.asList("CPH", "FCO", "TK4667"),
            Arrays.asList("CPH", "FCO", "U24631"),
            Arrays.asList("BCN", "AMS", "U24985"),
            Arrays.asList("AMS", "FCO", "BA7610"));

        routeDataProvider = new CsvRouteDataProvider(contentStream);
    }

    @Test
    public void originWithSingleDestinationHas2Flights()
    {
        List<String> flights = routeDataProvider.getFlights("CPH", "FCO");

        assertThat(flights, hasSize(2));
        assertThat(flights, containsInAnyOrder("TK4667", "U24631"));
    }

    @Test
    public void routeWithTwoDestinationsHas2FlightsOneForEachDestination()
    {
        List<String> flights = routeDataProvider.getFlights("BCN", "FRA");

        assertThat(flights, hasSize(1));
        assertThat(flights, contains("FR7521"));

        flights = routeDataProvider.getFlights("BCN", "AMS");

        assertThat(flights, hasSize(1));
        assertThat(flights, contains("U24985"));
    }

    @Test
    public void routeWithSingleDestinationHas1Flight()
    {
        List<String> flights = routeDataProvider.getFlights("AMS", "FCO");

        assertThat(flights, hasSize(1));
        assertThat(flights, contains("BA7610"));
    }
}
