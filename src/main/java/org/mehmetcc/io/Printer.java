package org.mehmetcc.io;

public class Printer {

  private final Boolean isVerbose;

  public Printer(final Boolean isVerbose) {
    this.isVerbose = isVerbose;
  }

  public Printer() {
    this.isVerbose = false;
  }

  public void printLine(String content) {
    if (isVerbose) {
      System.out.println(content);
    }
  }

  public void printError(String content) {
    System.out.println(content);
  }
}
