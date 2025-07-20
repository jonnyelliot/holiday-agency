package com.jelliot.quote;

import com.jelliot.Costs;
import com.jelliot.io.dao.Flight;
import com.jelliot.io.dao.Journey;
import com.jelliot.io.dao.JourneySuggestion;
import java.util.List;

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

  public JourneySuggestion getLowestCostJourney(Journey journey) {
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
            journey.getStartingAirport(), journey.getDestinationAirport());
    List<Flight> inBoundRoute =
        flightRouteCalculator.getRoute(
            journey.getDestinationAirport(), journey.getStartingAirport());

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
