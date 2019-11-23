package com.hit.haircutappointments.Classes.Calander;

public class Customer
{

    private String mID;
    private String mName;
    private String mEmail;
    private String mPhoneNumber;

    public Customer(String mID, String mName, String mEmail, String mPhoneNumber)
    {
        this.mID = mID;
        this.mName = mName;
        this.mEmail = mEmail;
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmID()
    {
        return mID;
    }

    public void setmID(String mID)
    {
        this.mID = mID;
    }

    public String getmName()
    {
        return mName;
    }

    public void setmName(String mName)
    {
        this.mName = mName;
    }

    public String getmEmail()
    {
        return mEmail;
    }

    public void setmEmail(String mEmail)
    {
        this.mEmail = mEmail;
    }

    public String getmPhoneNumber()
    {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber)
    {
        this.mPhoneNumber = mPhoneNumber;
    }
}
