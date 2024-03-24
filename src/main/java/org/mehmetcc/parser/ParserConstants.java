package org.mehmetcc.parser;

import static org.mehmetcc.parser.TokenType.COMMAND;
import static org.mehmetcc.parser.TokenType.FLAG;

import java.util.HashMap;
import java.util.Map;

public class ParserConstants {

  public static final String SHRED = "shred";
  public static final String FILL_DATA = "fill-data";
  public static final String DUMP_DB = "dump-db";

  public static final String HELP = "--help";
  public static final String HELP_SHORTENED = "-H";
  public static final String VERBOSE = "--verbose";
  public static final String VERBOSE_SHORTENED = "-V";
  public static final String SEPERATOR = "--seperator";
  public static final String SEPERATOR_SHORTENED = "-S";


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
