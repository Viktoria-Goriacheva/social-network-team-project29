package ru.socialnet.team29;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Testcontainers
public class TestApp {

    static SqlDataStore sqlDataStore;

    @Container
    final static PostgreSQLContainer postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
        postgresContainer.start();

        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }

    @BeforeAll
    public static void init() {
        sqlDataStore =
                new SqlDataStore(
                        postgresContainer.getJdbcUrl(),
                        postgresContainer.getUsername(),
                        postgresContainer.getPassword()
                );
    }

    @Test
    public void runCreatePersonQuery() throws SQLException {
        sqlDataStore.runCreatePersonQuery();
    }

    @Test
    public void runDummyQuery() throws SQLException {
        int count = sqlDataStore.runDummyQuery();
        assertEquals(count, 6);
    }
}
