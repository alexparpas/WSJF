package com.example.alexparpas.wsjf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    private int userValue, timeValue, rroeValue, jobsize;
    private String jobName, jobDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    public int calculateWSJF(int userValue, int timeValue, int rroeValue, int jobsize) {
        int score;
        score = (userValue + timeValue + rroeValue) / jobsize;
        return score;
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

    public void setJobsize(int jobsize) {
        this.jobsize = jobsize;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
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

    public int getJobsize() {
        return jobsize;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

}
