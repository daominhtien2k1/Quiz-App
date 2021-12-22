package com.example.android.quiz.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountHelper extends SQLiteOpenHelper {
    static String DATABASE_NAME="UserDataBase";

    public static final String TABLE_NAME="UserTable";

    public static final String Table_Column_ID="id";

    public static final String Table_Column_Name="name";

    public static final String Table_Column_Email="email";

    public static final String Table_Column_Password="password";

    public static final String Table_Column_Role="role";

    public AccountHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+ "("+Table_Column_ID+" INTEGER PRIMARY KEY, "+Table_Column_Name+" VARCHAR, "+Table_Column_Email+" VARCHAR, "+Table_Column_Password+" VARCHAR, "+Table_Column_Role+" VARCHAR"+")";
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}
