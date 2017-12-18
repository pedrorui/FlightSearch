package com.lastminute.data;

import java.util.List;

public interface RouteDataProvider
{
    List<String> getFlights(String origin, String destination);
}
