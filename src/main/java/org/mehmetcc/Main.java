package org.mehmetcc;

import java.util.Arrays;
import java.util.List;
import org.mehmetcc.command.CliCommandClient;
import org.mehmetcc.parser.Parser;
import org.mehmetcc.parser.ParsingResult;

public class Main {

  public static void main(String[] args) {
    Parser parser = new Parser();
    CliCommandClient client = new CliCommandClient();
    
    //List<String> arguments = List.of("shred", "C:\\Users\\Knuth\\Desktop\\Projects\\sielay\\sadadsads.txt", "-V");

    List<String> arguments = Arrays.asList(args);
    ParsingResult result = parser.parse(arguments);
    client.execute(result);
  }
}