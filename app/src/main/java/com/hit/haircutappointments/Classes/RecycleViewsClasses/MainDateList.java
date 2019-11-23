package com.hit.haircutappointments.Classes.RecycleViewsClasses;

import android.graphics.Bitmap;

import com.hit.haircutappointments.Classes.Calander.Day;

import java.io.Serializable;

public class MainDateList implements Serializable {

    private String mDate;
    private String mOnlyTheDay;
    private String mNameDayDate;
    private Day mDay;
    private String mHotMuchEventsAvailable;
    private Bitmap mImageView;

    public MainDateList(String mDate, String mOnlyTheDay, String mNameDayDate, Day mDay,
                        String mHotMuchEventsAvailable, Bitmap mImageView) {
        this.mDate = mDate;
        this.mNameDayDate = mNameDayDate;
        this.mDay = mDay;
        this.mOnlyTheDay = mOnlyTheDay;
        this.mHotMuchEventsAvailable = mHotMuchEventsAvailable;
        this.mImageView = mImageView;
    }

    public String getmOnlyTheDay() {
        return mOnlyTheDay;
    }

    public void setmOnlyTheDay(String mOnlyTheDay) {
        this.mOnlyTheDay = mOnlyTheDay;
    }

    public Day getmDay() {
        return mDay;
    }

    public void setmDay(Day mDay) {
        this.mDay = mDay;
    }

    public String getmNameDayDate() {
        return mNameDayDate;
    }

    public void setmNameDayDate(String mNameDayDate) {
        this.mNameDayDate = mNameDayDate;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmHotMuchEventsAvailable() {
        return mHotMuchEventsAvailable;
    }

    public void setmHotMuchEventsAvailable(String mHotMuchEventsAvailable) {
        this.mHotMuchEventsAvailable = mHotMuchEventsAvailable;
    }

    public Bitmap getmImageView() {
        return mImageView;
    }

    public void setmImageView(Bitmap mImageView) {
        this.mImageView = mImageView;
    }
}
