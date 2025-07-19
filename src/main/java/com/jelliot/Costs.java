package com.jelliot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Costs {

  private static final Logger log = LoggerFactory.getLogger(Costs.class);

  public static final int COST_FLIGHT_MILE = getCost("COST_FLIGHT_MILE", 10);
  public static final int COST_TAXI_MILE = getCost("COST_TAXI_MILE", 40);
  public static final int COST_CAR_MILE = getCost("COST_CAR_MILE", 20);
  public static final int COST_CAR_PARKING = getCost("COST_CAR_PARKING", 300);

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
}
