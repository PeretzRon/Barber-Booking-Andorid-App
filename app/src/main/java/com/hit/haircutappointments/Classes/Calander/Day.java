package com.hit.haircutappointments.Classes.Calander;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Day {
    public enum eDayLong
    {
        ShortDay(12), LongDay(19);

        private final int value;

        eDayLong(int iValue)
        {
            this.value = iValue;
        }

        public int getValue()
        {
            return value;
        }
    }

    private Date mDay;
    private int mDayDate;
    private ArrayList<Event> mEvents;
    private eDayLong mDayLong;
    private String mFullDate;

    public Day(Date iDay, eDayLong iDayLong, String iFullDate)
    {
        this.mDayLong = iDayLong;
        this.mEvents = new ArrayList<>();
        this.mDay = iDay;
        this.mFullDate = iFullDate;
        createEvents(iDayLong);
    }

    public String getmFullDate() {
        return mFullDate;
    }

    public void setmFullDate(String mFullDate) {
        this.mFullDate = mFullDate;
    }

    public Day(){}

    public Date getmDay() {
        return mDay;
    }

    public void setmDay(Date mDay) {
        this.mDay = mDay;
    }

    public int getmDayDate() {
        return mDayDate;
    }

    public void setmDayDate(int mDayDate) {
        this.mDayDate = mDayDate;
    }

    public void setmEvents(ArrayList<Event> mEvents) {
        this.mEvents = mEvents;
    }

    public void setmDayLong(eDayLong mDayLong) {
        this.mDayLong = mDayLong;
    }

    public ArrayList<Event> getmEvents()
    {
        return mEvents;
    }

    public eDayLong getDayLong(){return  mDayLong;}

    private void createEvents(eDayLong iDayType)
    {
        Calendar cal = Calendar.getInstance();
        if(iDayType == eDayLong.LongDay)
        {
            cal.set(Calendar.HOUR_OF_DAY, 10);
        }
        else
        {
            cal.set(Calendar.HOUR_OF_DAY, 8);
        }

        cal.set(Calendar.MINUTE, 0);
        for (int i = 0; i < iDayType.value; i++)
        {
            Calendar startTime = (Calendar) cal.clone();
            cal.add(Calendar.MINUTE, 30);
            mEvents.add(new Event(startTime, cal));

        }
    }
}
