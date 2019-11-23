package com.hit.haircutappointments.Classes.RecycleViewsClasses;

public class PriceList {

    private String mAction;
    private String mCost;

    public PriceList(String mAction, String mCost) {
        this.mAction = mAction;
        this.mCost = mCost;
    }

    public String getmAction() {
        return mAction;
    }

    public void setmAction(String mAction) {
        this.mAction = mAction;
    }

    public String getmCost() {
        return mCost;
    }

    public void setmCost(String mCost) {
        this.mCost = mCost;
    }

    public int CompareByPrice(PriceList o2) {
        if (this.getmCost().length() > o2.getmCost().length()) {
            return 1;
        }

        if (this.getmCost().length() < o2.getmCost().length()) {
            return -1;
        }

        return this.getmCost().compareTo(o2.getmCost());
    }
}
