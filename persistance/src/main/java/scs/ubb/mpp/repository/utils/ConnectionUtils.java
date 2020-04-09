package scs.ubb.mpp.repository.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionUtils {
    private Connection connection;
    private Statement statement;
    private static ConnectionUtils instance = null;

    public ConnectionUtils() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mpp", "root", "root");
        statement = connection.createStatement();
    }

    public static ConnectionUtils getInstance() {
        if (instance == null) {
            try {
                instance = new ConnectionUtils();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public Statement getStatement() {
        return this.statement;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
