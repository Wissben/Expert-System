package BackEnd.Types;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by ressay on 04/04/18.
 */
public class IntervalUnion<T extends Comparable>
{
    LinkedList<Interval<T>> intervals;
    public IntervalUnion(Interval<T>... intervals)
    {
        this.intervals = new LinkedList<>();
        this.intervals.addAll(Arrays.asList(intervals));
        fixIntervals();
    }

    public static IntervalUnion<Integer> singleInt(int value)
    {
        return new IntervalUnion<>(new Interval<Integer>(value));
    }

    public static IntervalUnion<Double> singleDouble(double value)
    {
        return new IntervalUnion<>(new Interval<Double>(value));
    }

    public IntervalUnion(LinkedList<Interval<T>> intervals) {
        this.intervals = intervals;
        fixIntervals();
    }

    protected void fixIntervals()
    {
        for (int i = 0; i < intervals.size(); i++) {
            Interval<T> min = intervals.get(i);
            int k = i;
            for (int j = i; j < intervals.size(); j++) {
                if(min.inf.compareTo(intervals.get(j).inf) > 0)
                {
                    k = j;
                    min = intervals.get(j);
                }
            }
            // min contains interval with minimum inf
            // if interval in min intersects with interval in i-1 we merge intervals
            if(i>0 && intervals.get(i-1).intersects(min) != null)
            {
                min = min.union(intervals.get(i-1)).intervals.get(0);
                intervals.set(i-1,min);
                intervals.remove(k);
            }
            else
            {
                // else order intervals
                intervals.set(k,intervals.get(i));
                intervals.set(i,min);
            }
        }
    }

    public IntervalUnion<T> union(Interval<T> interval)
    {
        LinkedList<Interval<T>> ints = new LinkedList<>(intervals);
        ints.add(interval);
        return new IntervalUnion<>(ints);
    }

    public IntervalUnion<T> union(IntervalUnion<T> intervalUnion)
    {
        LinkedList<Interval<T>> ints = new LinkedList<>(intervals);
        ints.addAll(intervalUnion.intervals);
        return new IntervalUnion<>(ints);
    }

    public IntervalUnion<T> intersects(Interval<T> interval)
    {
        LinkedList<Interval<T>> list = new LinkedList<>();
        for (Interval<T> in: intervals) {
            Interval<T> result = in.intersects(interval);
            if(result!=null)
                list.add(result);
        }
        return new IntervalUnion<>(list);
    }



    public IntervalUnion<T> intersects(IntervalUnion<T> intervalUnion)
    {
        IntervalUnion<T> result = new IntervalUnion<>(); // empty interval
        for(Interval<T> interval:intervalUnion.intervals) // intersection with all union's intervals
            result = result.union(intersects(interval)); // intervals are fixed
        return result;
    }
}
