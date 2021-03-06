package BackEnd.Types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by ressay on 04/04/18.
 */
public class IntervalUnion<T extends Comparable> implements Serializable
{
    LinkedList<Interval<T>> intervals = new LinkedList<>();
    public IntervalUnion(Interval<T>... intervals)
    {
        this.intervals = new LinkedList<>();
        this.intervals.addAll(Arrays.asList(intervals));
        fixIntervals();
    }

    public static IntervalUnion<Integer> singleInt(int value)
    {
        return new IntervalUnion<Integer>(new Interval<>(value));
    }

    public static IntervalUnion<Double> singleDouble(double value)
    {
        return new IntervalUnion<Double>(new Interval<>(value));
    }

    public IntervalUnion<T> supInterval(T value,boolean included)
    {
        return new IntervalUnion<>(new Interval<>(null,value,false,included));
    }

    public IntervalUnion<T> infInterval(T value,boolean included)
    {
        return new IntervalUnion<>(new Interval<>(value,null,included,false));
    }

    public IntervalUnion(LinkedList<Interval<T>> intervals) {
        this.intervals = new LinkedList<>(intervals);
        fixIntervals();
    }

    public IntervalUnion() // full interval union
    {
        this.intervals.add(new Interval<>());
    }

    public IntervalUnion(T sup, T inf, boolean includeSup, boolean includeInf)// one interval
    {
        this.intervals.add(new Interval<>(sup,inf,includeSup,includeInf));
    }

    public IntervalUnion(T value) // one value interval
    {
        this.intervals.add(new Interval<>(value));
    }

    protected void fixIntervals()
    {
        for (int i = 0; i < intervals.size(); i++) {
            Interval<T> min = intervals.get(i);
            int k = i;
            for (int j = i; j < intervals.size(); j++) {
                if(min.compareInfInf(min.inf,intervals.get(j).inf) > 0)
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

        IntervalUnion<T> result = new IntervalUnion<>(new LinkedList<>()); // empty interval
        for(Interval<T> interval:intervalUnion.intervals) // intersection with all union's intervals
            result = result.union(intersects(interval)); // intervals are fixed
        return result;
    }

    public IntervalUnion<T> remove(Interval<T> interval)
    {
        LinkedList<Interval<T>> ints = new LinkedList<>(intervals);
        IntervalUnion<T> result = new IntervalUnion<>(new LinkedList<>());
        for (Interval<T> in : ints) {
            result = result.union(in.remove(interval));
        }
        return result;
    }

    public IntervalUnion<T> remove(IntervalUnion<T> intervalUnion)
    {
        LinkedList<Interval<T>> ints = new LinkedList<>(intervalUnion.intervals);
        IntervalUnion<T> result = new IntervalUnion<>(new LinkedList<>()); // empty
        for (Interval<T> in : ints)
            result = result.union(remove(in));
        return result;
    }





    @Override
    public boolean equals(Object o)
    {
        IntervalUnion<T> intervalUnion = (IntervalUnion<T>) o;
        if(intervals.size() != intervalUnion.intervals.size())
            return false;
        for (int i = 0; i < intervals.size(); i++) {
            if(!intervals.get(i).equals(intervalUnion.intervals.get(i)))
                return false;
        }
        return true;
    }

    public boolean isLessThan(IntervalUnion<T> intervalUnion)
    {
        Interval<T> interval = intervalUnion.intervals.get(0);
        Interval<T> i = intervals.get(0);
        return i.isLessThan(interval);
    }

    public boolean isMoreThan(IntervalUnion<T> intervalUnion)
    {
        Interval<T> interval = intervalUnion.intervals.getLast();
        Interval<T> i = intervals.getLast();
        return i.isMoreThan(interval);
    }

    @Override
    public String toString()
    {
        if(intervals.isEmpty())
            return "empty";
        String result = intervals.getFirst()+"";
        for (int i = 1; i < intervals.size(); i++) {
            result += " U " + intervals.get(i);
        }
        return result;
    }

    public T getSup()
    {
        return intervals.getLast().sup;
    }

    public T getInf()
    {
        return intervals.getFirst().inf;
    }

    public boolean isEmpty()
    {
        return intervals.isEmpty();
    }

}
