package com.example.alexparpas.wsjf.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.example.alexparpas.wsjf.database.JobDbSchema.JobTable;

/**
 * Created by Alex on 20/08/2016.
 */
public class JobBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "jobBase.db";

    public JobBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + JobTable.NAME + "(" + "_id integer primary key autoincrement, " +
                JobTable.Cols.UUID + ", " +
                JobTable.Cols.JOB_NAME + ", " +
                JobTable.Cols.DESCRIPTION + ", " +
                JobTable.Cols.USER_VALUE + ", " +
                JobTable.Cols.TIME_VALUE + ", " +
                JobTable.Cols.RROE_VALUE + ", " +
                JobTable.Cols.JOB_SIZE + ", " +
                JobTable.Cols.WSJF_VALUE + ", " +
                JobTable.Cols.DATE + ", " +
                JobTable.Cols.COMPLETED + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
