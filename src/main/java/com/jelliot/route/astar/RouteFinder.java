package com.jelliot.route.astar;

import java.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation of A* (pronounced "A-star"), a graph traversal and pathfinding algorithm. Given a
 * weighted graph, a source node and a goal node, the algorithm finds the shortest path (with
 * respect to the given weights) from source to goal.
 */
public class RouteFinder<T extends GraphNode> {
  private final Graph<T> graph;
  private final Scorer<T> nextNodeScorer;
  private final Scorer<T> targetScorer;

  public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
    this.graph = graph;
    this.nextNodeScorer = nextNodeScorer;
    this.targetScorer = targetScorer;
  }

  public @Nullable List<T> findRoute(T from, T to) {
    Queue<RouteNode<T>> openSet = new PriorityQueue<>();
    Map<T, RouteNode<T>> allNodes = new HashMap<>();

    RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
    openSet.add(start);
    allNodes.put(from, start);

    while (!openSet.isEmpty()) {
      RouteNode<T> next = openSet.poll();
      if (next.getCurrent().equals(to)) {
        List<T> route = new ArrayList<>();
        RouteNode<T> current = next;
        do {
          route.add(0, current.getCurrent());
          current = allNodes.get(current.getPrevious());
        } while (current != null);
        return route;
      }

      graph
          .getConnections(next.getCurrent())
          .forEach(
              connection -> {
                RouteNode<T> nextNode =
                    allNodes.getOrDefault(connection, new RouteNode<>(connection));
                allNodes.put(connection, nextNode);

                double newScore =
                    next.getRouteScore()
                        + nextNodeScorer.computeCost(next.getCurrent(), connection);
                if (newScore < nextNode.getRouteScore()) {
                  nextNode.setPrevious(next.getCurrent());
                  nextNode.setRouteScore(newScore);
                  nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                  openSet.add(nextNode);
                }
              });
    }
    // No route found
    return null;
  }
}
