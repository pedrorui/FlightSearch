package com.lastminute.validation;

import com.lastminute.core.BaseValidator;
import com.lastminute.model.TicketPriceRequest;

public class TicketPriceRequestValidator extends BaseValidator<TicketPriceRequest>
{
    public TicketPriceRequestValidator()
    {
       addRule(target -> target.getFlightCode() == null || target.getFlightCode().isEmpty(), () -> new IllegalArgumentException("Flight must be supplied"));
       addRule(target -> target.getPassengers() < 1, () -> new IllegalArgumentException("The number of passengers must be greater than 0"));
       addRule(target -> target.getDeparture() == null, () -> new IllegalArgumentException("Departure data must be supplied"));
    }
}
