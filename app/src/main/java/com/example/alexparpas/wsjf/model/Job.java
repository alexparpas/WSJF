package com.example.alexparpas.wsjf.model;

import java.util.Date;

/**
 * Created by Alex on 06/08/2016.
 */
public class Job {

    private int userValue, timeValue, rroeValue, jobSize;
    private String jobName, jobDescription;
    private Date startDate, endDate;
    private boolean completed;

    public Job() {
        startDate = new Date();
        endDate = new Date();
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int calculateWSJF(int userValue, int timeValue, int rroeValue, int jobsize) {
        int score;
        score = (userValue + timeValue + rroeValue) / jobsize;
        return score;
    }
}
