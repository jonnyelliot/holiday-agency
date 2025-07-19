package com.jelliot.quote;

import com.jelliot.Costs;
import com.jelliot.io.dao.Journey;

public class CostToAirportCalculator {

  private final Costs costs;

  public CostToAirportCalculator(Costs costs) {
    this.costs = costs;
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

  private static int getCarCount(Journey journey, int passengersPerCar) {
    return (int) Math.ceil(journey.getPassengers() / ((double) passengersPerCar));
  }
}
