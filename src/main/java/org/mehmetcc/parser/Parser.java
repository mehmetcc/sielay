package org.mehmetcc.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.mehmetcc.command.CliCommandClient;
import org.mehmetcc.io.Printer;

public class Parser {
  // TODO even though unit testing this is unnecessary, maybe consider writing some for coverage

  private final Lexer lexer;
  private final Validator validator;

  private final CliCommandClient client;

  public Parser() {
    lexer = new Lexer();
    validator = new Validator();

    this.client = new CliCommandClient();
  }

  public ParsingResult parse(final List<String> args) {
    List<Token> tokens = lexer.scan(args);
    List<String> failures = validator.check(tokens);

    if (failures.size() == 0) {
      return match(tokens);
    } else {
      var validationMessages = prettifyFailureMessages(failures);
      new Printer().printError(validationMessages);
      System.exit(0); // I hated doing this but feels like terminating the entire program when
      // there are failures should be acceptable.
    }
    return null;
  }

  private ParsingResult match(final List<Token> tokens) {
    Token command = Token.command("empty");
    Optional<Path> path = Optional.empty();

    Boolean isVerbose = false;
    Boolean isHelp = false;
    Optional<Token> seperator = Optional.empty();

    for (int i = 0; i < tokens.size(); i++) {
      // Traverse the token list and match based on Token::getType
      var current = tokens.get(i);
      if (current.getType() == TokenType.COMMAND) { // Set command
        command = current;
        if (command.getContent().equals(ParserConstants.DUMP_DB)) { // fill the path acc. to dump-db
          if (tokens.get(i + 1).getType().equals(TokenType.STRING)) {
            path = Optional.of(Path.of(tokens.get(i + 1).getContent()));
          }
        } else { // fill the path for remaining commands
          path = Optional.of(Path.of(tokens.get(i + 1).getContent()));
        } // Setting command and path ends here
      } else if (current.getType() == TokenType.FLAG) { // Set flags
        switch (current.getContent()) {
          case ParserConstants.VERBOSE, ParserConstants.VERBOSE_SHORTENED -> isVerbose = true;
          case ParserConstants.HELP, ParserConstants.HELP_SHORTENED -> isHelp = true;
          case ParserConstants.SEPERATOR, ParserConstants.SEPERATOR_SHORTENED ->
              seperator = Optional.of(tokens.get(i + 1));
        }
      }
    }

    return new ParsingResult(command, path, isVerbose, isHelp, seperator);
  }

  private String prettifyFailureMessages(final List<String> failures) {
    StringBuilder base = new StringBuilder(
        "There are couple of problems with the command entered:\n");

    for (int i = 0; i < failures.size(); i++) {
      base.append("%d. %s\n".formatted(i + 1, failures.get(i)));
    }

    return base.toString();
  }
}
