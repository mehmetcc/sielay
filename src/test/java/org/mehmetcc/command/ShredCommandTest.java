package org.mehmetcc.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;
import org.mehmetcc.parser.Token;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShredCommandTest {
  @Mock
  Printer printer;

  @Mock
  FileContext fileContext;

  ShredCommand shredCommand;

  @BeforeEach
  void setup() {
    shredCommand = new ShredCommand("some/path", printer, fileContext);
  }

  @Test
  void whenValidInputGivenShouldGenerateSuccessfulOutput() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("shred"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.empty());

    // Stubbing
    when(fileContext.read(any())).thenReturn("I am mayalying the lake.");
    when(fileContext.exists(any())).thenReturn(true);
    when(fileContext.write(any(), any())).thenReturn(true);

    // Interaction
    var result = shredCommand.execute(data);

    // Verification
    verify(fileContext).read(any());
    verify(fileContext).exists(any());
    verify(fileContext, atLeast(3)).write(any(), any());

    // Assertions
    assertThat(result).hasSize(1)
        .element(0)
        .isEqualTo("I am mayalying the lake.");
  }

  @Test
  void whenFaultyWriteHappensInsideApplicationContextShouldCallWriteFunctionOnlyOnce() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("shred"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.empty());

    // Stubbing
    when(fileContext.read(any())).thenReturn("I am mayalying the lake.");
    when(fileContext.exists(any())).thenReturn(true);
    when(fileContext.write(any(), any())).thenReturn(false);

    // Interaction
    var result = shredCommand.execute(data);

    // Verification
    verify(fileContext).read(any());
    verify(fileContext).exists(any());
    verify(fileContext, atMost(1)).write(any(), any());

    // Assertions
    assertThat(result).hasSize(1)
        .element(0)
        .isEqualTo("I am mayalying the lake.");
  }

  @Test
  void whenSourceFileReadingFailsShoulParseEmptyString() {
    // Data Preparation
    ParsingResult data = new ParsingResult(Token.command("shred"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.empty());

    // Stubbing
    when(fileContext.read(any())).thenReturn("");
    when(fileContext.exists(any())).thenReturn(true);
    when(fileContext.write(Path.of("some/path"), "")).thenReturn(true);

    // Interaction
    var result = shredCommand.execute(data);

    // Verification
    verify(fileContext).read(any());
    verify(fileContext).exists(any());
    verify(fileContext, atLeast(3)).write(Path.of("some/path"), "");

    // Assertions
    assertThat(result).hasSize(1)
        .element(0)
        .isEqualTo("");
  }
}