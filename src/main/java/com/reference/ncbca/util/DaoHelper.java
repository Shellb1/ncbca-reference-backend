package com.reference.ncbca.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoHelper {

    public static Connection connect(String database) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(database);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
