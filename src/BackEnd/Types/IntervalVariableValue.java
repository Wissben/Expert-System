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

    public IntervalVariableValue(Interval<T>... intervals) {
        this.value = new IntervalUnion<>(intervals);
    }

    public IntervalVariableValue() // full interval union
    {
        this.value = new IntervalUnion<>();
    }

    public IntervalVariableValue(T sup, T inf, boolean includeSup, boolean includeInf)// one interval
    {
        this.value = new IntervalUnion<>(sup,inf,includeSup,includeInf);
    }

    public IntervalVariableValue(T value) // one value interval
    {
        this.value = new IntervalUnion<>(value);
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
        switch (condition.getIndex())
        {
            case 1: // case =
                if(value == null) {
                    value = variableValue.value;
                    break;
                }
                value = value.intersects(variableValue.value);
                if(value.isEmpty()) throw new ConflictException();
                break;
            case 2: // case >
                if(value == null) {
                    value = value.supInterval(variableValue.value.getSup(),false);
                    break;
                }
                value = value.intersects(value.supInterval(variableValue.value.getSup(),false));
                if(value.isEmpty()) throw new ConflictException();
                break;
            case 3: // case <
                if(value == null) {
                    value = value.infInterval(variableValue.value.getInf(),false);
                    break;
                }
                value = value.intersects(value.infInterval(variableValue.value.getInf(),false));
                if(value.isEmpty()) throw new ConflictException();
                break;
            case 4: // case !=
                if(value == null) {
                    value = new IntervalUnion<T>(new Interval<>()).remove(variableValue.value);
                    break;
                }
                value = value.remove(variableValue.value);
                if(value.isEmpty()) throw new ConflictException();
                break;
        }
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
