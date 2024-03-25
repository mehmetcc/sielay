package org.mehmetcc.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mehmetcc.context.ApplicationContext;
import org.mehmetcc.context.ApplicationContextDeserializer;
import org.mehmetcc.io.DatabaseConnector;
import org.mehmetcc.io.FileContext;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;
import org.mehmetcc.parser.Token;
import org.mehmetcc.parser.TokenType;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DumpDatabaseCommandTest {
  @Mock
  Printer printer;

  @Mock
  FileContext fileContext;

  @Mock
  DatabaseConnector databaseConnector;

  @Mock
  ApplicationContextDeserializer applicationContextDeserializer;

  @InjectMocks
  DumpDatabaseCommand dumpDatabaseCommand;

  @Test
  void whenPathIsNotSetShouldFetchFromApplicationContext() {
    // Data Preparation
    var data = new ParsingResult(Token.command("dump-db"),
        Optional.empty(),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";")));
    var context = new ApplicationContext("hebele hübele", ":::");

    // Stubbing
    when(applicationContextDeserializer.deserialize()).thenReturn(context);

    // Interaction
    var result = dumpDatabaseCommand.execute(data);

    // Verification
    verify(applicationContextDeserializer).deserialize();
    verify(databaseConnector).fill(List.of("hebele hübele"));

    // Assertions
    assertThat(result).hasSize(1)
        .element(0)
        .isEqualTo(context.lastFillDatabaseContent());
  }

  @Test
  void whenPathIsSetButSeperatorIsNotAroundShouldExtractFromFileAndUseDefaultSeperator() {
    // Data Preparation
    var data = new ParsingResult(Token.command("dump-db"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.empty());

    // Stubbing
    when(fileContext.read("some\\path")).thenReturn("ehehe:::bisiler:::yazdim");
    when(fileContext.exists(any())).thenReturn(true);
    doNothing().when(databaseConnector).fill(List.of("ehehe", "bisiler", "yazdim"));

    // Interaction
    var result = dumpDatabaseCommand.execute(data);

    // Verification
    verify(fileContext).read("some\\path");
    verify(fileContext).exists(any());
    verify(databaseConnector).fill(List.of("ehehe", "bisiler", "yazdim"));

    // Assertions
    assertThat(result).isNotEmpty()
        .hasSize(3)
        .isEqualTo(List.of("ehehe", "bisiler", "yazdim"));
  }

  @Test
  void whenPathAndSeperatorIsSetShouldExtractAccordingly() {
    // Data Preparation
    var data = new ParsingResult(Token.command("dump-db"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";;")));

    // Stubbing
    when(fileContext.read("some\\path")).thenReturn("ehehe;;bisiler;;yazdim");
    when(fileContext.exists(any())).thenReturn(true);
    doNothing().when(databaseConnector).fill(List.of("ehehe", "bisiler", "yazdim"));

    // Interaction
    var result = dumpDatabaseCommand.execute(data);

    // Verification
    verify(fileContext).read("some\\path");
    verify(fileContext).exists(any());
    verify(databaseConnector).fill(List.of("ehehe", "bisiler", "yazdim"));

    // Assertions
    assertThat(result).isNotEmpty()
        .hasSize(3)
        .isEqualTo(List.of("ehehe", "bisiler", "yazdim"));
  }

  @Test
  void whenNonExistingPathGivenShouldReturnEmptyListAndTerminate() {
    // Data Preparation
    var data = new ParsingResult(Token.command("dump-db"),
        Optional.of(Path.of("some/path")),
        false,
        false,
        Optional.of(new Token(TokenType.STRING, ";;")));

    // Stubbing
    when(fileContext.exists(any())).thenReturn(false);

    // Interaction
    var result = dumpDatabaseCommand.execute(data);

    // Verification
    verify(fileContext).exists(any());

    // Assertions
    assertThat(result).isEmpty();
  }
}