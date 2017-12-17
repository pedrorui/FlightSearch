package com.lastminute.validation;

import com.lastminute.core.BaseValidator;
import com.lastminute.model.FlightSearchCriteria;

public class FlightSearchCriteriaValidator extends BaseValidator<FlightSearchCriteria>
{
    public FlightSearchCriteriaValidator()
    {
        addRule(target -> target.getOrigin() == null || target.getOrigin().isEmpty(), () -> new IllegalArgumentException("Origin must be supplied"));
        addRule(target -> target.getDestination() == null || target.getDestination().isEmpty(), () -> new IllegalArgumentException("Destination must be supplied"));
        addRule(target -> target.getPassengers() < 1, () -> new IllegalArgumentException("The number of passengers must be greater than 0"));
        addRule(target -> target.getDeparture() == null, () -> new IllegalArgumentException("Departure data must be supplied"));
    }
}
