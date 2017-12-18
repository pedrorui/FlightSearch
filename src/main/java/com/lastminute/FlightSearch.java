package com.lastminute;

import com.lastminute.core.DefaultTimeProvider;
import com.lastminute.core.TimeProvider;
import com.lastminute.core.Validator;
import com.lastminute.data.PriceDataProvider;
import com.lastminute.data.RouteDataProvider;
import com.lastminute.data.persistency.CsvPriceDataProvider;
import com.lastminute.data.persistency.CsvRouteDataProvider;
import com.lastminute.infrastructure.ResourceLocator;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.TicketPriceRequest;
import com.lastminute.services.FlightSearchService;
import com.lastminute.services.PriceCalculatorService;
import com.lastminute.validation.FlightSearchCriteriaValidator;
import com.lastminute.validation.TicketPriceRequestValidator;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.lastminute.infrastructure.CsvFiles.records;

public class FlightSearch
{
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    private final TimeProvider timeProvider;
    private final FlightSearchService flightSearchService;

    public static void main(String[] args)
    {
        FlightSearch flightSearch = new FlightSearch();
        flightSearch.demo();
    }

    FlightSearch()
    {
        ResourceLocator resourceLocator = new ResourceLocator();

        PriceDataProvider priceDataProvider = new CsvPriceDataProvider(records(resourceLocator.fullPathTo("flight-prices.csv")));
        RouteDataProvider routeDataProvider = new CsvRouteDataProvider(records(resourceLocator.fullPathTo("flight-routes.csv")));

        Validator<TicketPriceRequest> priceCalculationRequestValidator = new TicketPriceRequestValidator();
        Validator<FlightSearchCriteria> flightSearchCriteriaValidator = new FlightSearchCriteriaValidator();

        timeProvider = new DefaultTimeProvider();

        PriceCalculatorService priceCalculatorService = new PriceCalculatorService(priceDataProvider, timeProvider, priceCalculationRequestValidator);

        flightSearchService = new FlightSearchService(routeDataProvider, priceCalculatorService, flightSearchCriteriaValidator);
    }

    void demo()
    {
        printSearchResults(new FlightSearchCriteria("BCN", "MAD", timeProvider.getCurrentDateTime().plusDays(15), 4));
        printSearchResults(new FlightSearchCriteria("AMS", "FRA", timeProvider.getCurrentDateTime().plusDays(35), 2));
        printSearchResults(new FlightSearchCriteria("CGG", "FRA", timeProvider.getCurrentDateTime().plusDays(1), 1));
    }

    private void printSearchResults(FlightSearchCriteria searchCriteria)
    {
        List<FlightPrice> flightPrices = flightSearchService.searchFlightPrices(searchCriteria);

        System.out.println(String.format("Flights from %s to %s, for %d passengers, departing %s:",
            searchCriteria.getOrigin(),
            searchCriteria.getDestination(),
            searchCriteria.getPassengers(),
            searchCriteria.getDeparture().format(dateTimeFormatter))
        );

        if (flightPrices.isEmpty())
        {
            System.out.println("No flights available");
        }
        else
        {
            flightPrices.forEach(flightPrice -> System.out.println(String.format("* %s, %.2f %s",
                flightPrice.getFlightCode(),
                flightPrice.getPrice().getAmount(),
                flightPrice.getPrice().getCurrency().getSymbol()))
            );
        }
    }
}
