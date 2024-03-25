package org.mehmetcc.command;

import java.util.List;
import org.mehmetcc.parser.ParsingResult;

interface CliCommand {
  List<String> execute(final ParsingResult result);
}
