package com.example.android.quiz.activities.student;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.LoginActivity;
import com.example.android.quiz.activities.chapters.ChapterActivity;
import com.example.android.quiz.activities.quizzes.DoExamAcitivity;
import com.example.android.quiz.activities.student.ShowExam.ShowExam;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.RecyclerViewAdapterSubject;
import com.example.android.quiz.model.Subject;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements RecyclerViewAdapterSubject.ItemClickListener{
    RecyclerViewAdapterSubject recyclerViewAdapterSubject;
    ImageButton back_to_login;
    ImageButton avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_home);
        back_to_login = findViewById(R.id.btn_back_to_login);
        avatar = findViewById(R.id.ibtn_avatar_user);

        back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog logout = new Dialog(HomeScreen.this, R.style.AnimateDialog);
                logout.setContentView(R.layout.dialog_logout);
                ImageButton close = findViewById(R.id.ibtn_close);
                Button signout = findViewById(R.id.btn_signout);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomeScreen.this, HomeScreen.class);
                        startActivity(i);
                    }
                });
                signout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomeScreen.this, LoginActivity.class);
                        startActivity(i);
                    }
                });
                logout.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        insertDummyData();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        ArrayList<Subject> subjects=new ArrayList<Subject>();
        String[] projectionSubject = {QuizContract.SubjectEntry.COLUMN_SUBJECT_ID,QuizContract.SubjectEntry.COLUMN_SUBJECT_NAME, QuizContract.SubjectEntry.COLUMN_SUBJECT_DESCRIPTION};
        Cursor cursor = getContentResolver().query(QuizContract.SubjectEntry.CONTENT_URI_SUBJECT, projectionSubject, null, null, null);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rclv_home);
        recyclerViewAdapterSubject = new RecyclerViewAdapterSubject(this,cursor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewAdapterSubject.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapterSubject);
    }

    private void insertDummyData(){
        ArrayList<Subject> subjects=createAvailableSubject();
        for(Subject subject:subjects){
            ContentValues values = new ContentValues();
            values.put(QuizContract.SubjectEntry.COLUMN_SUBJECT_ID,subject.getSubjectID());
            values.put(QuizContract.SubjectEntry.COLUMN_SUBJECT_NAME,subject.getName());
            values.put(QuizContract.SubjectEntry.COLUMN_SUBJECT_DESCRIPTION,subject.getDescription());
            values.put(QuizContract.SubjectEntry.COLUMN_SUBJECT_CREATAT,subject.getCreateAt());
            values.put(QuizContract.SubjectEntry.COLUMN_SUBJECT_MODIFYAT,subject.getModifyAt());
            getContentResolver().insert(QuizContract.SubjectEntry.CONTENT_URI_SUBJECT,values);
        }
    }

    private ArrayList<Subject> createAvailableSubject(){
        ArrayList<Subject> subjects= new ArrayList<Subject>();
        Subject subject1= new Subject("SB01","JavaScript","JavaScript is a scripting or programming language that allows you to implement complex features on web pages", "", "");
        subjects.add(subject1);
        Subject subject2= new Subject("SB02","HTML","HTML is the standard markup language for creating Web pages","","");
        subjects.add(subject2);
        Subject subject3= new Subject("SB03","Angular","Angular is a platform for building mobile and desktop web applications","","");
        subjects.add(subject3);
        Subject subject4= new Subject("SB04","Bootstrap","Bootstrap is the most popular CSS Framework for developing responsive and mobile-first websites.","","");
        subjects.add(subject4);
        return subjects;
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DisplayChapter.class);
        String subjectID = recyclerViewAdapterSubject.getSubjectID(position);
        String subjectName = recyclerViewAdapterSubject.getSubjectName(position);
        intent.putExtra("SubjectID", subjectID);
        intent.putExtra("SubjectName", subjectName);
        startActivity(intent);
    }
}
