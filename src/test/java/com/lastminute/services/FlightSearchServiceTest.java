package com.lastminute.services;

import com.lastminute.data.RouteDataProvider;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void searchFlightReturnRoutesAndPricesSuccess()
    {
        List<String> flightCodes = Arrays.asList("TK2372", "TK2659", "LH5909");
        when(routeDataProvider.getRoutes(anyString(), anyString()))
            .thenReturn(flightCodes);

        when(priceCalculatorService.calculateTicketPrice(any(TicketPriceRequest.class)))
            .thenReturn(new TicketPrice(new BigDecimal(157.60), Currency.EURO))
            .thenReturn(new TicketPrice(new BigDecimal(198.40), Currency.EURO))
            .thenReturn(new TicketPrice(new BigDecimal(90.40), Currency.EURO));

        FlightSearchCriteria searchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 1);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(searchCriteria);

        verify(priceCalculatorService, times(3)).calculateTicketPrice(any(TicketPriceRequest.class));

        assertThat(flightPrices, hasSize(3));
        assertThat(flightPrices, everyItem(hasProperty("price")));
    }
}