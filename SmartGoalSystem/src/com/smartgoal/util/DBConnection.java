package com.smartgoal.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Utility class for JDBC connection to Oracle.
 * Update DB_URL, DB_USER, DB_PASS to match your Oracle Database environment.
 */
public class DBConnection {

    // Default Oracle thin client URL (xe is the default Express Edition service name)
    // Change "xe" to "orcl" or "FREE" if your database has a different service name.
    private static final String DB_URL  = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "System"; // change to your Oracle username
    private static final String DB_PASS = "manager"; // change to your Oracle password

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
