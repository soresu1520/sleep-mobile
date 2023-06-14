package com.example.sleepdiary;

public class PatientIdSingleton {
    String id;
    private static final PatientIdSingleton ourInstance = new PatientIdSingleton();

    public static PatientIdSingleton getInstance() {
        return ourInstance;
    }

    private PatientIdSingleton() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
