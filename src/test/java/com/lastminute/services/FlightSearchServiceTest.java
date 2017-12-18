package com.lastminute.services;

import com.lastminute.data.RouteDataProvider;
import com.lastminute.exceptions.PriceCalculationException;
import com.lastminute.model.Currency;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.TicketPrice;
import com.lastminute.model.TicketPriceRequest;
import com.lastminute.validation.FlightSearchCriteriaValidator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FlightSearchServiceTest
{
    @Mock
    private RouteDataProvider routeDataProvider;

    @Mock
    private PriceCalculatorService priceCalculatorService;

    @Mock
    private FlightSearchCriteriaValidator flightSearchCriteriaValidator;

    private FlightSearchService flightSearchService;

    @BeforeMethod
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        flightSearchService = new FlightSearchService(routeDataProvider, priceCalculatorService, flightSearchCriteriaValidator);
    }

    @Test
    public void searchFlightReturnRoutesAndPrices()
    {
        List<String> flightCodes = Arrays.asList("TK2372", "TK2659", "LH5909");
        when(routeDataProvider.getFlights(anyString(), anyString()))
            .thenReturn(flightCodes);

        TicketPrice[] ticketPrices = new TicketPrice[3];
        ticketPrices[0] = new TicketPrice(new BigDecimal(157.6), Currency.EURO);
        ticketPrices[1] = new TicketPrice(new BigDecimal(198.4), Currency.EURO);
        ticketPrices[2] = new TicketPrice(new BigDecimal(90.4), Currency.EURO);

        when(priceCalculatorService.calculateTicketPrice(any(TicketPriceRequest.class)))
            .thenReturn(ticketPrices[0])
            .thenReturn(ticketPrices[1])
            .thenReturn(ticketPrices[2]);

        FlightSearchCriteria searchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 1);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(searchCriteria);

        verify(priceCalculatorService, times(3)).calculateTicketPrice(any(TicketPriceRequest.class));

        assertThat(flightPrices, hasSize(3));
        assertThat(flightPrices, everyItem(hasProperty("price")));

        List<TicketPrice> actualTicketPrices = flightPrices.stream()
            .map(FlightPrice::getPrice)
            .collect(Collectors.toList());

        // https://stackoverflow.com/questions/21624592/hamcrest-compare-collections
        assertThat(actualTicketPrices, containsInAnyOrder(ticketPrices));
    }

    @Test
    public void searchFlightsNoRoutesAvailableReturnEmptyList()
    {
        when(routeDataProvider.getFlights(anyString(), anyString()))
            .thenReturn(Collections.emptyList());

        when(priceCalculatorService.calculateTicketPrice(any(TicketPriceRequest.class)))
            .thenReturn(new TicketPrice(BigDecimal.ZERO, Currency.EURO));

        FlightSearchCriteria searchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 1);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(searchCriteria);

        verify(priceCalculatorService, never()).calculateTicketPrice(any(TicketPriceRequest.class));

        assertThat(flightPrices, hasSize(0));
    }

    @Test
    public void searchFlightsNoPricesAvailableReturnEmptyList()
    {
        List<String> flightCodes = Arrays.asList("TK2372", "TK2659", "LH5909");
        when(routeDataProvider.getFlights(anyString(), anyString()))
            .thenReturn(flightCodes);

        when(priceCalculatorService.calculateTicketPrice(any(TicketPriceRequest.class))).thenThrow(new PriceCalculationException("No price available"));

        FlightSearchCriteria searchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 1);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(searchCriteria);

        verify(priceCalculatorService, times(3)).calculateTicketPrice(any(TicketPriceRequest.class));

        assertThat(flightPrices, hasSize(0));
    }
}