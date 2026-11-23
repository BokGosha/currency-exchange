package com.currencyexchange.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class HikariDataSourceFactory {

    private static HikariDataSource dataSource;

    private HikariDataSourceFactory() {
    }

    public static void create() {
        if (dataSource != null) {
            return;
        }

        HikariConfig config = new HikariConfig("/hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource не инициализирован");
        }

        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
