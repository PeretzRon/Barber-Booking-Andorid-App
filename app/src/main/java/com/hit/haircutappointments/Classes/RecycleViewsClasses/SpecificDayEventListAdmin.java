package com.hit.haircutappointments.Classes.RecycleViewsClasses;

import android.graphics.Bitmap;

public class SpecificDayEventListAdmin {

    private String mStartEvent;
    private String mEndEvent;
    private String mEventId;
    private String mPhone;
    private String mName;
    private Bitmap mStatusImage;

    public SpecificDayEventListAdmin(String mStartEvent, String mEndEvent, Bitmap mStatusImage,
                                     String mEventId, String mName, String mPhone) {
        this.mStartEvent = mStartEvent;
        this.mEndEvent = mEndEvent;
        this.mStatusImage = mStatusImage;
        this.mEventId = mEventId;
        this.mName = mName;
        this.mPhone = mPhone;
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

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}
