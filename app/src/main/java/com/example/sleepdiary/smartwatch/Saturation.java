package com.example.sleepdiary.smartwatch;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Saturation {
    float max;
    float min;
    float mean;
    float desaturationTime;
    Timestamp entryDate;

    public Saturation(float max, float min, float mean, float desaturationTime, Timestamp entryDate) {
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.desaturationTime = desaturationTime;
        this.entryDate = entryDate;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMean() {
        return mean;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public float getDesaturationTime() {
        return desaturationTime;
    }

    public void setDesaturationTime(float desaturationTime) {
        this.desaturationTime = desaturationTime;
    }

    public void setDesaturationTime(int desaturationTime) {
        this.desaturationTime = desaturationTime;
    }

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }
}
