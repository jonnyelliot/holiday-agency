package com.jelliot.quote;

/** Provider of cost data. All values are in round-number pence */
public interface Costs {

  int getFlightMile();

  int getTaxiMile();

  int getCarMile();

  int getCarParking();

  int getMaxPassengersPerCar();

  int getMaxPassengersPerTaxi();
}
