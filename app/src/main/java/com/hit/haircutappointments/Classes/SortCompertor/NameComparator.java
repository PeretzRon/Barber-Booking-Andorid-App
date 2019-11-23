package com.hit.haircutappointments.Classes.SortCompertor;

import com.hit.haircutappointments.Classes.RecycleViewsClasses.RegisteredUsersList;

import java.util.Comparator;

public class NameComparator implements Comparator<RegisteredUsersList> {
    @Override
    public int compare(RegisteredUsersList o1, RegisteredUsersList o2) {
        // compare by name
        return o1.getmFullName().compareTo(o2.getmFullName());

    }
}
