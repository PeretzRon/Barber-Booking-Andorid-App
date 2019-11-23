package com.hit.haircutappointments.Classes.SortCompertor;

import com.hit.haircutappointments.Classes.RecycleViewsClasses.PriceList;

import java.util.Comparator;

public class PriceComparator implements Comparator<PriceList> {

    @Override
    public int compare(PriceList o1, PriceList o2) {
        return o1.CompareByPrice(o2);
    }

}
