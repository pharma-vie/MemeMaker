package com.dinosilvestro.mememaker.parcels;


import android.os.Parcel;
import android.os.Parcelable;

public class SavedMemeParcel implements Parcelable {

    public static final Creator<SavedMemeParcel> CREATOR = new Creator<SavedMemeParcel>() {
        @Override
        public SavedMemeParcel createFromParcel(Parcel in) {
            return new SavedMemeParcel(in);
        }

        @Override
        public SavedMemeParcel[] newArray(int size) {
            return new SavedMemeParcel[size];
        }
    };

    private String mMemeImageUrl;

    private SavedMemeParcel(Parcel in) {
        mMemeImageUrl = in.readString();
    }

    // Default empty constructor
    public SavedMemeParcel() {
    }

    public String getMemeImageUrl() {
        return mMemeImageUrl;
    }

    public void setMemeImageUrl(String memeImageUrl) {
        mMemeImageUrl = memeImageUrl;
    }

    @Override
    public int describeContents() {
        return 0; // Not using this
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMemeImageUrl);
    }
}