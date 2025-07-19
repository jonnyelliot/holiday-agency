package com.jelliot.io.dao;

public class Flight {
  private final String airportFrom;
  private final String airportTo;
  private final int miles;

  public Flight(String airportFrom, String airportTo, int miles) {
    this.airportFrom = airportFrom;
    this.airportTo = airportTo;
    this.miles = miles;
  }

  public String getAirportFrom() {
    return airportFrom;
  }

  public String getAirportTo() {
    return airportTo;
  }

  public int getMiles() {
    return miles;
  }

  @Override
  public String toString() {
    return "Flight{"
        + "airportFrom='"
        + airportFrom
        + '\''
        + ", airportTo='"
        + airportTo
        + '\''
        + ", miles="
        + miles
        + '}';
  }
}
