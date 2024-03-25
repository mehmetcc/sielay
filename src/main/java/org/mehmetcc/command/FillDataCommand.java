package org.mehmetcc.command;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.mehmetcc.context.ApplicationContext;
import org.mehmetcc.context.ApplicationContextSerializer;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParserConstants;
import org.mehmetcc.parser.ParsingResult;

public class FillDataCommand implements CliCommand {
  private final String sourcePath;

  private final Printer printer;

  private final FileContext fileContext;

  private final ApplicationContextSerializer serializer;

  public FillDataCommand(final Printer printer) {
    this.printer = printer;
    this.sourcePath = "src/main/resources/queen-of-hearts.txt";
    this.fileContext = new FileContext(this.printer);
    this.serializer = new ApplicationContextSerializer(printer);
  }

  public FillDataCommand(final String sourcePath, final Printer printer) {
    this.printer = printer;
    this.sourcePath = sourcePath;
    this.fileContext = new FileContext(this.printer);
    this.serializer = new ApplicationContextSerializer(printer);
  }

  public FillDataCommand(final String sourcePath,
      final Printer printer,
      final FileContext fileContext,
      final ApplicationContextSerializer serializer) {
    this.printer = printer;
    this.sourcePath = sourcePath;
    this.fileContext = fileContext;
    this.serializer = serializer;
  }

  @Override
  public List<String> execute(final ParsingResult result) {
    var parsed = extractSeperator(result, fileContext.readLines(sourcePath));
    printer.printLine("Running %s.".formatted(ParserConstants.FILL_DATA));

    var isSuccess = result.path().map(path -> fileContext.write(path, parsed.content()))
        .orElseGet(() -> fileContext.write(Path.of(""), parsed.content())); // empty path, will generate a failure at FileContext

    printer.printLine("Task finished.");
    serializer.serialize(new ApplicationContext(parsed.content(), parsed.seperator()));
    return isSuccess ? List.of(parsed.content()) : List.of("");
  }

  private ContentAndSeperator extractSeperator(final ParsingResult result, final List<String> content) {
    return result.seperator()
        .map(seperator -> applySeperator(content, seperator.getContent()))
        .orElseGet(() -> applySeperator(content, CommandConstants.DEFAULT_SEPERATOR));
  }

  private ContentAndSeperator applySeperator(final List<String> content, final String seperator) {
    var str = content.stream()
        .map(current -> "%s %s".formatted(current, seperator))
        .collect(Collectors.joining("\n"));

    return new ContentAndSeperator(str, seperator);
  }

  record ContentAndSeperator(String content, String seperator) { }
}
