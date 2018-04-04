package BackEnd.Types;

/**
 * Created by ressay on 04/04/18.
 */
public class IntegerValue extends IntervalVariableValue<Integer>
{

    public IntegerValue(IntervalUnion<Integer> value) {
        super(value);
    }

    public IntegerValue(int value)
    {
       super(IntervalUnion.singleInt(value));
    }
}
