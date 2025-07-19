package com.jelliot;

import com.google.common.collect.Multimaps;
import com.jelliot.io.FlightFileReader;
import com.jelliot.io.JourneyFileReader;
import com.jelliot.io.dao.Flight;
import com.jelliot.io.dao.Journey;
import com.jelliot.io.dao.JourneySuggestion;
import com.jelliot.quote.CostToAirportCalculator;
import com.jelliot.quote.FlightRouteCalculator;
import com.jelliot.quote.OverridableCosts;
import com.jelliot.quote.QuoteGenerator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);
  private static final Path FLIGHT_INPUT_PATH = Path.of("data", "inputs", "flights.txt");
  private static final Path JOURNEY_INPUT_PATH = Path.of("data", "inputs", "journeys.csv");

  public static void main(String[] args) {
    Map<String, Collection<Flight>> flights = loadFlights();
    Collection<Journey> journeys = loadJourneys();

    Costs costs = new OverridableCosts();
    CostToAirportCalculator costToAirportCalculator = new CostToAirportCalculator(costs);
    FlightRouteCalculator flightRouteCalculator = new FlightRouteCalculator(flights);
    QuoteGenerator quoteGenerator =
        new QuoteGenerator(costToAirportCalculator, flightRouteCalculator, costs);

    for (Journey j : journeys) {
      JourneySuggestion lowestCostJourney = quoteGenerator.getLowestCostJourney(j);
      log.info("Journey {} lowest cost: {}", j, lowestCostJourney);
    }
  }

  private static Collection<Journey> loadJourneys() {
    try {
      log.info("Loading journeys from {}", JOURNEY_INPUT_PATH.toAbsolutePath());
      Collection<Journey> journeys = new JourneyFileReader(JOURNEY_INPUT_PATH).readAll();
      log.info("Loaded {} journeys", journeys.size());
      return journeys;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load journey data", e);
    }
  }

  private static Map<String, Collection<Flight>> loadFlights() {
    try {
      log.info("Loading flights from {}", FLIGHT_INPUT_PATH.toAbsolutePath());
      Collection<Flight> flights = new FlightFileReader(FLIGHT_INPUT_PATH).readAll();
      log.info("Loaded {} flights", flights.size());
      return  Multimaps.index(flights, Flight::getAirportFrom).asMap();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load flight data", e);
    }
  }
}
