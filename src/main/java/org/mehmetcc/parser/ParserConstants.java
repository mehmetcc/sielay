package org.mehmetcc.parser;

import static org.mehmetcc.parser.TokenType.COMMAND;
import static org.mehmetcc.parser.TokenType.FLAG;

import java.util.HashMap;
import java.util.Map;

public class ParserConstants {

  static final String SHRED = "shred";
  static final String FILL_DATA = "fill-data";
  static final String DUMP_DB = "dump-db";

  static final String HELP = "--help";
  static final String HELP_SHORTENED = "-H";
  static final String VERBOSE = "--verbose";
  static final String VERBOSE_SHORTENED = "-V";
  static final String SEPERATOR = "--seperator";
  static final String SEPERATOR_SHORTENED = "-S";


  static final Map<String, TokenType> COMMANDS;

  static final Map<String, TokenType> FLAGS;

  static {
    COMMANDS = new HashMap<>();
    COMMANDS.put(SHRED, COMMAND);
    COMMANDS.put(FILL_DATA, COMMAND);
    COMMANDS.put(DUMP_DB, COMMAND);
  }

  static {
    FLAGS = new HashMap<>();
    FLAGS.put(HELP, FLAG);
    FLAGS.put(HELP_SHORTENED, FLAG);
    FLAGS.put(VERBOSE, FLAG);
    FLAGS.put(VERBOSE_SHORTENED, FLAG);
    FLAGS.put(SEPERATOR, FLAG);
    FLAGS.put(SEPERATOR_SHORTENED, FLAG);
  }
}
