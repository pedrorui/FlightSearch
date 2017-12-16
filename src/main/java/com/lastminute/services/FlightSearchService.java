package com.lastminute.services;

import com.lastminute.data.RouteDataProvider;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.Money;
import com.lastminute.model.PriceCalculationRequest;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchService
{
    private final RouteDataProvider routeDataProvider;
    private final PriceCalculatorService priceCalculatorService;

    public FlightSearchService(RouteDataProvider routeDataProvider, PriceCalculatorService priceCalculatorService)
    {
        this.routeDataProvider = routeDataProvider;
        this.priceCalculatorService = priceCalculatorService;
    }

    public List<FlightPrice> search(FlightSearchCriteria searchCriteria)
    {
        List<String> flightCodes = routeDataProvider.getRoutes(searchCriteria.getOrigin(), searchCriteria.getDestination());
        List<FlightPrice> flightPrices = new ArrayList<>(flightCodes.size());

        for (String flightCode : flightCodes)
        {
            Money price = priceCalculatorService.calculate(new PriceCalculationRequest(flightCode, searchCriteria.getDeparture(), searchCriteria.getPassengers()));
            flightPrices.add(new FlightPrice(flightCode, price));
        }

        return flightPrices;
    }
}
