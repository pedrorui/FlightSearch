package com.lastminute.data.persistency;

import java.util.*;

class Edges
{
    private Map<String, List<String>> edges = new HashMap<>();

    void addEdge(String edge, String flightNumber)
    {
        List<String> flights = edges.putIfAbsent(edge, new LinkedList<>());
        flights.add(flightNumber);
    }

    List<String> getFlights(String destination)
    {
        return edges.getOrDefault(destination, Collections.emptyList());
    }
}
