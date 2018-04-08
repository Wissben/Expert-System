package BackEnd.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBQuery
{

    private DBconnection dbConnection;
    private String query;

    public DBQuery(DBconnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    public DBQuery()
    {
    }

    public ResultSet executeQuery()
    {
        ResultSet resultSet = null;
        try
        {
            Statement command = this.dbConnection.getConnection().createStatement();
            resultSet = command.executeQuery(this.query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdateQuery()
    {
        int resultSet = -1;
        try
        {
            Statement command = this.dbConnection.getConnection().createStatement();
            resultSet = command.executeUpdate(this.query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void insertQuery(String tableName, String... values)
    {
        this.query = "INSERT INTO " + tableName + " VALUES(";
        for (String value : values)
        {
            this.query += value + ",";
        }
        query = query.substring(0, query.length() - 1);
        this.query += ")";
        this.setQuery(query);


    }

    public void dropTableQuery(String tableName)
    {
        this.query = "DROP TABLE " + tableName + ";";
        this.setQuery(query);
        int resultSet = -1;
        try
        {
            Statement command = this.dbConnection.getConnection().createStatement();
            resultSet = command.executeUpdate(this.query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void selectQuery(String tableName, ArrayList<String> columns, ArrayList<String> conditions)
    {
        //TODO easy
    }

    public void createTableQuerry(String tableName, String... columns)
    {
        this.query = generateCreateTableQuery(tableName, columns);
        this.setQuery(query);
    }


    public String generateCreateTableQuery(String tableName, String... columns)
    {
        String query;
        query = "CREATE TABLE " + tableName + " (\n";
        for (int i = 0; i < columns.length - 1; i += 2)
        {
            query += columns[i] + " " + columns[i + 1] + ",\n";
        }

        for (String comp : columns)
        {

        }
        query = query.substring(0, query.length() - 2);
        query += "\n);";
        return query;
    }

    public DBconnection getDbConnection()
    {
        return dbConnection;
    }

    public void setDbConnection(DBconnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }
}
