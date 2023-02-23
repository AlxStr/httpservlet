package com.servlet.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

final public class PostgresConnectionProvider implements SQLConnectionProvider {
    private static final String HOST = "localhost";
    private static final String DATABASE = "app";
    private static final String USER = "app";
    private static final String PASSWORD = "secret";

    public void loadDriver() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    public Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        props.setProperty("ssl", "false");

        String url = String.format("jdbc:postgresql://%s:5432/%s", HOST, DATABASE);

        return DriverManager.getConnection(url, props);
    }
}
