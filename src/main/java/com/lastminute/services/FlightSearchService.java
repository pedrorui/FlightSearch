package com.lastminute.services;

import com.lastminute.core.Validator;
import com.lastminute.data.RouteDataProvider;
import com.lastminute.model.FlightPrice;
import com.lastminute.model.FlightSearchCriteria;
import com.lastminute.model.TicketPrice;
import com.lastminute.model.TicketPriceRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlightSearchService
{
    private static final Logger logger = Logger.getLogger(FlightSearchService.class.getName());

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

        List<String> flightCodes = routeDataProvider.getFlights(searchCriteria.getOrigin(), searchCriteria.getDestination());
        final List<FlightPrice> flightPrices = new ArrayList<>(flightCodes.size());

        flightCodes.forEach(flightCode -> {
            try
            {
                TicketPrice price = priceCalculatorService.calculateTicketPrice(new TicketPriceRequest(flightCode, searchCriteria.getDeparture(), searchCriteria.getPassengers()));
                flightPrices.add(new FlightPrice(flightCode, price));
            }
            catch (Exception exception)
            {
                logger.log(Level.SEVERE, "Unable to get price for flight: " + flightCode, exception);
            }
        });

        return flightPrices;
    }
}
