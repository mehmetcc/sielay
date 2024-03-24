package org.mehmetcc.context;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mehmetcc.io.Printer;


class ApplicationContextDeserializerTest {

  PrintStream standardOut;
  ByteArrayOutputStream outputStreamCaptor;
  ApplicationContextDeserializer dut;
  @TempDir
  private File temporaryDirectory;
  @TempDir
  private File temporaryApplicationContextFile;

  @BeforeEach
  void setup() throws IOException {
    temporaryApplicationContextFile = new File(temporaryDirectory, "application-context.json");
    Files.writeString(temporaryApplicationContextFile.toPath(),
        "{\"didRunFillDatabaseBefore\":false,\"lastFillDatabaseContent\":\"fdsfsd\",\"seperator\":\";\"}");

    dut = new ApplicationContextDeserializer(new Printer(true));

    standardOut = System.out;
    outputStreamCaptor = new ByteArrayOutputStream();

    System.setOut(new PrintStream(outputStreamCaptor));
  }

  @Test
  void shouldDeserialize() {
    var output = dut.deserialize();
    var expected = new ApplicationContext(false, "fdsfsd", ";");

    assertAll(
        () -> assertTrue(Files.exists(temporaryApplicationContextFile.toPath()),
            "File should exist"),
        () -> assertEquals(output, expected));
  }
}