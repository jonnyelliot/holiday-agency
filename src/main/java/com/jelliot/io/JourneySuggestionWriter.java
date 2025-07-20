package com.jelliot.io;

import com.jelliot.io.dao.Flight;
import com.jelliot.io.dao.JourneySuggestion;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/** Writes priced-up and routed journeys to file */
public class JourneySuggestionWriter {
  public static final String HEADER =
      "#,vehicle,vehicle return cost,outbound rate,outbound cost,inbound route,inbound cost,total cost";
  public static final String SEPARATOR = ",";
  private final Path path;

  public JourneySuggestionWriter(Path path) {
    this.path = path;
  }

  public void writeAll(List<JourneySuggestion> journeySuggestions) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
      writer.write(HEADER);
      writer.newLine();
      for (JourneySuggestion s : journeySuggestions) {
        writer.write(s.getId());
        writer.write(SEPARATOR);
        writer.write(singleQuoted(s.getVehicle()));
        writer.write(SEPARATOR);
        writer.write(penceToString(s.getVehicleReturnCost()));
        writer.write(SEPARATOR);
        writer.write(routeToString(s.getOutboundRoute(), false));
        writer.write(SEPARATOR);
        writer.write(penceToString(s.getOutboundCost()));
        writer.write(SEPARATOR);
        writer.write(routeToString(s.getInboundRoute(), true));
        writer.write(SEPARATOR);
        writer.write(penceToString(s.getInboundCost()));
        writer.write(SEPARATOR);
        writer.write(penceToString(s.getTotalCost()));
        writer.newLine();
      }
    }
  }

  private @NotNull String routeToString(List<Flight> route, boolean inbound) {
    if (route.isEmpty()) {
      if (inbound) {
        return "'No inbound flight'";
      } else {
        return "'No outbound flight'";
      }
    }
    return singleQuoted(route.stream().map(Flight::toString).collect(Collectors.joining("--")));
  }

  private String singleQuoted(String s) {
    return String.format("'%s'", s);
  }

  private String penceToString(long pence) {
    if (pence < 0) {
      throw new IllegalArgumentException();
    } else if (pence == 0) {
      return "0";
    } else {
      String s = String.valueOf(pence);
      return String.format("%s.%s", s.substring(0, s.length() - 2), s.substring(s.length() - 2));
    }
  }
}
