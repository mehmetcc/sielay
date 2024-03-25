package org.mehmetcc.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;


public class DatabaseConnector {

  private final Printer printer;

  public DatabaseConnector(final Printer printer) {
    this.printer = printer;
  }

  public void fill(final List<String> content) {
    createDirectory();

    var databasePath = "databases/" + UUID.randomUUID().toString();
    executeStatement(content, databasePath);
  }

  public void fill(final List<String> content, Path path) {
    executeStatement(content, path.toString());
  }

  private void executeStatement(final List<String> content, final String databasePath) {
    String url = "jdbc:sqlite:" + databasePath + ".sqlite";

    // This can be divided into sub-functions, but I did not want to mess with control
    // of managed resources
    try (Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();) {
      DatabaseMetaData meta = connection.getMetaData();
      printer.printLine("The driver name is " + meta.getDriverName());
      printer.printLine("A new database under %s has been created.".formatted(databasePath));

      statement.setQueryTimeout(30); // in seconds
      statement.execute("create table lines (id integer, line string)");
      printer.printLine("Database table <lines> have been created");

      for (int i = 0; i < content.size(); i++) {
        statement.executeUpdate("insert into lines values(%d, '%s')".formatted(i, content.get(i)));
      }

    } catch (SQLException e) {
      printer.printError("Failed to fill in the database.");
      printer.printError(e.getMessage());
    }
  }

  private void createDirectory() {
    try {
      Files.createDirectories(Paths.get("databases"));
    } catch (IOException e) {
      printer.printError("Error happened while creating databases subdirectory");
    }
  }
}
