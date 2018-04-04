package BackEnd.Types;

import BackEnd.Condition;

/**
 * Created by ressay on 04/04/18.
 */
public class StringVariableValue extends VariableValue<String>
{
    public StringVariableValue(String val) {
        this.value = val;
    }

    @Override
    public void affect(Condition condition, VariableValue<String> variableValue) throws ConflictException
    {
        this.value = variableValue.value;
    }

    @Override
    public boolean equals(Object object)
    {
        StringVariableValue val = (StringVariableValue) object;
        return value.equals(val.value);
    }

    // compared using lexicographical order
    @Override
    public int compareTo(VariableValue<String> variableValue) {
        return value.compareTo(variableValue.value);
    }

    @Override
    public String toString()
    {
        return value;
    }
}
