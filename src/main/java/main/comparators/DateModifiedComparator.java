package main.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class DateModifiedComparator implements Comparator<Fajl> {
    boolean asc=true;

    public DateModifiedComparator(boolean asc){
        this.asc=asc;
    }

    @Override
    public int compare(Fajl o1, Fajl o2) {
        if(asc)
            return o1.getModificationDate().compareTo(o2.getModificationDate());
        else
            return (-1)*( o1.getModificationDate().compareTo(o2.getModificationDate()));

    }
}
