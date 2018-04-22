package BackEnd.Database;

import BackEnd.Types.DoubleValue;
import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;
import BackEnd.Types.TypeGetter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryToProductsConverter
{
    private QueryAnswer queryAnswer;
    private FeaturesToQuerryConverter featuresToQuerryConverter;
    private HashMap<Integer, TypeGetter> mapTypeToGetType;

    public QueryToProductsConverter(FeaturesToQuerryConverter featuresToQuerryConverter)
    {
        this.featuresToQuerryConverter = featuresToQuerryConverter;
        this.queryAnswer = new QueryAnswer();
    }

    public Product[] queryDB()
    {
        featuresToQuerryConverter.generateQuery();
        DBQuery dbQuery = featuresToQuerryConverter.getDbQuery();
        ResultSet resultSet = dbQuery.executeQuery();
        ArrayList<Product> products = new ArrayList<>();
        try
        {
            while (resultSet.next())
            {
                this.initMapTypeToGetType(resultSet);
                HashMap<String, String> colMap = featuresToQuerryConverter.getRuleBaseToTableConverter().getMapColNameToType();
                Product product = new Product();

                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
                {

                    int colType = resultSet.getMetaData().getColumnType(i);
                    String colName = resultSet.getMetaData().getColumnName(i);
                    product.getMapColToValue().put(colName,
                            mapTypeToGetType.get(colType).getValue(resultSet.getMetaData().getColumnName(i), resultSet));
                }
                /**Debugging purpose
                 for (String key : product.getMapColToValue().keySet())
                 {
                 System.out.println("HERE : " + key + "=>" + product.getMapColToValue().get(key));
                 }
                 **/
                products.add(product);
            }

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return products.toArray(new Product[products.size()]);
    }


    private void initMapTypeToGetType(ResultSet resultSet)
    {
        this.mapTypeToGetType = new HashMap<>();
        mapTypeToGetType.put(Types.VARCHAR, (column, resultSet1) ->
        {
            try
            {
                return new StringVariableValue(resultSet1.getString(column));
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        });

        mapTypeToGetType.put(Types.INTEGER, (column, resultSet1) ->
        {

            try
            {
                return new IntegerValue(resultSet.getInt(column));
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        });


        mapTypeToGetType.put(Types.REAL, (column, resultSet1) ->
        {
            try
            {
                return new DoubleValue(resultSet.getFloat(column));
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            return null;
        });

    }


}
