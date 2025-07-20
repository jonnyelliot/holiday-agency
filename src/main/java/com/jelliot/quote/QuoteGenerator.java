package com.jelliot.quote;

import com.jelliot.io.dao.Flight;
import com.jelliot.io.dao.Journey;
import com.jelliot.io.dao.JourneySuggestion;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/** Given a journey outline, generates the lowest cost quote; a priced-up and routed journey */
public class QuoteGenerator {

  public static final String TAXI = "Taxi";
  public static final String CAR = "Car";
  private final CostToAirportCalculator costToAirportCalculator;
  private final FlightRouteCalculator flightRouteCalculator;
  private final Costs costs;

  public QuoteGenerator(
      CostToAirportCalculator costToAirportCalculator,
      FlightRouteCalculator flightRouteCalculator,
      Costs costs) {
    this.costToAirportCalculator = costToAirportCalculator;
    this.flightRouteCalculator = flightRouteCalculator;
    this.costs = costs;
  }

  public JourneySuggestion getLowestCostJourney(
      Journey journey, Map<String, Collection<Flight>> flights) {
    int carReturnCost = costToAirportCalculator.getCarReturnCost(journey);
    int taxiReturnCost = costToAirportCalculator.getTaxiReturnCost(journey);
    String vehicle;
    int vehicleReturnCost;
    // prefer car if costs are the same
    if (carReturnCost > taxiReturnCost) {
      vehicle = TAXI;
      vehicleReturnCost = taxiReturnCost;
    } else {
      vehicle = CAR;
      vehicleReturnCost = carReturnCost;
    }
    List<Flight> outBoundRoute =
        flightRouteCalculator.getRoute(
            journey.getStartingAirport(), journey.getDestinationAirport(), flights);
    List<Flight> inBoundRoute =
        flightRouteCalculator.getRoute(
            journey.getDestinationAirport(), journey.getStartingAirport(), flights);

    int outBoundCost = getCostOfFlights(outBoundRoute, journey.getPassengers());
    int inBoundCost = getCostOfFlights(inBoundRoute, journey.getPassengers());
    int totalCost =
        (inBoundRoute.isEmpty() || outBoundRoute.isEmpty())
            ? 0
            : vehicleReturnCost + outBoundCost + inBoundCost;

    return new JourneySuggestion(
        journey.getId(),
        vehicle,
        vehicleReturnCost,
        outBoundRoute,
        outBoundCost,
        inBoundRoute,
        inBoundCost,
        totalCost);
  }

  private int getCostOfFlights(List<Flight> flights, int passengers) {
    return passengers
        * flights.stream()
            .map(Flight::getMiles)
            .map(m -> m * costs.getFlightMile())
            .reduce(0, Integer::sum);
  }
}
