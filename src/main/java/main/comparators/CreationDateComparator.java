package main.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class CreationDateComparator implements Comparator<Fajl> {
    boolean asc=true;

    public CreationDateComparator(boolean asc){
        this.asc=asc;
    }

    @Override
    public int compare(Fajl o1, Fajl o2) {
        if(asc)
               return o1.getCreationDate().compareTo(o2.getCreationDate());
        else
            return (-1)*(o1.getCreationDate().compareTo(o2.getCreationDate()));

    }
}
