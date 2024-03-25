package org.mehmetcc.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;
import org.mehmetcc.parser.Token;

class ShredCommandIntegrationTest {
  @TempDir
  File temporaryDirectory;

  @TempDir
  File targetFile;

  PrintStream standardOut;
  ByteArrayOutputStream outputStreamCaptor;


  ShredCommand dut;

  @BeforeEach
  void setup() throws IOException {
    targetFile = new File(temporaryDirectory, "sting.txt");
    Files.writeString(targetFile.toPath(),
        "I'm an alien, I'm a legal alien, I'm an Englishman in New York");

    dut = new ShredCommand(new Printer(false));

    standardOut = System.out;
    outputStreamCaptor = new ByteArrayOutputStream();

    System.setOut(new PrintStream(outputStreamCaptor));

  }

  @Test
  void validPathGivenShouldOverwriteAndDeleteFile() {
    ParsingResult result = new ParsingResult(Token.command("shred"),
        Optional.of(targetFile.toPath()),
        false,
        false,
        Optional.empty());

    dut.execute(result);

    assertFalse(Files.exists(targetFile.toPath()));
  }

  @Test
  void invalidPathGivenShouldWriteStdOut() {
    ParsingResult result = new ParsingResult(Token.command("shred"),
        Optional.of(Paths.get("buralar/hep/dutluktu")),
        false,
        false,
        Optional.empty());

    dut.execute(result);

    assertThat(outputStreamCaptor.toString().trim())
        .isNotBlank()
        .contains("Given path can't be read. Terminating gracefully.");
  }

  @Test
  void invalidSourcePathGivenShouldWriteStdOut() {
    ParsingResult result = new ParsingResult(Token.command("shred"),
        Optional.of(targetFile.toPath()),
        false,
        false,
        Optional.empty());

    dut = new ShredCommand("buralar/da/dutluktu", new Printer(false));
    dut.execute(result);

    assertThat(outputStreamCaptor.toString().trim()).isNotBlank()
        .isEqualTo("Source file can't be read. Contact me for troubleshooting.");
  }
}