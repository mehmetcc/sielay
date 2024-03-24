package org.mehmetcc.command;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;

public class FillDataCommand implements CliCommand {

  private static final String DEFAULT_SEPERATOR = ":::";

  private final String sourcePath;

  private final Printer printer;

  public FillDataCommand(final Printer printer) {
    this.printer = printer;
    this.sourcePath = "src/main/resources/queen-of-hearts.txt";
  }

  public FillDataCommand(final Printer printer, final String sourcePath) {
    this.printer = printer;
    this.sourcePath = sourcePath;
  }


  @Override
  public void execute(final ParsingResult result) {
    overwriteFile(result, readSourceFile());
    printer.printLine(
        "Successfully printed to file at %s".formatted(
            result.path().orElse(Path.of("")).toString()));
  }

  private List<String> readSourceFile() {
    Path path = Paths.get(sourcePath);
    String content = "";
    try {
      content = Files.readString(path);
    } catch (IOException e) {
      printer.printError("Source file can't be read. Contact me for troubleshooting.");
    }
    return Arrays.asList(content.replaceAll("\\r", "").split("\n"));
  }

  private void overwriteFile(final ParsingResult result,
      final List<String> content) {
    try (FileWriter writer = new FileWriter(result.path().orElse(Path.of("")).toFile())) {
      writer.write(extractSeperator(result, content));
    } catch (IOException e) {
      printer.printError("Given path can't be read. Terminating gracefully.");
    }
  }

  private String extractSeperator(final ParsingResult result, final List<String> content) {
    if (result.seperator().isPresent()) {
      return applySeperator(content, result.seperator().get().getContent());
    } else {
      return applySeperator(content, DEFAULT_SEPERATOR);
    }
  }

  private String applySeperator(final List<String> content, final String seperator) {
    return content.stream()
        .map(current -> "%s %s".formatted(current, seperator))
        .collect(Collectors.joining("\n"));
  }
}
