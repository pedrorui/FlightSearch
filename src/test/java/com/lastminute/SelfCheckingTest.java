package com.lastminute;

import com.lastminute.core.TimeProvider;
import com.lastminute.core.Validator;
import com.lastminute.data.persistency.CsvPriceDataProvider;
import com.lastminute.data.persistency.CsvRouteDataProvider;
import com.lastminute.validation.FlightSearchCriteriaValidator;
import com.lastminute.validation.TicketPriceRequestValidator;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.TicketPriceRequest;
import com.lastminute.services.FlightSearchService;
import com.lastminute.services.PriceCalculatorService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.lastminute.infrastructure.CsvFiles.records;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@Test(groups = {"selfChecking"})
public class SelfCheckingTest
{
    @Mock
    private TimeProvider timeProvider;

    private FlightSearchService flightSearchService;

    @BeforeClass
    public void setup()
    {
        MockitoAnnotations.initMocks(this);

        when(timeProvider.getCurrentDateTime()).thenReturn(LocalDateTime.of(2017, 12, 17, 15, 39));

        CsvPriceDataProvider priceDataProvider = new CsvPriceDataProvider(records(fullPathTo("flight-prices.csv")));
        CsvRouteDataProvider routeDataProvider = new CsvRouteDataProvider(records(fullPathTo("flight-routes.csv")));

        Validator<TicketPriceRequest> priceCalculationRequestValidator = new TicketPriceRequestValidator();
        Validator<FlightSearchCriteria> flightSearchCriteriaValidator = new FlightSearchCriteriaValidator();

        PriceCalculatorService priceCalculatorService = new PriceCalculatorService(priceDataProvider, timeProvider, priceCalculationRequestValidator);
        flightSearchService = new FlightSearchService(routeDataProvider, priceCalculatorService, flightSearchCriteriaValidator);
    }

    @Test
    public void flightSearchFor1Passenger31DaysToDepartureReturnSuccess()
    {
        LocalDateTime departure = LocalDateTime.of(2018, 1, 30, 17, 30);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(new FlightSearchCriteria("AMS", "FRA", departure, 1));

        assertThat(flightPrices, hasSize(3));
        assertThat(flightPrices, everyItem(hasProperty("price")));
        assertThat(flightPrices.get(0).getPrice().getAmount(), is(equalTo(BigDecimal.valueOf(15760, 2))));
    }

    @Test
    public void flightSearchFor3Passengers15DaysToDepartureReturnSuccess()
    {
        LocalDateTime departure = LocalDateTime.of(2018, 1, 1, 18, 30);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(new FlightSearchCriteria("LHR", "IST", departure, 3));

        assertThat(flightPrices, hasSize(2));
        assertThat(flightPrices, everyItem(hasProperty("price")));
        assertThat(flightPrices.get(0).getPrice().getAmount(), is(equalTo(BigDecimal.valueOf(90000, 2))));
    }

    @Test
    public void flightSearchFor2Passengers2DaysToDepartureReturnsSuccess()
    {
        LocalDateTime departure = LocalDateTime.of(2017, 12, 19, 17, 30);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(new FlightSearchCriteria("BCN", "MAD", departure, 2));

        assertThat(flightPrices, hasSize(2));
        assertThat(flightPrices, everyItem(hasProperty("price")));
        assertThat(flightPrices.get(0).getPrice().getAmount(), is(equalTo(BigDecimal.valueOf(77700, 2))));
    }

    @Test
    public void flightSearchFor2PassengersNoFlightsAvailable()
    {
        LocalDateTime departure = LocalDateTime.of(2018, 1, 30, 17, 30);
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(new FlightSearchCriteria("CDG", "FRA", departure, 2));

        assertThat(flightPrices, hasSize(0));
    }

    private String fullPathTo(String fileName)
    {
        return getClass().getClassLoader().getResource(fileName).getPath();
    }
}
