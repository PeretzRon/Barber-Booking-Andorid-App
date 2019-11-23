package com.hit.haircutappointments.Classes.RecycleViewsClasses;

import android.graphics.Bitmap;

public class SpecificDayEventList {

    private String mStartEvent;
    private String mEndEvent;
    private String mEventId;
    private Bitmap mStatusImage;

    public SpecificDayEventList(String mStartEvent, String mEndEvent, Bitmap mStatusImage, String mEventId) {
        this.mStartEvent = mStartEvent;
        this.mEndEvent = mEndEvent;
        this.mStatusImage = mStatusImage;
        this.mEventId = mEventId;
    }

    public String getmEventId() {
        return mEventId;
    }

    public void setmEventId(String mEventId) {
        this.mEventId = mEventId;
    }

    public String getmEndEvent() {
        return mEndEvent;
    }

    public void setmEndEvent(String mEndEvent) {
        this.mEndEvent = mEndEvent;
    }

    public String getmStartEvent() {
        return mStartEvent;
    }

    public void setmStartEvent(String mStartEvent) {
        this.mStartEvent = mStartEvent;
    }

    public Bitmap getmStatusImage() {
        return mStatusImage;
    }

    public void setmStatusImage(Bitmap mStatusImage) {
        this.mStatusImage = mStatusImage;
    }
}
