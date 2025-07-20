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

/** Calculates the optimal flight route to get from one place to another */
public class FlightRouteCalculator {

  // we don't have a heuristic to hint how close we are. In reality, we could use the straight
  // line distance from the current node (airport) to the destination, but we don't have this data
  // so instead use a constant value, effectively removing any heuristic
  private static final Scorer<AirportGraphNode> TARGET_SCORER = (from, to) -> 1;

  public List<Flight> getRoute(
      String startingAirport, String destinationAirport, Map<String, Collection<Flight>> flights) {
    RouteFinder<AirportGraphNode> routeFinder =
        new RouteFinder<>(createGraph(flights), createScorer(flights), TARGET_SCORER);

    @Nullable
    List<AirportGraphNode> routeNodes =
        routeFinder.findRoute(
            new AirportGraphNode(startingAirport), new AirportGraphNode(destinationAirport));

    if (routeNodes == null) {
      // no route
      return Collections.emptyList();
    }

    return getFlightsFromNodes(routeNodes, flights);
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

  @NotNull
  private List<Flight> getFlightsFromNodes(
      @NotNull List<AirportGraphNode> routeNodes, Map<String, Collection<Flight>> flights) {
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

  @NotNull
  private Scorer<AirportGraphNode> createScorer(Map<String, Collection<Flight>> flights) {
    return (from, to) ->
        flights.get(from.getId()).stream()
            .filter(n -> n.getAirportTo().equals(to.getId()))
            .map(Flight::getMiles)
            .sorted()
            .findFirst()
            .orElse(-1);
  }
}
