package BackEnd.Database;

import BackEnd.Types.VariableValue;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ressay on 07/04/18.
 */
public class Product implements Serializable
{
    private HashMap<String, VariableValue> mapColToValue;

    public Product()
    {
        this.mapColToValue = new HashMap<>();
    }

    public void displayProduct()
    {
        for (String key : mapColToValue.keySet())
        {
            System.out.print(key + "=>" + mapColToValue.get(key) + ",");
        }
        System.out.println();
    }


    public HashMap<String, VariableValue> getMapColToValue()
    {
        return mapColToValue;
    }

    public void setMapColToValue(HashMap<String, VariableValue> mapColToValue)
    {
        this.mapColToValue = mapColToValue;
    }
}
