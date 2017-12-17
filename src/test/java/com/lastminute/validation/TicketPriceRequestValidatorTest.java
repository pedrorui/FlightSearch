package com.lastminute.validation;

import com.lastminute.model.TicketPriceRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

public class TicketPriceRequestValidatorTest
{
    private TicketPriceRequestValidator validator;

    @BeforeClass
    public void setup()
    {
        validator = new TicketPriceRequestValidator();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidNumberOfPassengersThrowsException()
    {
        validator.validate(new TicketPriceRequest("TK2372", LocalDateTime.now(), 0));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void departureDateNullThrowsException()
    {
        validator.validate(new TicketPriceRequest("LH5909", null, 2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void flightCodeNullThrowsException()
    {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        validator.validate(new TicketPriceRequest(null, departureDate, 2));
    }
}
