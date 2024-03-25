package org.mehmetcc.command;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParserConstants;
import org.mehmetcc.parser.ParsingResult;

public class ShredCommand implements CliCommand {
  private static final Integer NUMBER_OF_OVERWRITES = 3;

  private final String sourcePath;

  private final Printer printer;

  private final FileContext fileContext;

  public ShredCommand(final Printer printer) {
    this.printer = printer;
    this.fileContext = new FileContext(this.printer);
    this.sourcePath = "src/main/resources/nasreddin_hodja.txt";
  }

  public ShredCommand(final String sourcePath, final Printer printer) {
    this.printer = printer;
    this.fileContext = new FileContext(this.printer);
    this.sourcePath = sourcePath;
  }

  public ShredCommand(final String sourcePath, final Printer printer, final FileContext fileContext) {
    this.sourcePath = sourcePath;
    this.printer = printer;
    this.fileContext = fileContext;
  }

  @Override
  public List<String> execute(final ParsingResult result) {
    printer.printLine("Running %s.".formatted(ParserConstants.SHRED));
    var content = readSourceFile();

    result.path()
        .flatMap(path -> validateAndOverride(path, content))
        .map(path -> deleteFile(path));

    printer.printLine("Task finished.");
    return List.of(content);
  }

  private String readSourceFile() {
    printer.printLine("Reading Nasreddin Hodja");
    return fileContext.read(sourcePath);
  }

  private Boolean deleteFile(final Path path) {
    var result = fileContext.delete(path);
    if (result) printer.printLine("File deleted");
    return result;
  }

  private Optional<Path> validateAndOverride(final Path path, final String content) {
    if (!fileContext.exists(path)) return Optional.empty();
    else return Optional.of(overwriteNTimes(path, content));
  }

  private Path overwriteNTimes(final Path path, final String content) {
    for (int i = 0; i < NUMBER_OF_OVERWRITES; i++) {
      printer.printLine("%d Overwriting file.".formatted(i));
      Boolean isSuccess = fileContext.write(path, content);

      if (!isSuccess) {
        printer.printError("Error occurred at iteration #%d".formatted(i));
        return path;
      }
    }
    return path;
  }
}
