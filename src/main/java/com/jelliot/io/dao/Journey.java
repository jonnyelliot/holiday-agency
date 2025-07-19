package com.jelliot.io.dao;

public class Journey {
  private final int passengers;
  private final String startingAirport;
  private final String destinationAirport;
  private final int homeToStartingAirportMiles;

  public Journey(
      int passengers,
      String startingAirport,
      String destinationAirport,
      int homeToStartingAirportMiles) {
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
}
