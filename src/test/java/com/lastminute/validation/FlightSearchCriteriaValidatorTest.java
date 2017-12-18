package com.lastminute.validation;

import com.lastminute.model.FlightSearchCriteria;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FlightSearchCriteriaValidatorTest
{
    private FlightSearchCriteriaValidator validator;

    @BeforeClass
    public void setup()
    {
        validator = new FlightSearchCriteriaValidator();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidNumberOfPassengersThrowsException()
    {
        validator.validate(new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 0));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidDepartureDateThrowsException()
    {
        validator.validate(new FlightSearchCriteria("AMS", "FRA", null, 2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidFlightOriginThrowsException()
    {
        validator.validate(new FlightSearchCriteria(null, "FRA", LocalDateTime.now(), 2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void invalidFlightDestinationThrowsException()
    {
        validator.validate(new FlightSearchCriteria(null, "FRA", LocalDateTime.now(), 2));
    }

    @Test
    public void validSearchCriteriaValidatedByAllRules()
    {
        FlightSearchCriteriaValidator spy = spy(validator);

        FlightSearchCriteria flightSearchCriteria = new FlightSearchCriteria("AMS", "FRA", LocalDateTime.now(), 2);
        spy.validate(flightSearchCriteria);

        verify(spy).validate(flightSearchCriteria);
    }
}
