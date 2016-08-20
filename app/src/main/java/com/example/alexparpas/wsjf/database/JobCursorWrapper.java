package com.example.alexparpas.wsjf.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.alexparpas.wsjf.database.JobDbSchema.JobTable;
import com.example.alexparpas.wsjf.model.Job;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 20/08/2016.
 */
public class JobCursorWrapper extends CursorWrapper{
    public JobCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Job getJob(){
        String uuidString = getString(getColumnIndex(JobTable.Cols.UUID));
        String jobName = getString(getColumnIndex(JobTable.Cols.JOB_NAME));
        String jobDescription = getString(getColumnIndex(JobTable.Cols.DESCRIPTION));
        int userValue = getInt(getColumnIndex(JobTable.Cols.USER_VALUE));
        int timeValue = getInt(getColumnIndex(JobTable.Cols.TIME_VALUE));
        int rroeValue = getInt(getColumnIndex(JobTable.Cols.RROE_VALUE));
        int jobSize = getInt(getColumnIndex(JobTable.Cols.JOB_SIZE));
        double wsjf = getDouble(getColumnIndex(JobTable.Cols.WSJF_VALUE));
        long date = getLong(getColumnIndex(JobTable.Cols.DATE));
        int isCompleted = getInt(getColumnIndex(JobTable.Cols.COMPLETED));

        Job job = new Job(UUID.fromString(uuidString));
        job.setJobName(jobName);
        job.setJobDescription(jobDescription);
        job.setUserValue(userValue);
        job.setTimeValue(timeValue);
        job.setRroeValue(rroeValue);
        job.setJobSize(jobSize);
        job.setWsjfScore(wsjf);
        job.setDate(new Date(date));
        job.setCompleted(isCompleted != 0);

        return job;
    }
}
