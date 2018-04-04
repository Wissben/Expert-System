package BackEnd.Types;

import java.util.LinkedList;

/**
 * Created by ressay on 04/04/18.
 */
public class Interval<T extends Comparable>
{
    T sup,inf; // null for infinite value
    boolean includeSup,includeInf;

    public Interval(T sup, T inf, boolean includeSup, boolean includeInf) {
        this.sup = sup;
        this.inf = inf;
        this.includeSup = includeSup;
        this.includeInf = includeInf;
    }

    public Interval(T value)
    {
        this.sup = value;
        this.inf = value;
        this.includeSup = true;
        this.includeInf = true;
    }

    public Interval() // full interval
    {
        this.sup = null;
        this.inf = null;
        this.includeSup = false;
        this.includeInf = false;
    }

    public Interval<T> supInterval(T value,boolean included)
    {
        return new Interval<>(value,null,included,false);
    }

    public Interval<T> infInterval(T value,boolean included)
    {
        return new Interval<>(null,value,false,included);
    }

    public Interval<T> intersects(Interval<T> interval)
    {
        T newSup,newInf;
        boolean incSup,incInf;
        if(compareSupInf(sup,interval.inf) < 0 || compareSupInf(interval.sup,inf) < 0
           || (compareSupInf(sup,interval.inf) == 0 && !(includeSup && interval.includeInf)) // case of [y,x] and ]x,z]
           || (compareSupInf(interval.sup,inf) == 0 && !(includeInf && interval.includeSup)))
            return null; // intervals are disjoint
        // there is intersection
        // first we look for new interval's sup
        if(compareSupSup(sup,interval.sup) == 0)
        {
            newSup = sup;
            incSup = includeSup && interval.includeSup;
        }
        else
        {
            newSup = (compareSupSup(sup,interval.sup) < 0)?sup:interval.sup; // select min of sups
            incSup = (compareSupSup(sup,interval.sup) < 0)?includeSup:interval.includeSup; // select inclusion accordingly
        }
        // second we look for new interval's inf
        if(compareInfInf(inf,interval.inf) == 0)
        {
            newInf = inf;
            incInf = includeInf && interval.includeInf;
        }
        else
        {
            newInf = (compareInfInf(inf,interval.inf) > 0)?inf:interval.inf; // select max of infs
            incInf = (compareInfInf(inf,interval.inf) > 0)?includeInf:interval.includeInf; // select inclusion accordingly
        }
        return new Interval<>(newSup,newInf,incSup,incInf);
    }

    public IntervalUnion<T> union(Interval<T> interval)
    {
        if(intersects(interval) != null)
        {
            T newSup = (compareSupSup(sup,interval.sup) > 0)?sup:interval.sup; // select max of sups
            boolean incSup = (compareSupSup(sup,interval.sup) > 0)?includeSup:interval.includeSup; // select inclusion accordingly
            T newInf = (compareInfInf(inf,interval.inf) < 0)?inf:interval.inf; // select min of infs
            boolean incInf = (compareInfInf(inf,interval.inf) < 0)?includeInf:interval.includeInf; // select inclusion accordingly
            if(compareSupSup(sup,interval.sup) == 0)
                incSup = includeSup && interval.includeSup;
            if(compareInfInf(inf,interval.inf) == 0)
                incInf = includeInf && interval.includeInf;
            return new IntervalUnion<>(new Interval<>(newSup,newInf,incSup,incInf));
        }
        return new IntervalUnion<>(this,interval);
    }

    public IntervalUnion<T> remove(Interval<T> interval)
    {
        if(intersects(interval) == null)
            return new IntervalUnion<>(this);
        if(compareSupSup(interval) <= 0 && compareInfInf(interval) >= 0)
            return new IntervalUnion<>(new LinkedList<>()); // empty
        if(compareSupSup(interval) > 0 && compareInfInf(interval) < 0) {
            Interval<T> i1 = new Interval<>(sup,interval.sup,includeSup,!interval.includeSup);
            Interval<T> i2 = new Interval<>(interval.inf,inf,!interval.includeInf,includeInf);
            return new IntervalUnion<>(i1,i2);
        }
        if(compareSupSup(interval) <= 0)
        {
            Interval<T> i1 = new Interval<>(interval.inf,inf,!interval.includeInf,includeInf);
            return new IntervalUnion<>(i1);
        }
        else
        {
            Interval<T> i1 = new Interval<>(sup,interval.sup,includeSup,!interval.includeSup);
            return new IntervalUnion<>(i1);
        }
    }

    @Override
    public String toString()
    {

        if(sup != null && inf != null && sup.equals(inf) && includeSup && includeInf)
            return sup+"";
        String s = (sup != null)?sup+"":"+infinity";
        String i = (inf != null)?inf+"":"-infinity";
        String left = (includeInf)?"[":"]";
        String right = (includeSup)?"]":"[";
        return left+" "+i+","+s+" "+right;
    }

    @Override
    public boolean equals(Object o)
    {
        Interval<T> interval = (Interval<T>) o;
        return inf.equals(interval.inf) && sup.equals(interval.sup)
                && includeInf == interval.includeInf && includeSup == interval.includeSup;
    }

    public boolean isLessThan(Interval<T> interval)
    {
        return compareInfInf(inf,interval.inf) < 0 || (compareInfInf(inf,interval.inf) == 0 && includeInf && !interval.includeInf);
    }

    public boolean isMoreThan(Interval<T> interval)
    {
        return compareSupSup(sup,interval.sup) > 0 || (compareSupSup(sup,interval.sup) == 0 && includeSup && !interval.includeSup);
    }

    int compareSupSup(T t1,T t2)
    {
        if(t1 == null && t2 == null)
            return 0;
        if(t1 == null)
            return 1;
        if(t2 == null)
            return -1;
        return t1.compareTo(t2);
    }

    int compareSupInf(T t1, T t2)
    {
        if(t1 == null)
            return 1;
        if(t2 == null)
            return 1;
        return t1.compareTo(t2);
    }

    int compareInfInf(T t1,T t2)
    {
        if(t1 == null && t2 == null)
            return 0;
        if(t1 == null)
            return -1;
        if(t2 == null)
            return 1;
        return t1.compareTo(t2);
    }

    int compareInfSup(T t1,T t2)
    {
        if(t1 == null)
            return -1;
        if(t2 == null)
            return -1;
        return t1.compareTo(t2);
    }

    int compareSupSup(Interval<T> interval)
    {
        int val = compareSupSup(sup,interval.sup);
        int elVal = (includeSup && !interval.includeSup)?1:(!includeSup && interval.includeSup)?-1:0;
        return (val != 0)?val:elVal;
    }

    int compareSupInf(Interval<T> interval)
    {
        int val = compareSupInf(sup,interval.inf);
        int elVal = (includeSup && !interval.includeInf)?1:(!includeSup && interval.includeInf)?-1:0;
        return (val != 0)?val:elVal;
    }

    int compareInfInf(Interval<T> interval)
    {
        int val = compareInfInf(inf,interval.inf);
        int elVal = (includeInf && !interval.includeInf)?1:(!includeInf && interval.includeInf)?-1:0;
        return (val != 0)?val:elVal;
    }

    int compareInfSup(Interval<T> interval)
    {
        int val = compareInfSup(inf,interval.sup);
        int elVal = (includeInf && !interval.includeSup)?1:(!includeInf && interval.includeSup)?-1:0;
        return (val != 0)?val:elVal;
    }

}
