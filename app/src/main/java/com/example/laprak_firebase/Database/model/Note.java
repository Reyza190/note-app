package com.example.laprak_firebase.Database.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    private String key;
    private String title;
    private String content;

    public Note(){}

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.title);
        dest.writeString(this.content);
    }

    public void readFromParcel(Parcel source) {
        this.key = source.readString();
        this.title = source.readString();
        this.content = source.readString();
    }

    protected Note(Parcel in) {
        this.key = in.readString();
        this.title = in.readString();
        this.content = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
