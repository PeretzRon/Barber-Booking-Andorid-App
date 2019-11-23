package com.hit.haircutappointments.Classes.RecycleViewsClasses;

import android.graphics.Bitmap;

public class RegisteredUsersList {

    private String mFullName;
    private String mPhoneNumber;
    private String mEmail;
    private Bitmap mGenderImage;

    public RegisteredUsersList(String mFullName, String mPhoneNumber, String mEmail, Bitmap mGenderImage) {
        this.mFullName = mFullName;
        this.mPhoneNumber = mPhoneNumber;
        this.mGenderImage = mGenderImage;
        this.mEmail = mEmail;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public Bitmap getmGenderImage() {
        return mGenderImage;
    }

    public void setmGenderImage(Bitmap mGenderImage) {
        this.mGenderImage = mGenderImage;
    }
}
