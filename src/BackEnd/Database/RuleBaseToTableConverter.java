package BackEnd.Database;

import BackEnd.ExpertSys.VariableMapper;
import BackEnd.RuleBase;
import BackEnd.Types.DoubleValue;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;
import BackEnd.Types.VariableValue;
import BackEnd.Variable;
import FrontEnd.Main;

import java.util.HashMap;

public class RuleBaseToTableConverter
{
    static public String tableName = "Product";
    private HashMap<String, String> mapColNameToType;
    private DBconnection dBconnection;
    private DBQuery dbQuery;
    protected VariableMapper mapper;
    static protected RuleBaseToTableConverter _instance = null;


    private RuleBaseToTableConverter(DBconnection dBconnection)
    {
        this.dBconnection = dBconnection;
        this.dbQuery = new DBQuery(dBconnection);
        this.generateTableColumns();
    }

    public VariableMapper generateProductMapper()
    {
        if(this.mapper == null)
        {
            VariableMapper mapper = new VariableMapper();
            mapper.addVariableValue("agentID", new IntegerValue(0));
            mapper.addVariableValue("name", new StringVariableValue(""));
            mapper.addVariableValue("Season", new StringVariableValue(""));
            mapper.addVariableValue("Usages", new StringVariableValue(""));
            mapper.addVariableValue("Position", new StringVariableValue(""));
            mapper.addVariableValue("Size", new StringVariableValue(""));
            mapper.addVariableValue("Article", new StringVariableValue(""));
            mapper.addVariableValue("Material", new StringVariableValue(""));
            mapper.addVariableValue("Gender", new StringVariableValue(""));
            mapper.addVariableValue("SleeveLength", new StringVariableValue(""));
            mapper.addVariableValue("Length", new StringVariableValue(""));
            mapper.addVariableValue("Price", new DoubleValue(0));
            this.mapper = mapper;
        }
        return this.mapper;
    }

    static public RuleBaseToTableConverter getInstance()
    {
        if(_instance ==null) _instance = new RuleBaseToTableConverter(Main.getdBconnection());
        return _instance;
    }


    private void generateTableColumns()
    {

        this.mapColNameToType = new HashMap<>();

        mapColNameToType.put(tableName, "VARCHAR(255)");
        mapColNameToType.put("id", "INT NOT NULL PRIMARY KEY");
        mapColNameToType.put("name", "VARCHAR(255)");
        mapColNameToType.put("agentID", "INT");

        VariableMapper mapper = generateProductMapper();
        for (String name : mapper.getVariables())
        {
            mapColNameToType.put(name, getTypeFrom(mapper.getVariableValue(name)));
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
        System.out.println(dbQuery.getQuery());
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
