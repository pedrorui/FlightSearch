package com.lastminute.services;

import com.lastminute.core.TimeProvider;
import com.lastminute.data.PriceDataProvider;
import com.lastminute.model.TicketPrice;
import com.lastminute.model.TicketPriceRequest;
import com.lastminute.validation.TicketPriceRequestValidator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

public class PriceCalculatorServiceTest
{
    private static final String FLIGHT_CODE = "TK2372";

    @Mock
    private TimeProvider timeProvider;

    @Mock
    private PriceDataProvider priceDataProvider;

    @Mock
    private TicketPriceRequestValidator validator;

    private PriceCalculatorService priceCalculatorService;

    @BeforeMethod
    public void setup()
    {
        MockitoAnnotations.initMocks(this);
        priceCalculatorService = new PriceCalculatorService(priceDataProvider, timeProvider, validator);
    }

    @Test
    public void singlePassengerMoreThan30DaysToDepartureSuccess()
    {
        LocalDateTime departureDate = LocalDateTime.of(2018, 2, 1, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2017, 12, 1, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(Optional.of(new BigDecimal(100)));

        TicketPriceRequest request = new TicketPriceRequest(FLIGHT_CODE, departureDate, 1);
        TicketPrice price = priceCalculatorService.calculateTicketPrice(request);

        assertThat(price.getAmount(), is(equalTo(BigDecimal.valueOf(8000, 2))));
    }

    @Test
    public void singlePassenger17DaysToDepartureSuccess()
    {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 13, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(Optional.of(new BigDecimal(157.6)));

        TicketPriceRequest request = new TicketPriceRequest(FLIGHT_CODE, departureDate, 1);
        TicketPrice price = priceCalculatorService.calculateTicketPrice(request);

        assertThat(price.getAmount(), is(equalTo(BigDecimal.valueOf(15760, 2))));
    }

    @Test
    public void threePassengers15DaysToDepartureSuccess()
    {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 15, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(Optional.of(new BigDecimal(250)));

        TicketPriceRequest request = new TicketPriceRequest(FLIGHT_CODE, departureDate, 3);
        TicketPrice price = priceCalculatorService.calculateTicketPrice(request);

        assertThat(price.getAmount(), is(equalTo(BigDecimal.valueOf(90000, 2))));
    }

    @Test
    public void twoPassengers2DaysToDepartureSuccess()
    {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 28, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(Optional.of(new BigDecimal(259)));

        TicketPriceRequest request = new TicketPriceRequest(FLIGHT_CODE, departureDate, 2);
        TicketPrice price = priceCalculatorService.calculateTicketPrice(request);

        assertThat(price.getAmount(), is(equalTo(BigDecimal.valueOf(77700, 2))));
    }
}
