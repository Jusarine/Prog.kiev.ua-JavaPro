package com.mysql.jdbc;

        import java.io.IOException;
        import java.io.InputStream;
        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.util.Properties;

public class ConnectionFactory {
    private String url;
    private String user;
    private String password;

    public ConnectionFactory() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");

        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        url = props.getProperty("db.url");
        user = props.getProperty("db.user");
        password = props.getProperty("db.password");
    }

    public Connection getConnection()  {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
