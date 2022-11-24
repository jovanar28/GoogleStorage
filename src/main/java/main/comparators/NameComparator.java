package main.comparators;

import baluni.model.Fajl;

import java.util.Comparator;

public class NameComparator implements Comparator<Fajl> {
    boolean asc=true;

    public NameComparator(boolean asc){
        this.asc=asc;
    }

    @Override
    public int compare(Fajl o1, Fajl o2) {
        if(asc){
            return o1.getFileName().compareTo(o2.getFileName());
        }else
            return (-1)*(o1.getFileName().compareTo(o2.getFileName()));
    }
}
