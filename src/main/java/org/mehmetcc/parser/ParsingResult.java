package org.mehmetcc.parser;

import java.nio.file.Path;
import java.util.Optional;

public record ParsingResult(Token command,
                            Optional<Path> path,
                            Boolean isVerbose,
                            Boolean isHelp,
                            Optional<Token> seperator) {

}
