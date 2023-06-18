package com.example.sleepdiary.smartwatch;

public class SleepStage {
    String startDate;
    String endDate;
    String stage;

    public SleepStage(String startDate, String endDate, String stage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.stage = stage;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }
}
