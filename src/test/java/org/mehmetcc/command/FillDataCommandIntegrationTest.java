package org.mehmetcc.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;
import org.mehmetcc.parser.Token;
import org.mehmetcc.parser.TokenType;

class FillDataCommandIntegrationTest {

  @TempDir
  File temporaryDirectory;

  PrintStream standardOut;
  ByteArrayOutputStream outputStreamCaptor;


  FillDataCommand dut;

  String content;

  @BeforeEach
  void setup() throws IOException {
    dut = new FillDataCommand(new Printer(false));

    standardOut = System.out;
    outputStreamCaptor = new ByteArrayOutputStream();

    System.setOut(new PrintStream(outputStreamCaptor));
    content = Files.readString(Path.of("src/main/resources/queen-of-hearts.txt"));
  }

  @Test
  void validPathAndOptionalSeperatorGivenShouldWriteFile() {
    ParsingResult result = new ParsingResult(Token.command("fill-data"),
        Optional.of(temporaryDirectory.toPath().resolve("teoman.txt")),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";")));
    List<String> expected = formatTestData(";");

    dut.execute(result);

    assertAll(
        () -> assertTrue(Files.exists(temporaryDirectory.toPath().resolve("teoman.txt")),
            "File should exist"),
        () -> assertLinesMatch(expected,
            Files.readAllLines(temporaryDirectory.toPath().resolve("teoman.txt"))));
  }

  @Test
  void validPathGivenShouldWriteFile() {
    ParsingResult result = new ParsingResult(Token.command("fill-data"),
        Optional.of(temporaryDirectory.toPath().resolve("teoman.txt")),
        false,
        false,
        Optional.empty());
    List<String> expected = formatTestData(":::");

    dut.execute(result);

    assertAll(
        () -> assertTrue(Files.exists(temporaryDirectory.toPath().resolve("teoman.txt")),
            "File should exist"),
        () -> assertLinesMatch(expected,
            Files.readAllLines(temporaryDirectory.toPath().resolve("teoman.txt"))));
  }

  @Test
  void invalidSourcePathGivenShouldWriteStdOut() {
    ParsingResult result = new ParsingResult(Token.command("shred"),
        Optional.of(temporaryDirectory.toPath().resolve("teoman.txt")),
        false,
        false,
        Optional.empty());

    dut = new FillDataCommand("buralar/da/dutluktu", new Printer(false));
    dut.execute(result);

    assertThat(outputStreamCaptor.toString().trim()).isNotBlank()
        .isEqualTo("Source file can't be read. Contact me for troubleshooting.");
  }


  private List<String> formatTestData(String seperator) {
    return Arrays.stream(content.replaceAll("\\r", "").split("\n"))
        .map(current -> "%s %s".formatted(current, seperator))
        .toList();
  }
}
