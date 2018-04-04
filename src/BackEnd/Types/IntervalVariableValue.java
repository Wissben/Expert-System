package BackEnd.Types;

import BackEnd.Condition;

/**
 * Created by ressay on 04/04/18.
 */
public class IntervalVariableValue<T extends Comparable> extends VariableValue<IntervalUnion<T>>
{

    public IntervalVariableValue(IntervalUnion<T> value) {
        this.value = value;
    }

    public IntervalVariableValue<Integer> singleInt(int value)
    {
        return new IntervalVariableValue<>(IntervalUnion.singleInt(value));
    }

    public IntervalVariableValue<Double> singleDouble(double value)
    {
        return new IntervalVariableValue<>(IntervalUnion.singleDouble(value));
    }

    @Override
    public void affect(Condition condition, VariableValue<IntervalUnion<T>> variableValue) throws ConflictException {

    }

    @Override
    public boolean equals(Object object)
    {
        IntervalVariableValue<T> variableValue = (IntervalVariableValue<T>) object;
        return value.equals(variableValue.value);
    }

    @Override
    public boolean isLessThan(VariableValue<IntervalUnion<T>> o) {
        return value.isLessThan(o.value);
    }

    @Override
    public boolean isMoreThan(VariableValue<IntervalUnion<T>> o) {
        return value.isMoreThan(o.value);
    }


    @Override
    public int compareTo(VariableValue<IntervalUnion<T>> o) {
        return 0;
    }

}
