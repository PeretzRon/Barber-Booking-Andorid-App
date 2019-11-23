package com.hit.haircutappointments.Classes.RecycleViewsClasses;

import android.graphics.Bitmap;

public class ComingAndPassedEventsList {

    private String mFullDate;
    private String mTime;
    private Bitmap mImage;

    public ComingAndPassedEventsList(String mFullDate, String mTime, Bitmap mImage) {
        this.mFullDate = mFullDate;
        this.mTime = mTime;
        this.mImage = mImage;
    }

    public String getmFullDate() {
        return mFullDate;
    }

    public void setmFullDate(String mFullDate) {
        this.mFullDate = mFullDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public void setmImage(Bitmap mImage) {
        this.mImage = mImage;
    }
}
