package com.example.android.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.student.HomeScreen;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizDbHelper;

public class LoginActivity extends AppCompatActivity {
    Button LogInButton;
    EditText Email, Password ;
    String EmailHolder, PasswordHolder;
    Boolean EditTextEmptyHolder;
    SQLiteDatabase sqLiteDatabaseObj;
    QuizDbHelper quizDbHelper;
    Cursor cursor;
    String TempPassword = "NOT_FOUND";
    RadioButton isTeacherButton;
    public static final String UserEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LogInButton = (Button)findViewById(R.id.btnLogin);

        Email = (EditText)findViewById(R.id.etvEmail);
        Password = (EditText)findViewById(R.id.etvPassword);
        isTeacherButton = (RadioButton)findViewById(R.id.rbtnRole);

        quizDbHelper = new QuizDbHelper(this);

        sqLiteDatabaseObj = quizDbHelper.getWritableDatabase();
        ContentValues tempValues = new ContentValues();
        tempValues.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_ID,1);
        tempValues.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_NAME,"tien");
        tempValues.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_EMAIL,"minhtien");
        tempValues.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_PASSWORD,"hello");
        tempValues.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_ROLE,"Teacher");
        sqLiteDatabaseObj.insert(QuizContract.AccountEntry.TABLE_NAME,null,tempValues);

        ContentValues std = new ContentValues();
        std.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_ID,2);
        std.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_NAME,"anhlp");
        std.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_EMAIL,"anhlp@gmail.com");
        std.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_PASSWORD,"123456");
        std.put(QuizContract.AccountEntry.COLUMN_ACCOUNT_ROLE,"Student");
        sqLiteDatabaseObj.insert(QuizContract.AccountEntry.TABLE_NAME,null, std);

        //Adding click listener to log in button.
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calling EditText is empty or no method.
                CheckEditTextStatus();
                // Calling login method.
                LoginFunction();
            }
        });
    }

    // Login function starts from here.
    public void LoginFunction(){
        if(EditTextEmptyHolder) {
            // Opening SQLite database write permission.
            sqLiteDatabaseObj = quizDbHelper.getWritableDatabase();

            // Adding search email query to cursor.
            cursor = sqLiteDatabaseObj.query(QuizContract.AccountEntry.TABLE_NAME, null, " " + QuizContract.AccountEntry.COLUMN_ACCOUNT_EMAIL+ "=?", new String[]{EmailHolder}, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst();
                    // Storing Password associated with entered email.
                    TempPassword = cursor.getString(cursor.getColumnIndex(QuizContract.AccountEntry.COLUMN_ACCOUNT_PASSWORD));
                    // Closing cursor.
                    cursor.close();
                }
            }
            CheckFinalResult();
        }
        else {
            //If any of login EditText empty then this block will be executed.
            Toast.makeText(LoginActivity.this,"Please Enter UserName or Password.",Toast.LENGTH_LONG).show();
        }
    }

    // Checking EditText is empty or not.
    public void CheckEditTextStatus(){
        // Getting value from All EditText and storing into String Variables.
        EmailHolder = Email.getText().toString();
        PasswordHolder = Password.getText().toString();
        // Checking EditText is empty or no using TextUtils.
        if( TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder)){
            EditTextEmptyHolder = false ;
        }
        else {
            EditTextEmptyHolder = true ;
        }
    }

    // Checking entered password from SQLite database email associated password.
    public void CheckFinalResult(){
        ContentValues contentValues=new ContentValues();
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (compoundButton, b) -> {
            if(isTeacherButton.isChecked()){
                contentValues.put("role", isTeacherButton.getText().toString());
            }
            else {
                contentValues.put("role", "Student");
            }
        };
        isTeacherButton.setOnCheckedChangeListener(onCheckedChangeListener);
        if(TempPassword.equalsIgnoreCase(PasswordHolder))
        {
            Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_LONG).show();
            // Going to Dashboard activity after login success message.

            if(isTeacherButton.isChecked()){
                Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                intent.putExtra(UserEmail, EmailHolder);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
                intent.putExtra(UserEmail, EmailHolder);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(LoginActivity.this,"UserName or Password is Wrong, Please Try Again.",Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND" ;
    }
}