package com.example.alexparpas.wsjf.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.alexparpas.wsjf.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Alex on 07/08/2016.
 */
public class JobLab {
    private static JobLab sJobLab;
    private List<Job> mJobs;
    private JobLab(Context context) {
        mJobs = new ArrayList<>();
    }

    public static JobLab get(Context context) {
        if (sJobLab == null) {
            sJobLab = new JobLab(context);
        }
        return sJobLab;
    }

    public void addJob(Job j){
        mJobs.add(j);
    }

    public List<Job> getJobs() {
        return mJobs;
    }

    public Job getJob(UUID id){
        for(Job job: mJobs){
            if(job.getId().equals(id)){
                return job;
            }
        }
        return null;
    }
}
