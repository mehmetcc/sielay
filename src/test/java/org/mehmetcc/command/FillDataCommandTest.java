package org.mehmetcc.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mehmetcc.context.ApplicationContextSerializer;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;
import org.mehmetcc.parser.Token;
import org.mehmetcc.parser.TokenType;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FillDataCommandTest {
  @Mock
  Printer printer;

  @Mock
  FileContext fileContext;

  @Mock
  ApplicationContextSerializer serializer;

  FillDataCommand fillDataCommand;

  @BeforeEach
  void setup() {
    fillDataCommand = new FillDataCommand("some/path", printer, fileContext, serializer);
  }

  @Test
  void validInputGivenShouldGenerateSuccessfully() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("fill-data"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";")));

    // Stubbing
    when(fileContext.readLines(any())).thenReturn(List.of("a", "b", "c"));
    when(fileContext.write(any(), any())).thenReturn(true);

    // Interaction
    var result = fillDataCommand.execute(data);

    // Verification
    verify(fileContext).readLines(any());
    verify(fileContext).write(any(), any());

    // Assertions
    assertThat(result).isNotEmpty()
        .hasSize(1)
        .element(0)
        .isEqualTo("a ;\nb ;\nc ;");
  }

  @Test
  void invalidInputGivenShouldGenerateEmptyList() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("fill-data"),
        Optional.empty(),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";")));

    // Stubbing
    when(fileContext.readLines(any())).thenReturn(List.of("a", "b", "c"));
    when(fileContext.write(any(), any())).thenReturn(false);

    // Interaction
    var result = fillDataCommand.execute(data);

    // Verification
    verify(fileContext).readLines(any());
    verify(fileContext).write(any(), any());

    // Assertion
    assertThat(result).hasSize(1)
        .element(0)
        .isEqualTo("");
  }

  @Test
  void seperatorNotGivenShouldUseDefaultSeperator() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("fill-data"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.empty());

    // Stubbing
    when(fileContext.readLines(any())).thenReturn(List.of("a", "b", "c"));
    when(fileContext.write(any(), any())).thenReturn(true);

    // Interaction
    var result = fillDataCommand.execute(data);

    // Verification
    verify(fileContext).readLines(any());
    verify(fileContext).write(any(), any());

    // Assertions
    assertThat(result).isNotEmpty()
        .hasSize(1)
        .element(0)
        .isEqualTo("a :::\nb :::\nc :::");
  }
}