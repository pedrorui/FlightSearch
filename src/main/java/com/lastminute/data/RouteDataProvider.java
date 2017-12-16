package com.lastminute.data;

import java.util.List;

public interface RouteDataProvider
{
    List<String> getRoutes(String origin, String destination);
}
