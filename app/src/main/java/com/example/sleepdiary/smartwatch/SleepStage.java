package com.example.sleepdiary.smartwatch;

import com.google.firebase.Timestamp;

public class SleepStage {
    Timestamp startDate;
    Timestamp endDate;
    String stage;
    int duration;

    public SleepStage(Timestamp startDate, Timestamp endDate, String stage, int duration) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.stage = stage;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
