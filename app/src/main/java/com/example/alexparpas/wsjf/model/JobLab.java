package com.example.alexparpas.wsjf.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alexparpas.wsjf.database.JobBaseHelper;
import com.example.alexparpas.wsjf.database.JobCursorWrapper;
import com.example.alexparpas.wsjf.database.JobDbSchema.JobTable;
import com.example.alexparpas.wsjf.fragments.TasksFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobLab {
    private static JobLab sJobLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private JobLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new JobBaseHelper(mContext).getWritableDatabase();
    }

    public static JobLab get(Context context) {
        if (sJobLab == null) {
            sJobLab = new JobLab(context);
        }
        return sJobLab;
    }

    public void addJob(Job j) {
        ContentValues values = getContentValues(j);
        mDatabase.insert(JobTable.NAME, null, values);
    }

    public List<Job> getJobs(int sortType) {
        List<Job> jobs = new ArrayList<>();
        JobCursorWrapper cursor;
        switch (sortType) {
            case TasksFragment.NOT_SORTED:
                cursor = queryJobs(null, null, null);
                break;
            case TasksFragment.SORT_ALPHABETICALLY:
                cursor = queryJobs(null, null, JobTable.Cols.JOB_NAME + " ASC");
                break;
            case TasksFragment.SORT_DATE:
                cursor = queryJobs(null, null, JobTable.Cols.DATE + " ASC");
                break;
            case TasksFragment.SORT_WSJF:
                cursor = queryJobs(null, null, JobTable.Cols.WSJF_VALUE + " DESC");
                break;
            default:
                cursor = queryJobs(null, null, null);
        }
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                jobs.add(cursor.getJob());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return jobs;
    }

    public Job getJob(UUID id) {
        JobCursorWrapper cursor = queryJobs(JobTable.Cols.UUID + " = ?", new String[]{id.toString()}, null);

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getJob();
        } finally {
            cursor.close();
        }
    }

    public void updateJob(Job job) {
        String uuidString = job.getId().toString();
        ContentValues values = getContentValues(job);

        mDatabase.update(JobTable.NAME, values, JobTable.Cols.UUID + " =?", new String[]{uuidString});
    }

    public void deleteJob(Job job) {
        String uuidString = job.getId().toString();
        mDatabase.delete(JobTable.NAME, JobTable.Cols.UUID + " =?", new String[]{uuidString});
    }

    private JobCursorWrapper queryJobs(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                JobTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );
        return new JobCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Job job) {
        ContentValues values = new ContentValues();
        values.put(JobTable.Cols.UUID, job.getId().toString());
        values.put(JobTable.Cols.JOB_NAME, job.getJobName());
        values.put(JobTable.Cols.DESCRIPTION, job.getJobDescription());
        values.put(JobTable.Cols.USER_VALUE, job.getUserValue());
        values.put(JobTable.Cols.TIME_VALUE, job.getTimeValue());
        values.put(JobTable.Cols.RROE_VALUE, job.getRroeValue());
        values.put(JobTable.Cols.JOB_SIZE, job.getJobSize());
        values.put(JobTable.Cols.WSJF_VALUE, job.getWsjfScore());
        values.put(JobTable.Cols.DATE, job.getDate().getTime());
        values.put(JobTable.Cols.TIME, job.getDateTime().getTime());
        values.put(JobTable.Cols.COMPLETED, job.isCompleted() ? 1 : 0);

        return values;
    }
}
