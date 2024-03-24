package org.mehmetcc.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Path;
import org.mehmetcc.io.Printer;

public class ApplicationContextDeserializer {

  private final Printer printer;

  public ApplicationContextDeserializer(final Printer printer) {
    this.printer = printer;
  }

  public ApplicationContext deserialize() {
    var path = Path.of(ApplicationContextConstants.APPLICATION_CONTEXT_LOCATION);
    checkIfApplicationStateExists(
        path); // check if application context exists under this path. Exits otherwise.

    ObjectMapper mapper = new ObjectMapper();
    ApplicationContext content = null; // will never return this tho

    try {
      content = mapper.readValue(path.toFile(), ApplicationContext.class);
      printer.printLine("ApplicationContext from a previous session has recovered: ");
      printer.printLine(content.toString());
    } catch (IOException e) {
      printer.printError("Error while deserializing ApplicationContext. Gracefully terminating");
      System.exit(0);
    }
    return content;
  }

  private void checkIfApplicationStateExists(final Path path) {
    if (!path.toFile().exists()) {
      printer.printError("No ApplicationState exists. Gracefully terminating.");
      System.exit(0);
    }
  }
}
