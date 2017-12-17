package com.lastminute.services;

import com.lastminute.data.RouteDataProvider;
import com.lastminute.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.beans.HasProperty.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlightSearchServiceTest
{
    @Mock
    private RouteDataProvider routeDataProvider;

    @Mock
    private PriceCalculatorService priceCalculatorService;

    private FlightSearchService flightSearchService;

    @Before
    public void setup()
    {
        flightSearchService = new FlightSearchService(routeDataProvider, priceCalculatorService);
    }

    @Test
    public void searchFlightReturnRoutesAndPricesSuccess()
    {
        List<String> flightCodes = Arrays.asList("TK2372", "TK2659", "LH5909");
        when(routeDataProvider.getRoutes(anyString(), anyString()))
                .thenReturn(flightCodes);

        when(priceCalculatorService.calculate(any(PriceCalculationRequest.class)))
                .thenReturn(new Money(new BigDecimal(157.60), Currency.EURO))
                .thenReturn(new Money(new BigDecimal(198.40), Currency.EURO))
                .thenReturn(new Money(new BigDecimal(90.40), Currency.EURO));

        FlightSearchCriteria searchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 1);
        List<FlightPrice> flightPrices = flightSearchService.search(searchCriteria);

        verify(priceCalculatorService, times(3)).calculate(any(PriceCalculationRequest.class));

        assertThat(flightPrices, hasSize(3));
        assertThat(flightPrices, everyItem(hasProperty("price")));
    }
}