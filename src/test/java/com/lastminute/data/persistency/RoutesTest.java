package com.lastminute.data.persistency;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class RoutesTest
{
    private Routes routes;

    @BeforeClass
    public void setup()
    {
        routes = new Routes();
        routes.addRoute("BCN", "FRA", "FR7521")
            .addRoute("CPH", "FCO", "TK4667")
            .addRoute("CPH", "FCO", "U24631")
            .addRoute("BCN", "AMS", "U24985")
            .addRoute("AMS", "FCO", "BA7610");
    }

    @Test
    public void allRoutesLoaded()
    {
        assertThat(routes.size(), is(equalTo(3)));
    }

    @Test
    public void routeFromFromOriginHas1EdgeAndTwoFlights()
    {
        Routes.Edges edges = routes.getEdges("CPH");

        assertThat(edges.size(), is(equalTo(1)));
        assertThat(edges.getNames(), hasSize(1));
        assertThat(edges.getFlights("FCO"), hasSize(2));
        assertThat(edges.getFlights("FCO"), containsInAnyOrder("TK4667", "U24631"));
    }

    @Test
    public void routeFromOriginHas2EdgesEachWithOneFlight()
    {
        Routes.Edges edges = routes.getEdges("BCN");

        assertThat(edges.size(), is(equalTo(2)));
        assertThat(edges.getNames(), containsInAnyOrder("FRA", "AMS"));
        assertThat(edges.getFlights("FRA"), hasSize(1));
        assertThat(edges.getFlights("FRA"), contains("FR7521"));
        assertThat(edges.getFlights("AMS"), hasSize(1));
        assertThat(edges.getFlights("AMS"), contains("U24985"));
    }

    @Test
    public void routeFromOriginHas1EdgeWithOneFlight()
    {
        Routes.Edges edges = routes.getEdges("AMS");

        assertThat(edges.size(), is(equalTo(1)));
        assertThat(edges.getNames(), hasSize(1));
        assertThat(edges.getFlights("FCO"), hasSize(1));
        assertThat(edges.getFlights("FCO"), contains("BA7610"));
    }
}
