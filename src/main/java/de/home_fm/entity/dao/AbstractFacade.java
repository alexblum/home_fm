package de.home_fm.entity.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class AbstractFacade {

  private final Connection connection;

  protected Connection getConnection() {
    return connection;
  }

  public AbstractFacade() throws IOException, SQLException {
    Properties properties = new Properties();
    properties.load(this.getClass().getResourceAsStream("/db.properties"));

    String host = properties.getProperty("host");
    String database = properties.getProperty("database");
    String user = properties.getProperty("user");
    String password = properties.getProperty("password");

    connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + database, user, password);
  }
}
