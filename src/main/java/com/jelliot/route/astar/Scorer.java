package com.jelliot.route.astar;

/**
 * A* Scorer. Informs the algorithm how close it is to the target goal.
 *
 * @param <T> graph node implementation
 */
public interface Scorer<T extends GraphNode> {
  double computeCost(T from, T to);
}
