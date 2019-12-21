package com.hit.haircutappointments.Classes.Calander;

import java.util.Calendar;

public class Event {
    public enum eEventStatus {
        Available,
        UnAvailable,
        CancelByManager,
        OrderedByPhone,
    }

    public enum eEventType {
        ManHairCut,
        WomanHairCut,
        KidsHairCut
    }

    private eEventStatus mEventStatus;
    private eEventType mEventType;
    private Customer mMustomer;
    private String mUserId;
    private EventTime mEventTime;
    private String mNameOrderedByPhone;
    private String mPhoneOrderedByPhone;


    public Event(Calendar iStartEvent, Calendar iEndEvent) {
        this.mEventTime = new EventTime(iStartEvent, iEndEvent);
        this.mEventStatus = eEventStatus.Available;
        this.mUserId = "";
        this.mNameOrderedByPhone = "";
        this.mPhoneOrderedByPhone = "";
    }

    public eEventStatus getmEventStatus() {
        return mEventStatus;
    }

    public void setmEventStatus(eEventStatus mEventStatus) {
        this.mEventStatus = mEventStatus;
    }

    public eEventType getmEventType() {
        return mEventType;
    }

    public void setmEventType(eEventType mEventType) {
        this.mEventType = mEventType;
    }

    public Event() {
    }

    public String getmNameOrderedByPhone() {
        return mNameOrderedByPhone;
    }

    public void setmNameOrderedByPhone(String mNameOrderedByPhone) {
        this.mNameOrderedByPhone = mNameOrderedByPhone;
    }

    public String getmPhoneOrderedByPhone() {
        return mPhoneOrderedByPhone;
    }

    public void setmPhoneOrderedByPhone(String mPhoneOrderedByPhone) {
        this.mPhoneOrderedByPhone = mPhoneOrderedByPhone;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public EventTime getmEventTime() {
        return mEventTime;
    }

    public Customer getmMustomer() {
        return mMustomer;
    }

    public void setmMustomer(String iId, String iName, String iEmail, String iPhoneNumber) {
        this.mMustomer = new Customer(iId, iName, iEmail, iPhoneNumber);
    }

    @Override
    public String toString() {
        return "Event{" +
                "mEventStatus=" + mEventStatus +
                ", mEventTime=" + mEventTime +
                '}';
    }
}
