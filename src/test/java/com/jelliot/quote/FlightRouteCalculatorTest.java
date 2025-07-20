package com.jelliot.quote;

import static com.jelliot.FlightUtil.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;

import com.jelliot.io.dao.Flight;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlightRouteCalculatorTest {

  private FlightRouteCalculator flightRouteCalculator;

  @BeforeEach
  void setUp() {
    flightRouteCalculator = new FlightRouteCalculator();
  }

  @Test
  void noFlights() {
    List<Flight> route = flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_B, emptyMap());
    assertEquals(emptyList(), route);
  }

  @Test
  void noFlightsFrom() {
    List<Flight> route = flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_B, Flight.index(B_TO_C));
    assertEquals(emptyList(), route);
  }

  @Test
  void noFlightsTo() {
    List<Flight> route = flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_B, Flight.index(A_TO_C));
    assertEquals(emptyList(), route);
  }

  @Test
  void exact() {
    List<Flight> route = flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_B, Flight.index(A_TO_B));
    assertEquals(List.of(A_TO_B), route);
  }

  @Test
  void twoHops() {
    List<Flight> route =
        flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_C, Flight.index(A_TO_B, B_TO_C));
    assertEquals(List.of(A_TO_B, B_TO_C), route);
  }

  @Test
  void threeHops() {
    List<Flight> route =
        flightRouteCalculator.getRoute(AIRPORT_A, AIRPORT_D, Flight.index(A_TO_B, B_TO_C, C_TO_D));
    assertEquals(List.of(A_TO_B, B_TO_C, C_TO_D), route);
  }

  @Test
  void noFlightsInThatDirection() {
    List<Flight> route = flightRouteCalculator.getRoute(AIRPORT_B, AIRPORT_A, Flight.index(A_TO_B));
    assertEquals(emptyList(), route);
  }
}
