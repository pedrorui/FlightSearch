package com.lastminute.data.persistency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Routes
{
    private Map<String, Edges> vertices = new HashMap<>();

    public void addRoute(String origin, String destination, String flightNumber)
    {
        Edges edges = vertices.putIfAbsent(origin, new Edges());
        edges.addEdge(destination, flightNumber);
    }

    public List<String> findRoute(String origin, String destination)
    {
        return vertices.get(origin).getFlights(destination);
    }
}
