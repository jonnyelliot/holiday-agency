package com.jelliot.io.dao;

import org.jetbrains.annotations.NotNull;

public class Flight {
  private final @NotNull String airportFrom;
  private final @NotNull String airportTo;
  private final int miles;

  public Flight(String airportFrom, String airportTo, int miles) {
    this.airportFrom = airportFrom;
    this.airportTo = airportTo;
    this.miles = miles;
  }

  public @NotNull String getAirportFrom() {
    return airportFrom;
  }

  public @NotNull String getAirportTo() {
    return airportTo;
  }

  public int getMiles() {
    return miles;
  }

  @Override
  public String toString() {
    return airportFrom + airportTo + miles;
  }
}
