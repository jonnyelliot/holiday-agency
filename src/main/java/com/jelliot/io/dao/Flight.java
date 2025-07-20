package com.jelliot.io.dao;

import static java.util.Arrays.asList;

import com.google.common.collect.Multimaps;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/** Representation of a flight that was provided as input to the application */
public class Flight {
  private final @NotNull String airportFrom;
  private final @NotNull String airportTo;
  private final int miles;

  public static Map<String, Collection<Flight>> index(Flight... flights) {
    return index(asList(flights));
  }

  public static Map<String, Collection<Flight>> index(Collection<Flight> flights) {
    return Multimaps.index(flights, Flight::getAirportFrom).asMap();
  }

  public Flight(@NotNull String airportFrom, @NotNull String airportTo, int miles) {
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
