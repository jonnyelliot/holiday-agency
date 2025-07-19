package com.jelliot.quote;

import com.jelliot.io.dao.Flight;
import com.jelliot.route.AirportGraphNode;
import com.jelliot.route.astar.Graph;
import com.jelliot.route.astar.RouteFinder;
import com.jelliot.route.astar.Scorer;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlightRouteCalculator {

  private final RouteFinder<AirportGraphNode> routeFinder;
  private final Map<String, Collection<Flight>> flights;

  public FlightRouteCalculator(Map<String, Collection<Flight>> flights) {
    this.flights = flights;

    Scorer<AirportGraphNode> nextNodeScorer =
        (from, to) ->
            flights.get(from.getId()).stream()
                .filter(n -> n.getAirportTo().equals(to.getId()))
                .map(Flight::getMiles)
                .sorted()
                .findFirst()
                .orElse(-1);

    Scorer<AirportGraphNode> targetScorer = (from, to) -> 1;

    routeFinder = new RouteFinder<>(createGraph(flights), nextNodeScorer, targetScorer);
  }

  private Graph<AirportGraphNode> createGraph(Map<String, Collection<Flight>> flights) {
    Set<AirportGraphNode> nodes =
        flights.keySet().stream().map(AirportGraphNode::new).collect(Collectors.toSet());
    Map<String, Set<String>> connections = new HashMap<>();
    for (Map.Entry<String, Collection<Flight>> e : flights.entrySet()) {
      AirportGraphNode from = new AirportGraphNode(e.getKey());
      nodes.add(from);
      for (Flight f : e.getValue()) {
        AirportGraphNode to = new AirportGraphNode(f.getAirportTo());
        nodes.add(to);
        Set<String> nodeConnections = connections.getOrDefault(from.getId(), new HashSet<>());
        nodeConnections.add(to.getId());
        connections.put(from.getId(), nodeConnections);
      }
    }
    return new Graph<>(nodes, connections);
  }

  public List<Flight> getRoute(String startingAirport, String destinationAirport) {

    @Nullable
    List<AirportGraphNode> routeNodes =
        routeFinder.findRoute(
            new AirportGraphNode(startingAirport), new AirportGraphNode(destinationAirport));

    if (routeNodes == null) {
      // no route
      return Collections.emptyList();
    }

    return getFlightsFromNodes(routeNodes);
  }

  @NotNull
  private List<Flight> getFlightsFromNodes(@NotNull List<AirportGraphNode> routeNodes) {
    List<Flight> route = new ArrayList<>();

    for (int i = 1; i < routeNodes.size(); i++) {
      String from = routeNodes.get(i - 1).getId();
      String to = routeNodes.get(i).getId();
      Flight flight =
          flights.get(from).stream()
              .filter(f -> f.getAirportTo().equals(to))
              .min(Comparator.comparing(Flight::getMiles))
              .orElseThrow();
      route.add(flight);
    }

    return route;
  }
}
