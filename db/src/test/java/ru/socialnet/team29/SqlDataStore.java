package ru.socialnet.team29;

import java.sql.*;
import java.util.Properties;

public class SqlDataStore {
    private final String jdbcUrl;
    private final Properties props;

    public SqlDataStore(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
    }

    int runDummyQuery() throws SQLException {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, props);
             Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM persons;"
            );
            int res = 0;
            while (rs.next()) res++;
            rs.close();
            return res;
        }
    }

    void runCreatePersonQuery() throws SQLException {
        try (Connection conn = DriverManager.getConnection(jdbcUrl, props);
             Statement st = conn.createStatement()) {
            st.execute(
                    "CREATE TABLE persons (first_name text, last_name text);" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Alex', 'Nedovizin');" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Oleg', 'Pohilko');" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Alisa', 'Tumanskaia');" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Maxim', 'Tarasov');" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Pasha', 'Pashu');" +
                            "INSERT INTO persons(first_name,last_name) VALUES ('Vika', 'Grumbla');"
            );
        }
    }
}
