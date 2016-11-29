package com.dinosilvestro.mememaker;


import android.os.Parcel;
import android.os.Parcelable;

public class MemeParcel implements Parcelable {

    public static final Creator<MemeParcel> CREATOR = new Creator<MemeParcel>() {
        @Override
        public MemeParcel createFromParcel(Parcel in) {
            return new MemeParcel(in);
        }

        @Override
        public MemeParcel[] newArray(int size) {
            return new MemeParcel[size];
        }
    };

    private static MemeParcel[] mMemes;
    private String mMemeImageUrl;

    private MemeParcel(Parcel in) {
        mMemeImageUrl = in.readString();
    }

    // Default empty constructor
    public MemeParcel() {
    }

    public MemeParcel[] getmMemes() {
        return mMemes;
    }

    public void setMemes(MemeParcel[] mMemes) {
        MemeParcel.mMemes = mMemes;
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
