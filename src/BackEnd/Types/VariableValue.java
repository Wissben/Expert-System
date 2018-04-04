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

    public abstract boolean isLessThan(VariableValue<T> o);

    public abstract boolean isMoreThan(VariableValue<T> o);
}
