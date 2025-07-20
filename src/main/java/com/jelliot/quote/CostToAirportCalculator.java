package com.jelliot.quote;

import com.jelliot.io.dao.Journey;

/**
 * Calculates the return cost of journey to and from the airport. Includes the journey prior to
 * outbound travel, and the journey after inbound travel
 */
public class CostToAirportCalculator {

  private final Costs costs;

  public CostToAirportCalculator(Costs costs) {
    this.costs = costs;
  }

  private static int getCarCount(Journey journey, int passengersPerCar) {
    return (int) Math.ceil(journey.getPassengers() / ((double) passengersPerCar));
  }

  public int getCarReturnCost(Journey journey) {
    int carCount = getCarCount(journey, costs.getMaxPassengersPerCar());
    int costPerCar =
        costs.getCarParking() + (costs.getCarMile() * journey.getHomeToStartingAirportMiles() * 2);
    return costPerCar * carCount;
  }

  public int getTaxiReturnCost(Journey journey) {
    int taxiCount = getCarCount(journey, costs.getMaxPassengersPerTaxi());
    int costPerTaxi = costs.getTaxiMile() * journey.getHomeToStartingAirportMiles() * 2;
    return costPerTaxi * taxiCount;
  }
}
