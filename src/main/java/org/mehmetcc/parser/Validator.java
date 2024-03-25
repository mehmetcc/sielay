package org.mehmetcc.parser;

import java.util.ArrayList;
import java.util.List;

class Validator {
  // TODO BIG TODO BIG BIG
  // TODO Currently, this whole flow does not concerns itself with --help flag
  // TODO hence, if program --help is run, it would print out a failure, saying
  // TODO no commands have been given.

  private final List<String> failures;

  Validator() {
    failures = new ArrayList<>();
  }

  List<String> check(final List<Token> tokens) {
    if (checkIfEmptyList(tokens))
      return failures;
    else if (!checkIfHelp(tokens))
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
    checkIfArgumentsAreCorrect(tokens);
    checkNumberOfCommands(tokens);
    checkValidFlags(tokens);
    checkSeperator(tokens);
    checkArgumentCount(tokens);
    checkIfSeperatorIsUsedWithCorrectCommand(tokens);
  }

  private Boolean checkIfEmptyList(final List<Token> tokens) {
    if (tokens.isEmpty()) {
      failures.add("Empty command.");
      return true;
    }
    return false;
  }

  private Boolean checkIfHelp(final List<Token> tokens) {
    for (Token token : tokens) {
      if (token.getContent().equals(ParserConstants.HELP) ||
          token.getContent().equals(ParserConstants.HELP_SHORTENED))
        return true;
    }
    return false;
  }

  private void checkValidCommand(final List<Token> tokens) {
    var filtered = filterCommands(tokens);
    var validCommands = validCommands();

    for (String command : filtered) {
      if (!validCommands.contains(command)) {
        failures.add("Unknown command.");
        return;
      }
    }
  }

  private void checkFirstCommand(final List<Token> tokens) {
    var first = tokens.get(0);

    if (first.getType() != TokenType.COMMAND)
      failures.add("First token should be a command.");
  }

  private void checkIfArgumentsAreCorrect(final List<Token> tokens) {
    var first = tokens.get(0);

    if (first.getContent().equals(ParserConstants.SHRED) ||
        first.getContent().equals(ParserConstants.FILL_DATA))
      if (tokens.get(1).getType() != TokenType.STRING)
        failures.add("No path string given.");
  }

  private void checkSeperator(final List<Token> tokens) {
    var foundAtIndex = findSeperator(tokens);

    if (foundAtIndex != -1)
      if (foundAtIndex + 1 < tokens.size()) {
        var lookup = tokens.get(foundAtIndex + 1);
        if (lookup.getType() != TokenType.STRING)
          failures.add(
              "Seperator flag is declared but no seperator has been given. Seperator should be positioned just after the flag itself.");
      } else
        failures.add(
            "Seperator flag is declared but no seperator has been given. Seperator should be positioned just after the flag itself.");
  }

  private void checkIfSeperatorIsUsedWithCorrectCommand(final List<Token> tokens) {
    var foundAtIndex = findSeperator(tokens);
    var command = tokens.get(0);

    if (foundAtIndex != -1)
      if (!command.getContent().equals(ParserConstants.FILL_DATA) &&
          !command.getContent().equals(ParserConstants.DUMP_DB))
        failures.add("Seperator flag can only be used with %s and %s commands."
                .formatted(ParserConstants.FILL_DATA, ParserConstants.DUMP_DB));
  }

  private void checkArgumentCount(final List<Token> tokens) {
    var foundAtIndex = findSeperator(tokens);
    var argumentCount = tokens.stream()
        .filter(current -> current.getType() == TokenType.STRING)
        .toList()
        .size();

    if (foundAtIndex == -1) // where no seperator is around
      if (argumentCount != 1 && !tokens.get(0).getContent().equals(ParserConstants.DUMP_DB))
        failures.add("Faulty number of arguments. Either the seperator or the file path is missing.");
     else// there is a seperator
      if (argumentCount != 2)
        failures.add("Faulty number of arguments. Either the seperator or the file path is missing.");
  }

  private Integer findSeperator(final List<Token> tokens) {
    var found = -1;

    loop:
    for (int i = 0; i < tokens.size(); i++) {
      var current = tokens.get(i);
      if (current.getContent().equals(ParserConstants.SEPERATOR) ||
          current.getContent().equals(ParserConstants.SEPERATOR_SHORTENED)) {
        found = i;
        break loop; // devilishly necessary
      }
    }
    return found;
  }

  private void checkNumberOfCommands(final List<Token> tokens) {
    var filtered = getCommands(tokens);

    if (filtered.size() == 0)
      failures.add("No commands given.");
    else if (filtered.size() != 1)
      failures.add("Multiple commands given.");
  }

  private void checkValidFlags(final List<Token> tokens) {
    var flags = filterFlags(tokens);
    var validCommands = validFlags();

    for (String flag : flags) {
      if (!validCommands.contains(flag)) {
        failures.add("Unknown flag.");
        return;
      }
    }
  }

  private List<String> filterFlags(final List<Token> tokens) {
    return tokens.stream()
        .filter(current -> current.getContent().startsWith("--") || current.getContent()
            .startsWith("-"))
        .map(Token::getContent)
        .toList();
  }

  private List<String> validFlags() {
    return ParserConstants.FLAGS.keySet().stream().toList();
  }

  private List<String> filterCommands(final List<Token> tokens) {
    return tokens.stream()
        .filter(current -> current.getType() == TokenType.COMMAND)
        .map(Token::getContent)
        .toList();
  }

  private List<Token> getCommands(final List<Token> tokens) {
    return tokens.stream()
        .filter(current -> current.getType() == TokenType.COMMAND)
        .toList();
  }

  private List<String> validCommands() {
    return ParserConstants.COMMANDS.keySet()
        .stream()
        .toList();
  }
}
