package com.servlet.provider;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLConnectionProvider {
    public Connection getConnection() throws SQLException;
    public void loadDriver() throws ClassNotFoundException;
}
