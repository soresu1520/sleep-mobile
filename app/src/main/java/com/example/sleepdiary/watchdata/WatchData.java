package com.example.sleepdiary.watchdata;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class WatchData{

    private String id, patientEmail;
    private ArrayList<Float> accX, accY, accZ, heart = new ArrayList<>();
    private Timestamp endDate, startDate;

    public WatchData() {
    }

    public WatchData(String id, String patientEmail, ArrayList<Float> accX, ArrayList<Float> accY, ArrayList<Float> accZ, ArrayList<Float> heart, Timestamp endDate, Timestamp startDate) {
        this.id = id;
        this.patientEmail = patientEmail;
        this.accX = accX;
        this.accY = accY;
        this.accZ = accZ;
        this.heart = heart;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public ArrayList<Float> getAccX() {
        return accX;
    }

    public void setAccX(ArrayList<Float> accX) {
        this.accX = accX;
    }

    public ArrayList<Float> getAccY() {
        return accY;
    }

    public void setAccY(ArrayList<Float> accY) {
        this.accY = accY;
    }

    public ArrayList<Float> getAccZ() {
        return accZ;
    }

    public void setAccZ(ArrayList<Float> accZ) {
        this.accZ = accZ;
    }

    public ArrayList<Float> getHeart() {
        return heart;
    }

    public void setHeart(ArrayList<Float> heart) {
        this.heart = heart;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
}
