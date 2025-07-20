package com.jelliot.io;

import com.jelliot.exception.FileFormatException;
import com.jelliot.io.dao.Journey;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class JourneyFileReader {

  private final Path path;

  public JourneyFileReader(Path path) {
    this.path = path;
  }

  public Collection<Journey> readAll() throws IOException {

    try (Stream<String> s = Files.lines(path)) {
      return s.map(this::lineToFlight).toList();
    }
  }

  private Journey lineToFlight(String line) {
    String[] values = line.split(",");
    if (values.length != 4) {
      throw new FileFormatException(path);
    }
    String id = values[0];
    int passengers = getPassengers(values);
    String homeToAirport = removeSingleQuotes(values[2]);
    String startingAirport = getStartingAirport(homeToAirport);
    int homeToStartingAirportMiles = getHomeToStartingAirportMiles(homeToAirport);
    String destinationAirport = getDestinationAirport(values);

    return new Journey(id, passengers, startingAirport, destinationAirport, homeToStartingAirportMiles);
  }

  private String getStartingAirport(String homeToAirport) {
    return homeToAirport.substring(0, 1);
  }

  private String getDestinationAirport(String[] values) {
    return removeSingleQuotes(values[3]);
  }

  private int getPassengers(String[] values) {
    try {
      return Integer.parseInt(values[1]);
    } catch (NumberFormatException e) {
      throw new FileFormatException(path, e);
    }
  }

  private int getHomeToStartingAirportMiles(String homeToAirport) {
    try {
      return Integer.parseInt(homeToAirport.substring(1));
    } catch (NumberFormatException e) {
      throw new FileFormatException(path, e);
    }
  }

  private String removeSingleQuotes(String s) {
    return s.replaceAll("'", "");
  }



}
