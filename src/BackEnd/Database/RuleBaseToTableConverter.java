package BackEnd.Database;

import BackEnd.RuleBase;
import BackEnd.Types.DoubleValue;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;
import BackEnd.Types.VariableValue;

import java.util.HashMap;

public class RuleBaseToTableConverter
{
    private RuleBase ruleBase;
    private String tableName;
    private HashMap<String, String> mapColNameToType;
    private DBconnection dBconnection;
    private DBQuery dbQuery;


    public RuleBaseToTableConverter(RuleBase ruleBase, String tableName, DBconnection dBconnection)
    {
        this.ruleBase = ruleBase;
        this.tableName = tableName;
        this.dBconnection = dBconnection;
        this.dbQuery = new DBQuery(dBconnection);
        this.generateTableColumns();
    }

    private void generateTableColumns()
    {

        this.mapColNameToType = new HashMap<>();

        mapColNameToType.put(tableName, "VARCHAR(255)");
        mapColNameToType.put("id", "INT NOT NULL PRIMARY KEY");
        mapColNameToType.put("name", "VARCHAR(255)");
        mapColNameToType.put("agentID", "INT");

        for (String name : ruleBase.getVariableList().keySet())
        {
            mapColNameToType.put(name, getTypeFrom(ruleBase.getVariableList().get(name).getValue()));
        }
        /**All hail the BRICOLAGE
         mapColNameToType.remove("Agents");
         **/
    }


    public void createTableQuery()
    {
        dbQuery = new DBQuery(dBconnection);
        String[] columns = new String[this.mapColNameToType.size() * 2];
        int index = 0;
        for (String name : this.mapColNameToType.keySet())
        {
            columns[index] = name;
            columns[index + 1] = this.mapColNameToType.get(name);
            index += 2;
        }
        dbQuery.createTableQuerry(this.tableName, columns);

    }


    public String getTypeFrom(VariableValue variableValue)
    {

        if (variableValue instanceof StringVariableValue)
        {
            return "VARCHAR(255)";
        }
        if (variableValue instanceof IntegerValue)
        {
            return "INT";
        }
        if (variableValue instanceof DoubleValue)
        {
            return "FLOAT";
        }
        return "VARCHAR(200)";

    }


    public RuleBase getRuleBase()
    {
        return ruleBase;
    }

    public void setRuleBase(RuleBase ruleBase)
    {
        this.ruleBase = ruleBase;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }


    public HashMap<String, String> getMapColNameToType()
    {
        return mapColNameToType;
    }

    public void setMapColNameToType(HashMap<String, String> mapColNameToType)
    {
        this.mapColNameToType = mapColNameToType;
    }

    public DBconnection getdBconnection()
    {
        return dBconnection;
    }

    public void setdBconnection(DBconnection dBconnection)
    {
        this.dBconnection = dBconnection;
    }

    public DBQuery getDbQuery()
    {
        return dbQuery;
    }

    public void setDbQuery(DBQuery dbQuery)
    {
        this.dbQuery = dbQuery;
    }
}
