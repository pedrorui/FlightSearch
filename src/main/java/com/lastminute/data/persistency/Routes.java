package com.lastminute.data.persistency;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Routes
{
    private Map<String, Edges> vertices = new HashMap<>();

    Routes addRoute(String origin, String destination, String flightNumber)
    {
        Edges edges = vertices.putIfAbsent(origin, new Edges());
        if (edges == null)
        {
            edges = vertices.get(origin);
        }
        edges.addEdge(destination, flightNumber);

        return this;
    }

    List<String> findRoute(String origin, String destination)
    {
        return getEdges(origin).getFlights(destination);
    }

    Edges getEdges(String origin)
    {
        return vertices.get(origin);
    }

    int size()
    {
        return vertices.size();
    }

    class Edges
    {
        private Map<String, List<String>> edges = new HashMap<>();

        private Edges addEdge(String edge, String flightNumber)
        {
            List<String> flights = edges.putIfAbsent(edge, new LinkedList<>());
            if (flights == null)
            {
                flights = edges.get(edge);
            }
            flights.add(flightNumber);

            return this;
        }

        int size()
        {
            return edges.size();
        }

        Set<String> getNames()
        {
            return edges.keySet();
        }

        List<String> getFlights(String destination)
        {
            return edges.getOrDefault(destination, Collections.emptyList());
        }
    }
}
