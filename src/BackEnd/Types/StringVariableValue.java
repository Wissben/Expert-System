package BackEnd.Types;

import BackEnd.Condition;

/**
 * Created by ressay on 04/04/18.
 */
public class StringVariableValue extends VariableValue<String> implements VariableValueCondition
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
    public boolean isLessThan(VariableValue<String> o) {
        return value.compareTo(o.value) < 0;
    }

    @Override
    public boolean isMoreThan(VariableValue<String> o) {
        return value.compareTo(o.value) > 0;
    }

    @Override
    public VariableValue<String> defaultConstructor() {
        return new StringVariableValue("");
    }


    @Override
    public String getCondition(VariableValue variableValue) {
        return "="+this.value;
    }
}
