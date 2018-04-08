package BackEnd.Database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBQuerry {

    private DBconnection dbConnection;
    private String querry;

    public DBQuerry(DBconnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void executeQuerry(String querry) {
        try {
            Statement command = this.dbConnection.getConnection().createStatement();
            command.execute(querry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertQuerry(String tableName,ArrayList<String> values)
    {
        //TODO easy
    }

    public void selectQuerry(String tableName, ArrayList<String> columns,ArrayList<String> conditions)
    {
        //TODO easy
    }
    public void createTableQuerry(String tableName, ArrayList<String> component)
    {
        try {
            Statement command = this.dbConnection.getConnection().createStatement();
            querry = "CREATE TABLE "+tableName+" (\n";
            for (String comp:component) {
                querry+=comp+",\n";
            }
            querry=querry.substring(0,querry.length()-2);
            querry+="\n);";
            System.out.println(querry);
            command.execute(querry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DBconnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DBconnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public String getQuerry() {
        return querry;
    }

    public void setQuerry(String querry) {
        this.querry = querry;
    }
}
