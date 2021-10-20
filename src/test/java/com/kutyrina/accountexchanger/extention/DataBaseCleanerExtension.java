package com.kutyrina.accountexchanger.extention;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseCleanerExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("example");
        DataSource dataSource = new HikariDataSource(hikariConfig);
        try (Connection connection = dataSource.getConnection()) {
            executeStatement(connection, "DELETE from account");
            executeStatement(connection, "DELETE from client");
            executeStatement(connection, "insert into client values (1, 'TestLogin' ,'$2a$10$BFa7UWLx7pVPlyUWSGdCiePfyLi4.noJoNXoQIoZCoQkqG5P43FBa')");
            executeStatement(connection, "insert into client values (2, 'TestLogin1' ,'$2a$10$LaV8gDHTM3KJkfqrWxHTl.hxEMoPYU2IT3k6INpPl.2A8wK30V5GW')");
            executeStatement(connection, "insert into account values (1, 500 ,1)");
            executeStatement(connection, "insert into account values (2, 500 ,2)");
        }
    }

    private void executeStatement(Connection connection, String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
        }
    }
}
