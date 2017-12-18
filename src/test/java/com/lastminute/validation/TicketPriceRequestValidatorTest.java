package com.lastminute.validation;

import com.lastminute.model.TicketPriceRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

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
        validator.validate(new TicketPriceRequest(null, LocalDateTime.now(), 2));
    }

    @Test
    public void validCalculationRequestValidatedByAllRules()
    {
        TicketPriceRequestValidator spy = spy(validator);

        TicketPriceRequest ticketPriceRequest = new TicketPriceRequest("LH5909", LocalDateTime.now(), 2);
        spy.validate(ticketPriceRequest);

        verify(spy).validate(ticketPriceRequest);
    }
}
