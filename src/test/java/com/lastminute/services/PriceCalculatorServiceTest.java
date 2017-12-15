package com.lastminute.services;

import com.lastminute.core.TimeProvider;
import com.lastminute.data.PriceDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceCalculatorServiceTest {
    private static final String FLIGHT_CODE = "TK2372";

    @Mock
    private TimeProvider timeProvider;

    @Mock
    private PriceDataProvider priceDataProvider;

    private PriceCalculatorService priceCalculatorService;

    @Before
    public void setup() {
        priceCalculatorService = new PriceCalculatorService(timeProvider, priceDataProvider);
    }

    @Test
    public void singlePassengerMoreThan30DaysToDepartureSuccess() {
        LocalDateTime departureDate = LocalDateTime.of(2018, 2, 1, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2017, 12, 1, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(new BigDecimal(100));

        BigDecimal price = priceCalculatorService.getPrice(FLIGHT_CODE, 1, departureDate);

        assertThat(price, is(equalTo(BigDecimal.valueOf(8000, 2))));
    }

    @Test
    public void singlePassenger17DaysToDepartureSuccess() {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 13, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(new BigDecimal(157.6));

        BigDecimal price = priceCalculatorService.getPrice(FLIGHT_CODE, 1, departureDate);

        assertThat(price, is(equalTo(BigDecimal.valueOf(15760, 2))));
    }

    @Test
    public void threePassengers15DaysToDepartureSuccess() {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 15, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(new BigDecimal(250));

        BigDecimal price = priceCalculatorService.getPrice(FLIGHT_CODE, 3, departureDate);

        assertThat(price, is(equalTo(BigDecimal.valueOf(90000, 2))));
    }

    @Test
    public void twoPassengers2DaysToDepartureSuccess() {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 30, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 28, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);
        when(priceDataProvider.getPriceForFlight(FLIGHT_CODE)).thenReturn(new BigDecimal(259));

        BigDecimal price = priceCalculatorService.getPrice(FLIGHT_CODE, 2, departureDate);

        assertThat(price, is(equalTo(BigDecimal.valueOf(77700, 2))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidNumberOfPassengersThrowsException() {
        priceCalculatorService.getPrice(FLIGHT_CODE, 0, LocalDateTime.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void departureDateInThePastThrowsException() {
        LocalDateTime departureDate = LocalDateTime.of(2018, 1, 14, 6, 0);
        LocalDateTime currentDate = LocalDateTime.of(2018, 1, 15, 6, 0);

        when(timeProvider.getCurrentDateTime()).thenReturn(currentDate);

        priceCalculatorService.getPrice(FLIGHT_CODE, 2, departureDate);
    }
}
