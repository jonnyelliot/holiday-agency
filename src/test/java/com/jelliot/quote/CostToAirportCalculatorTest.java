package com.jelliot.quote;

import static org.junit.jupiter.api.Assertions.*;

import com.jelliot.io.dao.Journey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CostToAirportCalculatorTest {

  private CostToAirportCalculator calculator;

  @BeforeEach
  public void setup() {
    calculator = new CostToAirportCalculator(new OverridableCosts());
  }

  @Test
  void getCarReturnCost() {
    assertEquals(700, calculator.getCarReturnCost(getJourney(1, 10)));
    assertEquals(1100, calculator.getCarReturnCost(getJourney(2, 20)));
    assertEquals(0, calculator.getCarReturnCost(getJourney(0, 1)));
    assertEquals(300, calculator.getCarReturnCost(getJourney(1, 0)));
  }

  @Test
  void getCarCostMultipleCars() {
    // miles there and back is 10 * 2 = 20
    // per car, cost per mile is 20, so cost of mileage = 20 * 20 = 400
    // per car, packing is 300
    // per car, total cost is 700
    // 5 passengers = 2 cars, so total cost is 1400
    assertEquals(1400, calculator.getCarReturnCost(getJourney(5, 10)));
  }

  @Test
  void getTaxiCostMultipleCars() {
    // miles there and back is 10 * 2 = 20
    // per taxi, cost per mile is 40, so cost of mileage = 20 * 40 = 800
    // per taxi, total cost is 800
    // 5 passengers = 2 taxis, so total cost is 1600
    assertEquals(1600, calculator.getTaxiReturnCost(getJourney(5, 10)));
  }

  @Test
  void getTaxiReturnCost() {
    assertEquals(800, calculator.getTaxiReturnCost(getJourney(1, 10)));
    assertEquals(800, calculator.getTaxiReturnCost(getJourney(2, 10)));
    assertEquals(1600, calculator.getTaxiReturnCost(getJourney(2, 20)));
    assertEquals(0, calculator.getTaxiReturnCost(getJourney(0, 1)));
    assertEquals(0, calculator.getTaxiReturnCost(getJourney(1, 0)));
  }

  private Journey getJourney(int passengers, int milesApart) {
    return new Journey("1", passengers, "A", "B", milesApart);
  }
}
