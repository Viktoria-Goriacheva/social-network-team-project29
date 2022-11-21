package ru.socialnet.team29;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
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
