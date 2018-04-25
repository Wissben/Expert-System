package BackEnd.Database;

import BackEnd.Types.VariableValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ressay on 07/04/18.
 */
public class Product implements Serializable
{
    private HashMap<String, VariableValue> mapColToValue;
    protected String[] display = {"Article","Size","Material","Gender","Price","Usages"};

    public static String imgDirectory = "clothesImages/";
    public Product()
    {
        this.mapColToValue = new HashMap<>();
    }

    public VariableValue getAttribute(String name)
    {
        return mapColToValue.get(name);
    }

    public String getPathToImage()
    {
        String path = getAttribute("imagePath").toString();
        return path.equals("null")?null:imgDirectory+path;
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

    @Override
    public String toString()
    {
        String result = "";
        for (String key : display)
        {
            result += key + ": " + mapColToValue.get(key) + "\n";
        }
        return result;
    }
}
