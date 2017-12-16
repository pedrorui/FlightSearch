package com.lastminute.data.persistency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Routes
{
    private Map<String, Edges> vertices = new HashMap<>();

    void addRoute(String origin, String destination, String flightNumber)
    {
        Edges edges = vertices.putIfAbsent(origin, new Edges());
        if (edges == null)
        {
            edges = vertices.get(origin);
        }
        edges.addEdge(destination, flightNumber);
    }

    List<String> findRoute(String origin, String destination)
    {
        return vertices.get(origin).getFlights(destination);
    }
}
