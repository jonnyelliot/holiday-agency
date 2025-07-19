package com.jelliot.quote;

import com.jelliot.Costs;

public class OverridableCosts implements Costs {
  private static int getCost(String env, int defaultValue) {
    String envVar = System.getenv(env);
    if (envVar == null) {
      return defaultValue;
    } else {
      try {
        return Integer.parseInt(envVar);
      } catch (NumberFormatException e) {
        throw new RuntimeException("Invalid cost value for " + env, e);
      }
    }
  }

  @Override
  public int getFlightMile() {
    return getCost("COST_FLIGHT_MILE", 10);
  }

  @Override
  public int getTaxiMile() {
    return getCost("COST_TAXI_MILE", 40);
  }

  @Override
  public int getCarMile() {
    return getCost("COST_CAR_MILE", 20);
  }

  @Override
  public int getCarParking() {
    return getCost("COST_CAR_PARKING", 300);
  }

  @Override
  public int getMaxPassengersPerCar() {
    return getCost("PASSENGERS_PER_TAXI", 4);
  }

  @Override
  public int getMaxPassengersPerTaxi() {
    return getCost("PASSENGERS_PER_CAR", 4);
  }
}
