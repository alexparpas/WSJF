package com.example.alexparpas.wsjf.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 06/08/2016.
 */
public class Job {

    private UUID id;
    private int userValue, timeValue, rroeValue, jobSize;
    private double wsjfScore;
    private String jobName, jobDescription;
    private Date date;
    private boolean completed;

    public Job() {
        this(UUID.randomUUID());
    }

    public Job(UUID id){
        this.id = id;
        date = new Date();
        jobName = "";
        jobDescription = "";
    }

    public void setUserValue(int userValue) {
        this.userValue = userValue;
    }

    public void setTimeValue(int timeValue) {
        this.timeValue = timeValue;
    }

    public void setRroeValue(int rroeValue) {
        this.rroeValue = rroeValue;
    }

    public void setJobSize(int jobsize) {
        this.jobSize = jobsize;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setWsjfScore(double wsjfScore) {
        this.wsjfScore = wsjfScore;
    }

    public int getUserValue() {
        return userValue;
    }

    public int getTimeValue() {
        return timeValue;
    }

    public int getRroeValue() {
        return rroeValue;
    }

    public int getJobSize() {
        return jobSize;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Date getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public double getWsjfScore() {
        return wsjfScore;
    }

    public void calculateWSJF() {
        wsjfScore = (userValue + timeValue + rroeValue) / jobSize;
    }
}
