package com.jelliot.quote;

import static com.jelliot.FlightUtil.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.jelliot.io.dao.Journey;
import com.jelliot.io.dao.JourneySuggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteGeneratorTest {

  public static final String JOURNEY_ID = "1";
  private OverridableCosts costs;
  private CostToAirportCalculator costToAirportCalculator;
  private FlightRouteCalculator flightRouteCalculator;
  private QuoteGenerator quoteGenerator;

  @BeforeEach
  void setUp() {
    costToAirportCalculator = mock(CostToAirportCalculator.class);
    flightRouteCalculator = mock(FlightRouteCalculator.class);
    costs = new OverridableCosts();
    quoteGenerator = new QuoteGenerator(costToAirportCalculator, flightRouteCalculator, costs);
  }

  @Test
  void lowestCostVehicleChosen() {
    Journey journey = new Journey(JOURNEY_ID, 1, AIRPORT_A, AIRPORT_B, 10);

    when(flightRouteCalculator.getRoute(eq(AIRPORT_A), eq(AIRPORT_B), any()))
        .thenReturn(singletonList(A_TO_B));
    when(flightRouteCalculator.getRoute(eq(AIRPORT_B), eq(AIRPORT_A), any()))
        .thenReturn(singletonList(B_TO_A));
    when(costToAirportCalculator.getCarReturnCost(journey)).thenReturn(1);
    when(costToAirportCalculator.getTaxiReturnCost(journey)).thenReturn(2);

    JourneySuggestion quote = quoteGenerator.getLowestCostJourney(journey, emptyMap());

    assertEquals("Car", quote.getVehicle());
    assertEquals(1, quote.getVehicleReturnCost());
  }

  @Test
  void totalCostZeroWhenNoOutboundRoute() {
    Journey journey = new Journey("1", 1, AIRPORT_A, AIRPORT_B, 10);

    when(flightRouteCalculator.getRoute(eq(AIRPORT_A), eq(AIRPORT_B), any()))
        .thenReturn(singletonList(A_TO_B));
    when(flightRouteCalculator.getRoute(eq(AIRPORT_B), eq(AIRPORT_A), any()))
        .thenReturn(emptyList());

    JourneySuggestion quote = quoteGenerator.getLowestCostJourney(journey, emptyMap());

    assertEquals(0, quote.getTotalCost());
  }

  @Test
  void totalCostZeroWhenNoInboundRoute() {
    Journey journey = new Journey("1", 1, AIRPORT_A, AIRPORT_B, 10);

    when(flightRouteCalculator.getRoute(eq(AIRPORT_A), eq(AIRPORT_B), any()))
        .thenReturn(emptyList());
    when(flightRouteCalculator.getRoute(eq(AIRPORT_B), eq(AIRPORT_A), any()))
        .thenReturn(singletonList(B_TO_A));

    JourneySuggestion quote = quoteGenerator.getLowestCostJourney(journey, emptyMap());

    assertEquals(0, quote.getTotalCost());
  }

  @Test
  void outboundFlightCosts() {
    Journey journey = new Journey("1", 41, AIRPORT_A, AIRPORT_C, 10);

    when(flightRouteCalculator.getRoute(eq(AIRPORT_A), eq(AIRPORT_C), any()))
        .thenReturn(asList(A_TO_B, B_TO_C));

    JourneySuggestion quote = quoteGenerator.getLowestCostJourney(journey, emptyMap());

    int expectedCost =
        (A_TO_B.getMiles() + B_TO_C.getMiles()) * costs.getFlightMile() * journey.getPassengers();

    assertEquals(expectedCost, quote.getOutboundCost());
  }

  @Test
  void inboundFlightCosts() {
    Journey journey = new Journey("1", 41, AIRPORT_A, AIRPORT_C, 10);

    when(flightRouteCalculator.getRoute(eq(AIRPORT_C), eq(AIRPORT_A), any()))
        .thenReturn(asList(C_TO_B, B_TO_A));

    JourneySuggestion quote = quoteGenerator.getLowestCostJourney(journey, emptyMap());

    int expectedCost =
        (C_TO_B.getMiles() + B_TO_A.getMiles()) * costs.getFlightMile() * journey.getPassengers();

    assertEquals(expectedCost, quote.getInboundCost());
  }
}
