package org.mehmetcc.parser;

public class Token {

  private final TokenType type;

  private final String content;

  public Token(TokenType type, String content) {
    this.type = type;
    this.content = content;
  }

  public static Token command(String lexeme) {
    return new Token(TokenType.COMMAND, lexeme);
  }

  public static Token flag(String lexeme) {
    return new Token(TokenType.FLAG, lexeme);
  }

  public TokenType getType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return "<%s:%s>".formatted(type.toString(), content);
  }
}
