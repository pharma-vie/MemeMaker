package com.dinosilvestro.mememaker;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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

    private static List<SavedMemeParcel> mMemes;
    private String mMemeImageUrl;

    private SavedMemeParcel(Parcel in) {
        mMemeImageUrl = in.readString();
    }

    // Default empty constructor
    public SavedMemeParcel() {
    }

    public static List<SavedMemeParcel> getMemes() {
        return mMemes;
    }

    public void setMemes(List<SavedMemeParcel> mMemes) {
        SavedMemeParcel.mMemes = mMemes;
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