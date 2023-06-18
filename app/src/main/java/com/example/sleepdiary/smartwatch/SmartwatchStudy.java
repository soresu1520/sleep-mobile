package com.example.sleepdiary.smartwatch;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class SmartwatchStudy {
    String id;
    String patientId;
    String patientEmail;
    ArrayList<SleepStage> sleepStages;
    float max;
    float min;
    float mean;
    float desaturationTime;
    Timestamp entryDate;

    public SmartwatchStudy(String id, String patientId, String patientEmail, ArrayList<SleepStage> sleepStages, float max, float min, float mean, float desaturationTime, Timestamp entryDate) {
        this.id = id;
        this.patientId = patientId;
        this.patientEmail = patientEmail;
        this.sleepStages = sleepStages;
        this.max = max;
        this.min = min;
        this.mean = mean;
        this.desaturationTime = desaturationTime;
        this.entryDate = entryDate;
    }

    public SmartwatchStudy() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public ArrayList<SleepStage> getSleepStages() {
        return sleepStages;
    }

    public void setSleepStages(ArrayList<SleepStage> sleepStages) {
        this.sleepStages = sleepStages;
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

    public Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Timestamp entryDate) {
        this.entryDate = entryDate;
    }
}
