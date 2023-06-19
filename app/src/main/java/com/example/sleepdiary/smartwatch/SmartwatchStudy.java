package com.example.sleepdiary.smartwatch;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class SmartwatchStudy {
    String id;
    String patientId;
    String patientEmail;
    ArrayList<SleepStage> sleepStages;
    float maxSaturation;
    float minSaturation;
    float meanSaturation;
    float desaturationTime;
    Timestamp entryDate;

    public SmartwatchStudy(String id, String patientId, String patientEmail, ArrayList<SleepStage> sleepStages, float maxSaturation, float minSaturation, float meanSaturation, float desaturationTime, Timestamp entryDate) {
        this.id = id;
        this.patientId = patientId;
        this.patientEmail = patientEmail;
        this.sleepStages = sleepStages;
        this.maxSaturation = maxSaturation;
        this.minSaturation = minSaturation;
        this.meanSaturation = meanSaturation;
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

    public float getMaxSaturation() {
        return maxSaturation;
    }

    public void setMaxSaturation(float maxSaturation) {
        this.maxSaturation = maxSaturation;
    }

    public float getMinSaturation() {
        return minSaturation;
    }

    public void setMinSaturation(float minSaturation) {
        this.minSaturation = minSaturation;
    }

    public float getMeanSaturation() {
        return meanSaturation;
    }

    public void setMeanSaturation(float meanSaturation) {
        this.meanSaturation = meanSaturation;
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
