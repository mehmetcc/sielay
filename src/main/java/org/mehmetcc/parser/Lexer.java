package org.mehmetcc.parser;

import static org.mehmetcc.parser.ParserConstants.*;
import static org.mehmetcc.parser.TokenType.STRING;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

class Lexer {

  // This actually generates an AST (essentially a list, since the syntax is linear), so indexes are important.
  List<Token> scan(final List<String> args) {
    return args.stream()
        .map(this::match)
        .collect(Collectors.toList());
  }

  private Token match(final String lexeme) {
    return matchCommands(lexeme)
        .or(() -> matchFlags(lexeme))
        .orElseGet(() -> new Token(STRING, lexeme));
  }

  private Optional<Token> matchCommands(final String lexeme) {
    var found = COMMANDS.get(lexeme); // lmao apparently there is no api that returns another option field
    if (Objects.nonNull(found)) return Optional.of(Token.command(lexeme));
    else return Optional.empty();
  }

  private Optional<Token> matchFlags(final String lexeme) {
    var found = FLAGS.get(lexeme);
    if (Objects.nonNull(found)) return Optional.of(Token.flag(lexeme));
    else return Optional.empty();
  }
}
