package com.lastminute;

import com.lastminute.core.DefaultTimeProvider;
import com.lastminute.core.TimeProvider;
import com.lastminute.data.persistency.CsvPriceDataProvider;
import com.lastminute.data.persistency.CsvRouteDataProvider;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.services.FlightSearchService;
import com.lastminute.services.PriceCalculatorService;

import java.util.List;

import static com.lastminute.infrastructure.CsvFiles.records;

public class FlightSearch
{
    public static void main(String[] args)
    {
        FlightSearch flightSearch = new FlightSearch();
        flightSearch.run();
    }

    private void run()
    {
        CsvPriceDataProvider priceDataProvider = new CsvPriceDataProvider(records(fullPathTo("flight-prices.csv")));
        CsvRouteDataProvider routeDataProvider = new CsvRouteDataProvider(records(fullPathTo("flight-routes.csv")));

        TimeProvider timeProvider = new DefaultTimeProvider();

        PriceCalculatorService priceCalculatorService = new PriceCalculatorService(priceDataProvider, timeProvider);
        FlightSearchService flightSearchService = new FlightSearchService(routeDataProvider, priceCalculatorService);

        List<FlightPrice> flightPrices = flightSearchService.search(new FlightSearchCriteria("AMS", "FRA", timeProvider.getCurrentDateTime().plusDays(31), 1));
        for (FlightPrice flightPrice : flightPrices)
        {
            System.out.println(flightPrice);
        }
    }

    private String fullPathTo(String fileName)
    {
        return getClass().getClassLoader().getResource(fileName).getPath();
    }
}
