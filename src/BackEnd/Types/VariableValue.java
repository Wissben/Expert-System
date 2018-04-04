package BackEnd.Types;

import BackEnd.Condition;

/**
 * Created by ressay on 03/04/18.
 */
public abstract class VariableValue<T> implements Comparable<VariableValue<T>>
{
    protected T value;
    public abstract void affect(Condition condition, VariableValue<T> variableValue) throws ConflictException;
    @Override
    public abstract boolean equals(Object object);
    @Override
    public abstract int compareTo(VariableValue<T> o);
}
