package org.mehmetcc.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;


class DatabaseConnectorTest {

  @TempDir
  File temporaryDirectory;

  DatabaseConnector dut;

  @BeforeEach
  void setup() {
    dut = new DatabaseConnector(new Printer(true));

  }

  @Test
  void shouldCreateDatabase() {
    var content = List.of("a", "b", "c");
    var path = temporaryDirectory.toPath().resolve(Path.of("database"));
    var database = path.resolveSibling(path.getFileName() + ".sqlite");
    dut.fill(content, path);

    assertTrue(Files.exists(database));
  }
}