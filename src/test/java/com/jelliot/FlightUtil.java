package com.jelliot;

import com.jelliot.io.dao.Flight;

public class FlightUtil {
  public static final String AIRPORT_A = "A";
  public static final String AIRPORT_B = "B";
  public static final String AIRPORT_C = "C";
  public static final String AIRPORT_D = "D";

  public static final Flight A_TO_B = new Flight(AIRPORT_A, AIRPORT_B, 3);
  public static final Flight B_TO_A = new Flight(AIRPORT_B, AIRPORT_A, 3);
  public static final Flight A_TO_C = new Flight(AIRPORT_A, AIRPORT_C, 5);
  public static final Flight B_TO_C = new Flight(AIRPORT_B, AIRPORT_C, 7);
  public static final Flight C_TO_D = new Flight(AIRPORT_C, AIRPORT_D, 11);
  public static final Flight C_TO_B = new Flight(AIRPORT_C, AIRPORT_B, 13);
}
