package org.mehmetcc.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidatorTest {

  Validator dut;

  @BeforeEach
  void setup() {
    dut = new Validator();
  }

  @Test
  void validStatementGivenShouldValidateWithEmptyList() {
    var input = List.of(Token.command("dump-db"), new Token(TokenType.STRING, "/some/path/here"));
    var output = dut.check(input);

    assertThat(output).isEmpty();
  }

  @Test
  void noTokensGivenShouldGenerateFailure() {
    var input = new ArrayList<Token>();
    var output = dut.check(input);

    assertThat(output).isNotEmpty().contains("Empty command.");
  }

  @Test
  void noCommandGivenShouldGenerateFailure() {
    var input = List.of(Token.flag("--hehe"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty().contains("No commands given.");
  }

  @Test
  void unknownFlagGivenShouldGenerateFailure() {
    var input = List.of(Token.command("dump-db"), new Token(TokenType.STRING, "my/file/is/here"),
        Token.flag("--hoho"));
    var output = dut.check(input);

    assertThat(output).hasSize(1)
        .element(0)
        .isEqualTo("Unknown flag.");
  }

  @Test
  void nonCommandFirstTokenGivenShouldGenerateFailure() {
    var input = List.of(Token.flag("--hoho"), Token.command("dump-db"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("First token should be a command.");
  }

  @Test
  void unknownCommandGivenShouldGenerateFailure() {
    var input = List.of(Token.command("hehe"), Token.flag("--hoho"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("Unknown command.");
  }

  @Test
  void multipleCommandsGivenShouldGenerateFailure() {
    var input = List.of(Token.command("dump-db"),
        new Token(TokenType.STRING, "/some/path/here"),
        Token.command("shred"));
    var output = dut.check(input);

    assertThat(output).hasSize(1)
        .element(0)
        .isEqualTo("Multiple commands given.");
  }

  @Test
  void seperatorDeclaredAsLastLexemeButNotGivenShouldGenerateFailure() {
    var input = List.of(Token.command("dump-db"), new Token(TokenType.STRING, "/some/path/here"),
        Token.flag("--seperator"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains(
            "Seperator flag is declared but no seperator has been given. Seperator should be positioned just after the flag itself.");
  }

  @Test
  void seperatorDeclaredButNotGivenShouldGenerateFailure() {
    var input = List.of(Token.command("dump-db"), new Token(TokenType.STRING, "/some/path/here"),
        Token.flag("--seperator"), Token.flag("--help"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains(
            "Seperator flag is declared but no seperator has been given. Seperator should be positioned just after the flag itself.");
  }

  @Test
  void multipleArgumentsGivenShouldGenerateFailure() {
    var input = List.of(Token.command("shred"),
        new Token(TokenType.STRING, "/some/path/here"),
        new Token(TokenType.STRING, "/some/other/path/here"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("Faulty number of arguments. Either the seperator or the file path is missing.");
  }

  @Test
  void multipleArgumentsWithSeperatorGivenShouldGenerateFailure() {
    var input = List.of(Token.command("dump-db"),
        new Token(TokenType.STRING, "/some/path/here"),
        Token.flag("--seperator"),
        new Token(TokenType.STRING, ":::"),
        new Token(TokenType.STRING, "/some/other/path/here"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("Faulty number of arguments. Either the seperator or the file path is missing.");
  }

  @Test
  void seperatorFlagUsedWithFaultyCommandShouldGenerateFailure() {
    var input = List.of(Token.command("shred"),
        Token.flag("--seperator"),
        new Token(TokenType.STRING, ";"));
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("Seperator flag can only be used with fill-data and dump-db commands.");
  }

  @Test
  void pathRequiringCommandWithNoPathGivenShouldGenerateFailure() {
    var input = List.of(Token.command("fill-data"),
        Token.flag("--seperator"),
        new Token(TokenType.STRING, ":::")
    );
    var output = dut.check(input);

    assertThat(output).isNotEmpty()
        .contains("No path string given.");
  }
}