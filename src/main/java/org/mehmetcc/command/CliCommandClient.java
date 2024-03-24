package org.mehmetcc.command;

import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParserConstants;
import org.mehmetcc.parser.ParsingResult;

public class CliCommandClient {

  public void execute(ParsingResult result) {
    Printer printer = new Printer(result.isVerbose());
    String content = result.command().getContent();

    if (content.equals(ParserConstants.SHRED)) {
      new ShredCommand(printer).execute(result);
    } else if (content.equals(ParserConstants.FILL_DATA)) {
      new FillDataCommand(printer).execute(result);
    } else {
      printer.printError("Error while matching commands.");
    }
  }
}

