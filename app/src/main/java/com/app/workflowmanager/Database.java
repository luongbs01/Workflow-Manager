package com.app.workflowmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public static final String REPOSITORY = "REPOSITORY";
    public static final String WORKFLOW = "WORKFLOW";
    public static final String WORKFLOW_RUN = "WORKFLOW_RUN";
    public static final String WORKFLOW_JOB = "WORKFLOW_JOB";
    public static final String STEP = "STEP";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String AVATAR_URL = "avatar_url";
    public static final String LOGIN = "login";
    public static final String DESCRIPTION = "description";
    public static final String VISIBILITY = "visibility";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String PATH = "path";
    public static final String HTML_URL = "html_url";
    public static final String RUN_NUMBER = "run_number";
    public static final String EVENT = "event";
    public static final String STATUS = "status";
    public static final String CONCLUSION = "conclusion";
    public static final String STARTED_AT = "started_at";
    public static final String COMPLETED_AT = "completed_at";
    public static final String NUMBER = "number";

    public static final String REPOSITORY_ID = "repository_id";
    public static final String WORKFLOW_ID = "workflow_id";
    public static final String RUN_ID = "run_id";
    public static final String JOB_ID = "job_id";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableRepository = "CREATE TABLE " + REPOSITORY + " ( " +
                ID + " INT NOT NULL PRIMARY KEY, " +
                NAME + " VARCHAR(255) NOT NULL, " +
                AVATAR_URL + " VARCHAR(255) NOT NULL, " +
                LOGIN + " VARCHAR(255) NOT NULL, " +
                DESCRIPTION + " VARCHAR(255) NOT NULL, " +
                VISIBILITY + " VARCHAR(255) NOT NULL, " +
                CREATED_AT + " VARCHAR(255) NOT NULL, " +
                UPDATED_AT + " VARCHAR(255))";

        String createTableWorkflow = "CREATE TABLE " + WORKFLOW + " ( " +
                ID + " INT NOT NULL PRIMARY KEY, " +
                NAME + " VARCHAR(255) NOT NULL, " +
                PATH + " VARCHAR(255) NOT NULL, " +
                CREATED_AT + " VARCHAR(255) NOT NULL, " +
                UPDATED_AT + " VARCHAR(255) NOT NULL, " +
                HTML_URL + " VARCHAR(255) NOT NULL, " +
                REPOSITORY_ID + " INT NOT NULL, " +
                "CONSTRAINT FK_WORKFLOW FOREIGN KEY (" + REPOSITORY_ID + ") REFERENCES " + REPOSITORY + "(" + ID + "))";

        String createTableWorkflowRun = "CREATE TABLE " + WORKFLOW_RUN + " ( " +
                ID + " INT NOT NULL PRIMARY KEY, " +
                NAME + " VARCHAR(255) NOT NULL, " +
                RUN_NUMBER + " VARCHAR(255) NOT NULL, " +
                EVENT + " VARCHAR(255) NOT NULL, " +
                STATUS + " VARCHAR(255) NOT NULL, " +
                CONCLUSION + " VARCHAR(255) NOT NULL, " +
                WORKFLOW_ID + " INT NOT NULL, " +
                HTML_URL + " VARCHAR(255) NOT NULL, " +
                CREATED_AT + " VARCHAR(255) NOT NULL, " +
                UPDATED_AT + " VARCHAR(255) NOT NULL, " +
                "CONSTRAINT FK_WORKFLOW_RUN FOREIGN KEY (" + WORKFLOW_ID + ") REFERENCES " + WORKFLOW + "(" + ID + "))";

        String createTableWorkflowJob = "CREATE TABLE " + WORKFLOW_JOB + " ( " +
                ID + " INT NOT NULL PRIMARY KEY, " +
                RUN_ID + " INT NOT NULL, " +
                HTML_URL + " VARCHAR(255) NOT NULL, " +
                STATUS + " VARCHAR(255) NOT NULL, " +
                CONCLUSION + " VARCHAR(255) NOT NULL, " +
                STARTED_AT + " VARCHAR(255) NOT NULL, " +
                COMPLETED_AT + " VARCHAR(255) NOT NULL, " +
                NAME + " VARCHAR(255) NOT NULL, " +
                "CONSTRAINT FK_WORKFLOW_JOB FOREIGN KEY (" + RUN_ID + ") REFERENCES " + WORKFLOW_RUN + "(" + ID + "))";

        String createTableStep = "CREATE TABLE " + STEP + " ( " +
                NAME + " VARCHAR(255) NOT NULL, " +
                STATUS + " VARCHAR(255) NOT NULL, " +
                CONCLUSION + " VARCHAR(255) NOT NULL, " +
                NUMBER + " INT NOT NULL PRIMARY KEY, " +
                STARTED_AT + " VARCHAR(255) NOT NULL, " +
                COMPLETED_AT + " VARCHAR(255) NOT NULL, " +
                JOB_ID + " INT NOT NULL PRIMARY KEY)";

        sqLiteDatabase.execSQL(createTableRepository);
        sqLiteDatabase.execSQL(createTableWorkflow);
        sqLiteDatabase.execSQL(createTableWorkflowRun);
        sqLiteDatabase.execSQL(createTableWorkflowJob);
        sqLiteDatabase.execSQL(createTableStep);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + REPOSITORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WORKFLOW);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WORKFLOW_RUN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WORKFLOW_JOB);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STEP);
        onCreate(sqLiteDatabase);
    }
}
