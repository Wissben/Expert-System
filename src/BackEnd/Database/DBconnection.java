package BackEnd.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBconnection {

    private String connectionURL;
    private String username;
    private String password;
    private String DBname;
    private Connection connection;

    public DBconnection(String connectionURL, String username, String password, String DBname) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.DBname = DBname;
        this.setConnection();
    }

    private void setConnection()
    {
        try
        {
            System.out.println(connectionURL+DBname);
            this.connection = DriverManager.getConnection(this.connectionURL+this.DBname, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
