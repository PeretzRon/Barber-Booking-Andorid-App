package com.hit.haircutappointments.Classes.Users;

public class UserDetails {

    public enum eGender {
        Select("Select Gender"),
        Male("Male"),
        Female("Female");

        private String sex;

        eGender(String iGender) {
            this.sex = iGender;
        }

        @Override
        public String toString() {
            return sex;
        }
    }

    private String mFirstName;
    private String mLastName;
    private String mPhoneNumber;
    private String mEmail;
    private eGender mGender;
    private String mUserIdFromFireBase;

    public UserDetails(String mFirstName, String mLastName, String phoneNumber, eGender mGender,
                       String mEmail) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPhoneNumber = phoneNumber;
        this.mGender = mGender;
        this.mEmail = mEmail;
    }

    public UserDetails(String mFirstName, String mLastName, String mPhoneNumber,
                       eGender mGender, String mEmail, String mUserIdFromFireBase) {
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmail = mEmail;
        this.mGender = mGender;
        this.mUserIdFromFireBase = mUserIdFromFireBase;
    }

    public String getmUserIdFromFireBase() {
        return mUserIdFromFireBase;
    }

    public void setmUserIdFromFireBase(String mUserIdFromFireBase) {
        this.mUserIdFromFireBase = mUserIdFromFireBase;
    }

    public UserDetails() {
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public eGender getmGender() {
        return mGender;
    }

    public void setmGender(eGender mGender) {
        this.mGender = mGender;
    }
}
