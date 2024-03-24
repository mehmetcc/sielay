package org.mehmetcc.parser;

public class Parser {

  private final Lexer lexer;
  private final Validator validator;

  public Parser() {
    lexer = new Lexer();
    validator = new Validator();
  }
}
