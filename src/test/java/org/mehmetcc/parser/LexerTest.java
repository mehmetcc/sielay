package org.mehmetcc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LexerTest {

  Lexer dut;

  @BeforeEach
  void setup() {
    dut = new Lexer();
  }

  @Test
  void emptyArgsGivenShouldReturnEmptyList() {
    List<String> input = new ArrayList<>();
    var output = dut.scan(input);

    assertThat(output).isEmpty();
  }

  @Test
  void noCommandOrFlagGivenShouldScanString() {
    var command = "son-feci-bisikletin-basscisi";
    var input = Collections.singletonList(command);
    var output = dut.scan(input);

    assertThat(output).hasSize(1)
        .element(0)
        .extracting(Token::getContent, Token::getType)
        .containsExactly(command, TokenType.STRING);

  }

  @ParameterizedTest
  @ValueSource(strings = {"shred", "fill-data", "dump-db"})
  void singleCommandGivenShouldScanCommandsAndGenerateSingletonList(String command) {
    var input = Collections.singletonList(command); // simulating args over here
    var output = dut.scan(input);

    assertThat(output).hasSize(1)
        .element(0)
        .extracting(Token::getContent, Token::getType)
        .containsExactly(command, TokenType.COMMAND);
  }

  @ParameterizedTest
  @ValueSource(strings = {"shred", "fill-data", "dump-db"})
  void singleCommandWithStringFieldGivenShouldScanAndGenerateList(String command) {
    var path = "burada/dosya/var";
    var statement = "%s %s".formatted(command, path);
    var input = Arrays.asList(statement.split(" "));
    var output = dut.scan(input);

    assertThat(output).hasSize(2)
        .element(0)
        .extracting(Token::getContent, Token::getType)
        .containsExactly(command, TokenType.COMMAND);
    assertThat(output)
        .element(1)
        .extracting(Token::getContent, Token::getType)
        .containsExactly(path, TokenType.STRING);
  }
}