package org.mehmetcc.command;

import org.mehmetcc.parser.ParsingResult;

interface CliCommand {

  void execute(final ParsingResult result);
}
