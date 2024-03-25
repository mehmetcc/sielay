package org.mehmetcc.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileContext {
  private final Printer printer;

  public FileContext(final Printer printer) {
    this.printer = printer;
  }

  public List<String> readLines(final String sourcePath) {
    Path path = Paths.get(sourcePath);
    String content = "";
    try {
      content = Files.readString(path);
    } catch (IOException e) {
      printer.printError("Source file can't be read. Contact me for troubleshooting.");
    }
    return Arrays.asList(content.replaceAll("\\r", "").split("\n"));
  }


  public String read(final String sourcePath) {
    Path path = Paths.get(sourcePath);
    String content = "";
    try {
      content = Files.readString(path);
    } catch (IOException e) {
      printer.printError("Source file can't be read. Contact me for troubleshooting.");
    }
    return content;
  }

  public Boolean write(final Path path, final String content) {
    try (FileWriter writer = new FileWriter(path.toFile())) {
      writer.write(content);
      return true;
    } catch (IOException e) {
      printer.printError("Given path can't be read. Terminating gracefully.");
    }
    return false;
  }

  public Boolean exists(final Path path) {
    if (Files.notExists(path)) {
      printer.printError("Given path can't be read. Terminating gracefully.");
      return false;
    }
    return true;
  }

  public Boolean delete(final Path path) {
    try {
      Files.delete(path);
      return true;
    } catch (IOException e) {
      printer.printError("Failed to delete the file for some reason. Terminating gracefully.");
    }

    return false;
  }
}
