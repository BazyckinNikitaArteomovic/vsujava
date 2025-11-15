package ru.vsu.cs.bazykin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2ConnectionManager {
    private static final String DB_URL = "jdbc:h2:" + System.getProperty("java.io.tmpdir") + "/archive_db";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initializeSchema();
        }
        return connection;
    }

    private static void initializeSchema() {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS archives (" +
                    "id VARCHAR(36) PRIMARY KEY, " +
                    "archive_path VARCHAR(500) NOT NULL" +
                    ")");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize H2 schema", e);
        }
    }
}