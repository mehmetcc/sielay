package org.mehmetcc.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.mehmetcc.io.Printer;

public class ApplicationContextSerializer {
  private final Printer printer;

  public ApplicationContextSerializer(final Printer printer) {
    this.printer = printer;
  }

  public void serialize(final ApplicationContext context) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(ApplicationContextConstants.APPLICATION_CONTEXT_LOCATION),
          context);
      printer.printLine("ApplicationContext saved under %s".formatted(
          ApplicationContextConstants.APPLICATION_CONTEXT_LOCATION));
    } catch (Exception e) {
      printer.printError("Error while serializing ApplicationContext. Gracefully terminating");
      System.exit(0);
    }
  }
}
