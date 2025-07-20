package com.jelliot.io.dao;

/** Representation of the outline of a journey that was provided as input to the application */
public class Journey {

  private final String id;
  private final int passengers;
  private final String startingAirport;
  private final String destinationAirport;
  private final int homeToStartingAirportMiles;

  public Journey(
      String id,
      int passengers,
      String startingAirport,
      String destinationAirport,
      int homeToStartingAirportMiles) {
    this.id = id;
    this.passengers = passengers;
    this.startingAirport = startingAirport;
    this.destinationAirport = destinationAirport;
    this.homeToStartingAirportMiles = homeToStartingAirportMiles;
  }

  public int getPassengers() {
    return passengers;
  }

  public String getStartingAirport() {
    return startingAirport;
  }

  public String getDestinationAirport() {
    return destinationAirport;
  }

  public int getHomeToStartingAirportMiles() {
    return homeToStartingAirportMiles;
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Journey{"
        + "id='"
        + id
        + '\''
        + "passengers="
        + passengers
        + ", startingAirport='"
        + startingAirport
        + '\''
        + ", destinationAirport='"
        + destinationAirport
        + '\''
        + ", homeToStartingAirportMiles="
        + homeToStartingAirportMiles
        + '}';
  }
}
