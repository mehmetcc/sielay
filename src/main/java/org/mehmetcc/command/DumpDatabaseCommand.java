package org.mehmetcc.command;

import java.util.Arrays;
import java.util.List;
import org.mehmetcc.context.ApplicationContextDeserializer;
import org.mehmetcc.io.DatabaseConnector;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParserConstants;
import org.mehmetcc.parser.ParsingResult;

public class DumpDatabaseCommand implements CliCommand {
  private final Printer printer;

  private final FileContext fileContext;

  private final DatabaseConnector databaseConnector;

  private final ApplicationContextDeserializer applicationContextDeserializer;

  public DumpDatabaseCommand(final Printer printer) {
    this.printer = printer;
    this.fileContext = new FileContext(this.printer);
    this.databaseConnector = new DatabaseConnector(this.printer);
    this.applicationContextDeserializer = new ApplicationContextDeserializer(this.printer);
  }

  public DumpDatabaseCommand(final Printer printer,
      final FileContext fileContext,
      final DatabaseConnector databaseConnector, final
      ApplicationContextDeserializer applicationContextDeserializer) {
    this.printer = new Printer();
    this.fileContext = fileContext;
    this.databaseConnector = databaseConnector;
    this.applicationContextDeserializer = applicationContextDeserializer;
  }

  @Override
  public List<String> execute(final ParsingResult result) {
    var content = extractFields(result);
    printer.printLine("%s command finished.".formatted(ParserConstants.DUMP_DB));
    return content;
  }

  private List<String> extractFields(final ParsingResult result) {
    return result.path()
        .map(path -> result.seperator()
            .map(seperator -> extractAndApplyFile(path.toString(), seperator.getContent()))
            .orElseGet(
                () -> extractAndApplyFile(path.toString(), CommandConstants.DEFAULT_SEPERATOR))
        ).orElseGet(() -> extractAndApplyApplicationContext());
  }

  private List<String> extractAndApplyFile(final String path, final String seperator) {
    var content = fileContext.read(path);
    var lines = lines(content, seperator);
    databaseConnector.fill(lines);
    return lines;
  }

  private List<String> extractAndApplyApplicationContext() {
    var applicationContext = applicationContextDeserializer.deserialize();
    var content = lines(applicationContext.lastFillDatabaseContent(),
        applicationContext.seperator());
    databaseConnector.fill(content);
    return content;
  }

  private List<String> lines(final String content, final String seperator) {
    return Arrays.asList(content
        .replaceAll("\n", "")
        .split(seperator));
  }
}
