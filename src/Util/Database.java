package Util;

import java.sql.*;

/**
 * @author Eric Bostard
 *
 * Database Connection
 */
public class Database {
    //variables used to connect to database
    private static final String databaseName = "client_schedule";
    private static final String databaseURL = "jdbc:mysql://localhost:3306/" + databaseName;
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    private static PreparedStatement ps;

    public static Connection openConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(databaseURL, username, password);
        return conn;
    }

    public static void closeConnection(Connection conn) throws SQLException{
        conn.close();
    }

    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    public static void setPreppedStatement(Connection conn, String sqlStatement) throws SQLException {
        ps = conn.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreppedStatement() {
        return ps;
    }

    public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
    }

    public static void closeResultSet(ResultSet resultSet) throws SQLException {
        resultSet.close();
    }
}
