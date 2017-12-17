package com.lastminute.data.persistency;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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

    private class Edges
    {
        private Map<String, List<String>> edges = new HashMap<>();

        void addEdge(String edge, String flightNumber)
        {
            List<String> flights = edges.putIfAbsent(edge, new LinkedList<>());
            if (flights == null)
            {
                flights = edges.get(edge);
            }
            flights.add(flightNumber);
        }

        List<String> getFlights(String destination)
        {
            return edges.getOrDefault(destination, Collections.emptyList());
        }
    }

}
