package com.lastminute.services;

import com.lastminute.core.Validator;
import com.lastminute.data.RouteDataProvider;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.TicketPrice;
import com.lastminute.model.TicketPriceRequest;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchService
{
    private final RouteDataProvider routeDataProvider;
    private final PriceCalculatorService priceCalculatorService;
    private final Validator<FlightSearchCriteria> flightSearchCriteriaValidator;

    public FlightSearchService(RouteDataProvider routeDataProvider, PriceCalculatorService priceCalculatorService, Validator<FlightSearchCriteria> flightSearchCriteriaValidator)
    {
        this.routeDataProvider = routeDataProvider;
        this.priceCalculatorService = priceCalculatorService;
        this.flightSearchCriteriaValidator = flightSearchCriteriaValidator;
    }

    public List<FlightPrice> searchFlightPrices(FlightSearchCriteria searchCriteria)
    {
        flightSearchCriteriaValidator.validate(searchCriteria);

        List<String> flightCodes = routeDataProvider.getRoutes(searchCriteria.getOrigin(), searchCriteria.getDestination());
        List<FlightPrice> flightPrices = new ArrayList<>(flightCodes.size());

        for (String flightCode : flightCodes)
        {
            TicketPrice price = priceCalculatorService.calculateTicketPrice(new TicketPriceRequest(flightCode, searchCriteria.getDeparture(), searchCriteria.getPassengers()));
            flightPrices.add(new FlightPrice(flightCode, price));
        }

        return flightPrices;
    }
}
