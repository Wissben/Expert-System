package BackEnd.Types;

/**
 * Created by ressay on 04/04/18.
 */
public class Interval<T extends Comparable>
{
    T sup,inf;
    boolean includeSup,includeInf;

    public Interval(T sup, T inf, boolean includeSup, boolean includeInf) {
        this.sup = sup;
        this.inf = inf;
        this.includeSup = includeSup;
        this.includeInf = includeInf;
    }

    public Interval<T> intersects(Interval<T> interval)
    {
        T newSup,newInf;
        boolean incSup,incInf;
        if(sup.compareTo(interval.inf) < 0 || interval.sup.compareTo(inf) < 0
           || (sup.compareTo(interval.inf) == 0 && !(includeSup && interval.includeInf)) // case of [y,x] and ]x,z]
           || (interval.sup.compareTo(inf) == 0 && !(includeInf && interval.includeSup)))
            return null; // intervals are disjoint
        // there is intersection
        // first we look for new interval's sup
        if(sup.compareTo(interval.sup) == 0)
        {
            newSup = sup;
            incSup = includeSup && interval.includeSup;
        }
        else
        {
            newSup = (sup.compareTo(interval.sup) < 0)?sup:interval.sup; // select min of sups
            incSup = (sup.compareTo(interval.sup) < 0)?includeSup:interval.includeSup; // select inclusion accordingly
        }
        // second we look for new interval's inf
        if(inf.compareTo(interval.inf) == 0)
        {
            newInf = inf;
            incInf = includeInf && interval.includeInf;
        }
        else
        {
            newInf = (inf.compareTo(interval.inf) > 0)?inf:interval.inf; // select max of infs
            incInf = (inf.compareTo(interval.inf) > 0)?includeInf:interval.includeInf; // select inclusion accordingly
        }
        return new Interval<>(newSup,newInf,incSup,incInf);
    }

    public IntervalUnion<T> union(Interval<T> interval)
    {
        if(intersects(interval) != null)
        {
            T newSup = (sup.compareTo(interval.sup) > 0)?sup:interval.sup; // select max of sups
            boolean incSup = (sup.compareTo(interval.sup) > 0)?includeSup:interval.includeSup; // select inclusion accordingly
            T newInf = (inf.compareTo(interval.inf) < 0)?inf:interval.inf; // select min of infs
            boolean incInf = (inf.compareTo(interval.inf) < 0)?includeInf:interval.includeInf; // select inclusion accordingly
            if(sup.compareTo(interval.sup) == 0)
                incSup = includeSup && interval.includeSup;
            if(inf.compareTo(interval.inf) == 0)
                incInf = includeInf && interval.includeInf;
            return new IntervalUnion<>(new Interval<>(newSup,newInf,incSup,incInf));
        }
        return new IntervalUnion<>(this,interval);
    }

    @Override
    public String toString()
    {
        String left = (includeInf)?"[":"]";
        String right = (includeSup)?"]":"[";
        return left+" "+inf+","+sup+" "+right;
    }

    @Override
    public boolean equals(Object o)
    {
        Interval<T> interval = (Interval<T>) o;
        return inf.equals(interval.inf) && sup.equals(interval.sup)
                && includeInf == interval.includeInf && includeSup == interval.includeSup;
    }

}
