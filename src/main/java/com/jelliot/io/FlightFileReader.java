package com.jelliot.io;

import com.jelliot.exception.FileFormatException;
import com.jelliot.io.dao.Flight;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

/** Reads flights from file */
public class FlightFileReader {

  private final Path path;

  public FlightFileReader(Path path) {
    this.path = path;
  }

  public Collection<@NotNull Flight> readAll() throws IOException {
    try (Stream<String> stream = Files.lines(path)) {
      return
      // remove any duplicate flights
      stream.distinct().map(this::toFlight).collect(Collectors.toList());
    }
  }

  private Flight toFlight(String line) {
    if (line.length() < 3) {
      throw new FileFormatException(path);
    }

    String airportFrom = line.substring(0, 1);
    String airportTo = line.substring(1, 2);
    int miles = getMiles(line);
    return new Flight(airportFrom, airportTo, miles);
  }

  private int getMiles(String line) {
    try {
      return Integer.parseInt(line.substring(2));
    } catch (NumberFormatException e) {
      throw new FileFormatException(path, e);
    }
  }
}
