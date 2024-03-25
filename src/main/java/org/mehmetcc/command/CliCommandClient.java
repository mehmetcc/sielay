package org.mehmetcc.command;

import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParserConstants;
import org.mehmetcc.parser.ParsingResult;

public class CliCommandClient {
  public void execute(ParsingResult result) {
    Printer printer = new Printer(result.isVerbose());
    String content = result.command().getContent();

    switch (content) {
      case ParserConstants.SHRED -> new ShredCommand(printer).execute(result);
      case ParserConstants.FILL_DATA -> new FillDataCommand(printer).execute(result);
      case ParserConstants.DUMP_DB -> new DumpDatabaseCommand(printer).execute(result);
      default -> printer.printError("Error while matching commands.");
    }
  }
}

