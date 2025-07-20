package com.jelliot;

import com.google.common.collect.Multimaps;
import com.jelliot.io.FlightFileReader;
import com.jelliot.io.JourneyFileReader;
import com.jelliot.io.JourneySuggestionWriter;
import com.jelliot.io.dao.Flight;
import com.jelliot.io.dao.Journey;
import com.jelliot.io.dao.JourneySuggestion;
import com.jelliot.quote.CostToAirportCalculator;
import com.jelliot.quote.FlightRouteCalculator;
import com.jelliot.quote.OverridableCosts;
import com.jelliot.quote.QuoteGenerator;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);
  private static final Path FLIGHT_INPUT_PATH = Path.of("flights.csv");
  private static final Path JOURNEY_INPUT_PATH = Path.of("journeys.csv");
  private static final Path QUOTE_OUTPUT_PATH = Path.of("quotes.csv");

  public static void main(String[] args) {
    Map<String, Collection<Flight>> flights = loadFlights();
    Collection<Journey> journeys = loadJourneys();

    Costs costs = new OverridableCosts();
    CostToAirportCalculator costToAirportCalculator = new CostToAirportCalculator(costs);
    FlightRouteCalculator flightRouteCalculator = new FlightRouteCalculator(flights);
    QuoteGenerator quoteGenerator =
        new QuoteGenerator(costToAirportCalculator, flightRouteCalculator, costs);

    log.info("Generating quotes");

    List<JourneySuggestion> journeySuggestions = new ArrayList<>();

    for (Journey j : journeys) {
      JourneySuggestion lowestCostJourney = quoteGenerator.getLowestCostJourney(j);
      journeySuggestions.add(lowestCostJourney);
      log.info("Lowest cost quote:\n{}\n{}\n", j, lowestCostJourney);
    }

    writeOutput(journeySuggestions);

    log.info("Quotes written to {}", QUOTE_OUTPUT_PATH.toAbsolutePath());
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
      return Multimaps.index(flights, Flight::getAirportFrom).asMap();
    } catch (IOException e) {
      throw new RuntimeException("Failed to load flight data", e);
    }
  }

  private static void writeOutput(List<JourneySuggestion> journeySuggestions) {
    try {
      new JourneySuggestionWriter(QUOTE_OUTPUT_PATH).writeAll(journeySuggestions);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write output", e);
    }
  }
}
