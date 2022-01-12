package com.app.workflowmanager.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private String name;
    private String status;
    private String conclusion;
    private int number;
    private String started_at;
    private String completed_at;

    protected Step(Parcel in) {
        name = in.readString();
        status = in.readString();
        conclusion = in.readString();
        number = in.readInt();
        started_at = in.readString();
        completed_at = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public int getNumber() {
        return number;
    }

    public String getStarted_at() {
        return started_at;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeString(conclusion);
        parcel.writeInt(number);
        parcel.writeString(started_at);
        parcel.writeString(completed_at);
    }
}
