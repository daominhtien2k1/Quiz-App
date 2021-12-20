package com.example.android.quiz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_TABLE_SUBJECT = "CREATE TABLE " + QuizContract.SubjectEntry.TABLE_NAME + "("
            + QuizContract.SubjectEntry.COLUMN_SUBJECT_ID + " TEXT PRIMARY KEY, " + QuizContract.SubjectEntry.COLUMN_SUBJECT_NAME
            + " TEXT NOT NULL, " + QuizContract.SubjectEntry.COLUMN_SUBJECT_DESCRIPTION + " TEXT NOT NULL, " + QuizContract.SubjectEntry.COLUMN_SUBJECT_CREATAT
            + " TEXT, " + QuizContract.SubjectEntry.COLUMN_SUBJECT_MODIFYAT + " TEXT );";

    private static final String CREATE_TABLE_CHAPTER = "CREATE TABLE " + QuizContract.ChapterEntry.TABLE_NAME + "("
            + QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " TEXT PRIMARY KEY, " + QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME
            + " TEXT NOT NULL, " + QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION + " TEXT NOT NULL, " + QuizContract.ChapterEntry.COLUMN_CHAPTER_CREATAT
            + " TEXT, " + QuizContract.ChapterEntry.COLUMN_CHAPTER_MODIFYAT + " TEXT, " + QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + ") REFERENCES " + QuizContract.SubjectEntry.TABLE_NAME + " (" +QuizContract.SubjectEntry.COLUMN_SUBJECT_ID +"));";

    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE " + QuizContract.QuizEntry.TABLE_NAME + "("
            + QuizContract.QuizEntry.COLUMN_QUIZ_ID + " TEXT PRIMARY KEY, " + QuizContract.QuizEntry.COLUMN_QUIZ_NAME
            + " TEXT NOT NULL, " + QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS + " INT NOT NULL, " + QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME + " INT NOT NULL, " + QuizContract.QuizEntry.COLUMN_QUIZ_CREATAT
            + " TEXT, " + QuizContract.QuizEntry.COLUMN_QUIZ_MODIFYAT + " TEXT, " + QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + ") REFERENCES " + QuizContract.ChapterEntry.TABLE_NAME + " (" +QuizContract.ChapterEntry.COLUMN_CHAPTER_ID +"));";

    private static final String CREATE_TABLE_QUESTION= "CREATE TABLE " + QuizContract.QuestionEntry.TABLE_NAME + "("
            + QuizContract.QuestionEntry.COLUMN_QUESTION_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION
            + " TEXT NOT NULL, " + QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1 + " TEXT NOT NULL, " +  QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2 + " TEXT NOT NULL, "
            + QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3 + " TEXT NOT NULL, " + QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4 + " TEXT NOT NULL, " + QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER + " TEXT NOT NULL, "
            +QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY + " TEXT, "+ QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE + " TEXT, "
            + "FOREIGN KEY (" + QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE + ") REFERENCES " + QuizContract.QuizEntry.TABLE_NAME + " (" +QuizContract.QuizEntry.COLUMN_QUIZ_ID +"));";
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SUBJECT);
        db.execSQL(CREATE_TABLE_CHAPTER);
        db.execSQL(CREATE_TABLE_QUIZ);
        db.execSQL(CREATE_TABLE_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
