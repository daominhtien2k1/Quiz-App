package com.example.android.quiz.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QuizProvider extends ContentProvider {
    private QuizDbHelper quizDbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        //thêm Uri cần nhận diện cùng mã code trả về
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.SubjectEntry.PATH_SUBJECT, QuizContract.SubjectEntry.CODE_SUBJECT);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.SubjectEntry.PATH_SUBJECT_ID, QuizContract.SubjectEntry.CODE_SUBJECT_ID);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.ChapterEntry.PATH_CHAPTER, QuizContract.ChapterEntry.CODE_CHAPTER);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.ChapterEntry.PATH_CHAPTER_ID, QuizContract.ChapterEntry.CODE_CHAPTER_ID);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.QuizEntry.PATH_QUIZ, QuizContract.QuizEntry.CODE_QUIZ);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.QuizEntry.PATH_QUIZ_ID, QuizContract.QuizEntry.CODE_QUIZ_ID);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.QuestionEntry.PATH_QUESTION, QuizContract.QuestionEntry.CODE_QUESTION);
        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.QuestionEntry.PATH_QUESTION_ID, QuizContract.QuestionEntry.CODE_QUESTION_ID);

        uriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.ChapterEntry.PATH_FUNCTION, 10);

    }

    @Override
    public boolean onCreate() {
        quizDbHelper = new QuizDbHelper(getContext());
        return true;
    }

    //query
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database=quizDbHelper.getReadableDatabase();
        Cursor cursor;
        int match=uriMatcher.match(uri);
        int identify=match%2; //xác định số chẵn hay lẻ
        switch (identify){
            case 1: //query table, số lẻ là query table
                cursor=queryTable(uri,database,projection,selection,selectionArgs,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    //query table chỉ định
    private Cursor queryTable(Uri uri, SQLiteDatabase database, String[] projection, String selection, String[] selectionArgs,
                                     String sortOrder){
        Cursor cursor;
        int match=uriMatcher.match(uri); //đã xác định là số lẻ nên là query table
        switch (match) {
            case QuizContract.SubjectEntry.CODE_SUBJECT:
                cursor = database.query(QuizContract.SubjectEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QuizContract.ChapterEntry.CODE_CHAPTER:
                cursor = database.query(QuizContract.ChapterEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QuizContract.QuizEntry.CODE_QUIZ:
                cursor = database.query(QuizContract.QuizEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case QuizContract.QuestionEntry.CODE_QUESTION:
                cursor = database.query(QuizContract.QuestionEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match=uriMatcher.match(uri);
        int rowsUpdated;
        SQLiteDatabase database=quizDbHelper.getWritableDatabase();
        switch (match) {
            case QuizContract.ChapterEntry.CODE_CHAPTER_ID:
                selection =  QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsUpdated=database.update(QuizContract.ChapterEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case QuizContract.QuizEntry.CODE_QUIZ_ID:
                selection =  QuizContract.QuizEntry.COLUMN_QUIZ_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsUpdated=database.update(QuizContract.QuizEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot update unknown URI " + uri);
        }
        return rowsUpdated;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match=uriMatcher.match(uri);
        int rowsDeleted;
        SQLiteDatabase database=quizDbHelper.getWritableDatabase();
        switch (match) {
            case QuizContract.ChapterEntry.CODE_CHAPTER_ID:
                selection =  QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsDeleted=database.delete(QuizContract.ChapterEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case QuizContract.QuizEntry.CODE_QUIZ_ID:
                selection =  QuizContract.QuizEntry.COLUMN_QUIZ_ID + "=?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                rowsDeleted=database.delete(QuizContract.QuizEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Cannot delete unknown URI " + uri);
        }
        return rowsDeleted;
    }

    //insert
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match=uriMatcher.match(uri);
        long id;
        SQLiteDatabase database=quizDbHelper.getWritableDatabase();
        switch (match) {
            case QuizContract.SubjectEntry.CODE_SUBJECT:
               id= database.insert(QuizContract.SubjectEntry.TABLE_NAME,null,values);
               break;
            case QuizContract.ChapterEntry.CODE_CHAPTER:
                id= database.insert(QuizContract.ChapterEntry.TABLE_NAME,null,values);
                break;
            case QuizContract.QuizEntry.CODE_QUIZ:
                id= database.insert(QuizContract.QuizEntry.TABLE_NAME,null,values);
                break;
            case QuizContract.QuestionEntry.CODE_QUESTION:
                id= database.insert(QuizContract.QuestionEntry.TABLE_NAME,null,values);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    public Cursor queryJoin(String sql) {
        SQLiteDatabase database = quizDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        return cursor;
    }
    public void save() {

        Log.d("Test method 2", "called");
        SQLiteDatabase database = quizDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from chapter",null);
        Log.d("Test method 2", String.valueOf(cursor.getCount()));
       //Bundle bundle= cursor.getExtras();

       //return bundle;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if(method.equals(QuizContract.ChapterEntry.PATH_FUNCTION)) {
            Log.d("Test method 1", "HELLLLLLLLLLLLLLLLO");
            //Bundle bundle = save();
            //return bundle;
            save();
        }
        return null;
    }


}
