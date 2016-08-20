package com.example.alexparpas.wsjf.database;

/**
 * Created by Alex on 20/08/2016.
 */
public class JobDbSchema {

    public static final class JobTable {
        public static final String NAME = "jobs";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String JOB_NAME = "jobname";
            public static final String DESCRIPTION = "description";
            public static final String USER_VALUE = "uservalue";
            public static final String TIME_VALUE = "timevalue";
            public static final String RROE_VALUE = "rroevalue";
            public static final String JOB_SIZE = "jobsize";
            public static final String WSJF_VALUE = "wsjfvalue";
            public static final String DATE = "date";
            public static final String COMPLETED = "completed";

        }
    }
}
