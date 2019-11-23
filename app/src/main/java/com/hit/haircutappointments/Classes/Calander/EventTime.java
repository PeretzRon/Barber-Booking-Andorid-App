package com.hit.haircutappointments.Classes.Calander;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventTime
{

    private String mStartTimeString;
    private String mEndTimeString;

    public EventTime(Calendar mStartTime, Calendar mEndTime)
    {
        DateFormat df = new SimpleDateFormat("HH:mm");
        this.mStartTimeString = df.format(mStartTime.getTime());
        this.mEndTimeString = df.format(mEndTime.getTime());
    }

    public  EventTime(){}

    public String getmStartTimeString() {
        return mStartTimeString;
    }

    public void setmStartTimeString(String mStartTimeString) {
        this.mStartTimeString = mStartTimeString;
    }

    public String getmEndTimeString() {
        return mEndTimeString;
    }

    public void setmEndTimeString(String mEndTimeString) {
        this.mEndTimeString = mEndTimeString;
    }

    @Override
    public String toString()
    {
        return "EventTime{" +
                "mStartTimeString='" + mStartTimeString + '\'' +
                ", mEndTimeString='" + mEndTimeString + '\'' +
                '}';
    }
}
