package BackEnd.Types;

/**
 * Created by ressay on 04/04/18.
 */
public class DoubleValue extends IntervalVariableValue<Double>
{

    public DoubleValue(IntervalUnion<Double> value) {
        super(value);
    }

    public DoubleValue(double value)
    {
        super(IntervalUnion.singleDouble(value));
    }
}
