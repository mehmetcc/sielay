package org.mehmetcc.command;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;

public class ShredCommand implements CliCommand {

  private static final Integer NUMBER_OF_OVERWRITES = 3;

  private final String sourcePath;

  private final Printer printer;

  public ShredCommand(final Printer printer) {
    this.printer = printer;
    this.sourcePath = "src/main/resources/nasreddin_hodja.txt";
  }

  public ShredCommand(final Printer printer, final String sourcePath) {
    this.printer = printer;
    this.sourcePath = sourcePath;
  }


  @Override
  public void execute(final ParsingResult result) {
    if (!checkIfFileExists(result.path().orElse(Path.of("")))) {
      return;
    }

    printer.printLine("Reading Nasreddin Hodja");
    String content = readHodjaNasreddin(); // hehe

    // Overwrite N times
    for (int i = 0; i < NUMBER_OF_OVERWRITES; i++) {
      printer.printLine("%d Overwriting file.".formatted(i));
      Boolean isSuccess = overwriteFile(result.path().orElse(Path.of("")), content);

      if (!isSuccess) {
        return;
      }
    }

    deleteFile(result.path().orElse(Path.of("")));
    printer.printLine("File deleted");
  }

  private String readHodjaNasreddin() {
    Path path = Paths.get(sourcePath);
    String content = "";
    try {
      content = Files.readString(path);
    } catch (IOException e) {
      printer.printError("Source file can't be read. Contact me for troubleshooting.");
    }
    return content;
  }

  private Boolean checkIfFileExists(final Path path) {
    if (Files.notExists(path)) {
      printer.printError("Given path can't be read. Terminating gracefully.");
      return false;
    }
    return true;
  }

  private Boolean overwriteFile(final Path path, final String content) {
    try (FileWriter writer = new FileWriter(path.toFile())) {
      writer.write(content);
      return true;
    } catch (IOException e) {
      printer.printError("Given path can't be read. Terminating gracefully.");
    }
    return false;
  }

  private void deleteFile(final Path path) {
    try {
      Files.delete(path);
    } catch (IOException e) {
      printer.printError("Failed to delete the file for some reason. Terminating gracefully.");
    }
  }
}
