package com.lastminute.data.persistency;

import com.lastminute.data.RouteDataProvider;

import java.util.List;
import java.util.stream.Stream;

public class CsvRouteDataProvider implements RouteDataProvider
{
    private final Routes routes = new Routes();

    public CsvRouteDataProvider(Stream<List<String>> source)
    {
        source.forEach(item -> routes.addRoute(item.get(0), item.get(1), item.get(2)));
    }

    @Override
    public List<String> getRoutes(String origin, String destination)
    {
        return routes.findRoute(origin, destination);
    }
}
