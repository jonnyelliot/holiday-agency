package com.jelliot.route;

import com.jelliot.route.astar.GraphNode;
import java.util.Objects;

public class AirportGraphNode implements GraphNode {
  private final String id;

  public AirportGraphNode(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AirportGraphNode that = (AirportGraphNode) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
