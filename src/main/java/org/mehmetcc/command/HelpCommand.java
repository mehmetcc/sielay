package org.mehmetcc.command;

import java.util.ArrayList;
import java.util.List;
import org.mehmetcc.io.Printer;
import org.mehmetcc.parser.ParsingResult;

public class HelpCommand {
  private final Printer printer;

  public HelpCommand() {
    this.printer = new Printer(true);
  }

  public List<String> execute() {
    var all = new ArrayList<String>();
    all.addAll(mainMenu());
    all.addAll(commands());
    all.addAll(flags());

    all.forEach(printer::printLine);

    return all;
  }

  private List<String> mainMenu() {
    List<String> mainMenu = new ArrayList<>();
    mainMenu.add("sielay [command] [path] [flags...]");
    mainMenu.add("or");
    mainMenu.add("sielay [command] [flags...]");
    return mainMenu;
  }

  private List<String> commands() {
    List<String> commands = new ArrayList<>();
    commands.add("shred: overwrite file contents N times before deleting it.");
    commands.add("usage: shred [path] [flags]");
    commands.add("fill-data: fill given path with predetemined data.");
    commands.add("usage: fill-data [path] [flags]");
    commands.add("dump-db: dump binary data to an sqlite database.");
    commands.add("usage: dump-db [path:optional] [flags]");
    return commands;
  }

  private List<String> flags() {
    List<String> flags = new ArrayList<>();
    flags.add("--verbose -V enables debugging mode.");
    flags.add("--seperator -S enables custom seperator. Only available for fill-data and dump-db commands. usage is optional.");
    flags.add("--help -H opens up help menu");
    return flags;
  }
}
