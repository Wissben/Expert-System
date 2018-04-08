package BackEnd.Database;

import BackEnd.ExpertSys.VariableMapper;
import FrontEnd.Main;

public class FeaturesToQuerryConverter
{

    private VariableMapper variableMapper;
    private DBQuery dbQuery;
    private RuleBaseToTableConverter ruleBaseToTableConverter;

    public FeaturesToQuerryConverter(VariableMapper variableMapper, RuleBaseToTableConverter ruleBaseToTableConverter)
    {
        this.ruleBaseToTableConverter = ruleBaseToTableConverter;
        this.variableMapper = variableMapper;
        this.dbQuery = new DBQuery(Main.dBconnection);

    }

    public void generateQuery()
    {
        String query;
        query = "SELECT *";
        query += " FROM " + ruleBaseToTableConverter.getTableName();
        query += " WHERE ";
        query += generateConditionsFromFeatures();
        query += ";";

//      System.out.println("QUERY IS : "+query);

        this.dbQuery.setQuery(query);
    }

    private String generateSelectedColumnsFromFeatures()
    {
        String separator = ",";
        String[] featuresNames = this.variableMapper.getVariables();
        String cols = "";
        for (String featureName : featuresNames)
        {
            cols += featureName + separator;
        }
        cols = cols.substring(0, cols.length() - separator.length());
        return cols;
    }

    private String generateConditionsFromFeatures()
    {
        String conditions = "";
        String separator = " AND ";
        for (String key : variableMapper.getVariableValueHashMap().keySet())
        {
            if (variableMapper.getVariableValueHashMap().get(key).getValue() != null)
            {
                String cond = variableMapper.getVariableValue(key).getCondition(key);
                if (cond != null)
                    conditions += cond + separator;
            }
        }
        conditions = conditions.substring(0, conditions.length() - separator.length());
        return conditions;
    }


    public VariableMapper getVariableMapper()
    {
        return variableMapper;
    }

    public void setVariableMapper(VariableMapper variableMapper)
    {
        this.variableMapper = variableMapper;
    }


    public DBQuery getDbQuery()
    {
        return dbQuery;
    }

    public void setDbQuery(DBQuery dbQuery)
    {
        this.dbQuery = dbQuery;
    }

    public RuleBaseToTableConverter getRuleBaseToTableConverter()
    {
        return ruleBaseToTableConverter;
    }

    public void setRuleBaseToTableConverter(RuleBaseToTableConverter ruleBaseToTableConverter)
    {
        this.ruleBaseToTableConverter = ruleBaseToTableConverter;
    }
}
