package org.mehmetcc.parser;

import java.util.ArrayList;
import java.util.List;

class Validator {

  private final List<String> failures;

  Validator() {
    failures = new ArrayList<>();
  }

  List<String> check(final List<Token> tokens) {
    runValidations(tokens);
    return failures;
  }

  private void runValidations(final List<Token> tokens) {
    // Following functions used to return Option[List[Token]] and chained to one another,
    // however I decided that is not the Java-way of doing this. Also does not make any sense
    // for following up on functional programming conventions as well, since we are already
    // manipulating data outside of Kleisli's scope, hence this current setup.
    checkFirstCommand(tokens);
    checkValidCommand(tokens);
    checkMultipleCommands(tokens);
    checkValidFlags(tokens);
    checkSeperator(tokens);
  }

  private void checkValidCommand(final List<Token> tokens) {
    var filtered = tokens.stream()
        .filter(current -> current.getType() == TokenType.COMMAND)
        .map(Token::getContent)
        .toList();
    var valid = ParserConstants.COMMANDS.keySet().stream().toList();

    for (String command : filtered) {
      if (!valid.contains(command)) {
        failures.add("Unknown command.");
        return;
      }
    }
  }

  private void checkFirstCommand(final List<Token> tokens) {
    var first = tokens.get(0);

    if (first.getType() != TokenType.COMMAND) {
      failures.add("First token should be a command.");
    }
  }

  private void checkSeperator(final List<Token> tokens) {
    var foundAtIndex = findSeperator(tokens);

    if (foundAtIndex != -1) {
      if (foundAtIndex + 1 < tokens.size()) {
        var lookup = tokens.get(foundAtIndex + 1);
        if (lookup.getType() != TokenType.STRING) {
          failures.add("Seperator flag is declared but no seperator has been given.");
        }
      } else {
        failures.add("Seperator flag is declared but no seperator has been given.");
      }
    }
  }

  private Integer findSeperator(final List<Token> tokens) {
    var found = -1;

    loop:
    for (int i = 0; i < tokens.size(); i++) {
      var current = tokens.get(i);
      if (current.getContent().equals(ParserConstants.SEPERATOR) || current.getContent()
          .equals(ParserConstants.SEPERATOR_SHORTENED)) {
        found = i;
        break loop; // devilishly necessary
      }
    }
    return found;
  }

  private void checkMultipleCommands(final List<Token> tokens) {
    var filtered = tokens.stream()
        .filter(current -> current.getType() == TokenType.COMMAND)
        .toList();

    if (filtered.size() != 1) {
      failures.add("Multiple commands given");
    }
  }

  private void checkValidFlags(final List<Token> tokens) {
    var flags = tokens.stream()
        .filter(current -> current.getContent().startsWith("--") || current.getContent()
            .startsWith("-"))
        .map(Token::getContent)
        .toList();
    var valid = ParserConstants.FLAGS.keySet().stream().toList();

    for (String flag : flags) {
      if (!valid.contains(flag)) {
        failures.add("Unknown flag.");
        return;
      }
    }
  }
}
