package BackEnd.Database;

import BackEnd.Types.IntegerValue;
import BackEnd.Types.StringVariableValue;
import BackEnd.Types.VariableValue;
import BackEnd.Variable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ressay on 07/04/18.
 */
public class Product implements Serializable {
    private ArrayList<VariableValue> features;
    private VariableValue name;
    private VariableValue id;

    public Product(String name, int id) {
        this.features = new ArrayList<>();
        this.name = new StringVariableValue(name);
        this.id = new IntegerValue(id);
    }

    public ArrayList<VariableValue> getFeatures() { return features; }

    public void setFeatures(ArrayList<VariableValue> features) { this.features = features; }

    public VariableValue getName() { return name; }

    public void setName(VariableValue name) { this.name = name; }

    public VariableValue getId() { return id; }

    public void setId(VariableValue id) { this.id = id; }
}
