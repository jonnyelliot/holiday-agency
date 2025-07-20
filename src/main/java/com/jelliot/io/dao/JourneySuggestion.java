package com.jelliot.io.dao;

import java.util.List;

public class JourneySuggestion {

  private final String id;
  private final String vehicle;
  private final long vehicleReturnCost;
  private final List<Flight> outboundRoute;
  private final long outboundCost;
  private final List<Flight> inboundRoute;
  private final long inboundCost;
  private final long totalCost;

  public JourneySuggestion(
      String id,
      String vehicle,
      long vehicleReturnCost,
      List<Flight> outboundRoute,
      long outboundCost,
      List<Flight> inboundRoute,
      long inboundCost,
      long totalCost) {
    this.id = id;
    this.vehicle = vehicle;
    this.vehicleReturnCost = vehicleReturnCost;
    this.outboundRoute = outboundRoute;
    this.outboundCost = outboundCost;
    this.inboundRoute = inboundRoute;
    this.inboundCost = inboundCost;
    this.totalCost = totalCost;
  }

  public String getVehicle() {
    return vehicle;
  }

  public long getVehicleReturnCost() {
    return vehicleReturnCost;
  }

  public List<Flight> getOutboundRoute() {
    return outboundRoute;
  }

  public long getOutboundCost() {
    return outboundCost;
  }

  public List<Flight> getInboundRoute() {
    return inboundRoute;
  }

  public long getInboundCost() {
    return inboundCost;
  }

  public long getTotalCost() {
    return totalCost;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "JourneySuggestion{"
        + "id='"
        + id
        + '\''
        + "vehicle='"
        + vehicle
        + '\''
        + ", vehicleReturnCost="
        + vehicleReturnCost
        + ", outboundRoute="
        + outboundRoute
        + ", outboundCost="
        + outboundCost
        + ", inboundRoute="
        + inboundRoute
        + ", inboundCost="
        + inboundCost
        + ", totalCost="
        + totalCost
        + '}';
  }
}
