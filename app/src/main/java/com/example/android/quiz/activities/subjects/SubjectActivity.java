package com.example.android.quiz.activities.subjects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterActivity;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.RecyclerViewAdapterSubject;
import com.example.android.quiz.model.Subject;

import java.util.ArrayList;

//dùng recyclerview + cardview
public class SubjectActivity extends AppCompatActivity implements RecyclerViewAdapterSubject.ItemClickListener {
    RecyclerViewAdapterSubject recyclerViewAdapterSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //insertDummyData(); //mới cài app thì bỏ comment để thêm tự động dữ liệu, khi có dữ liệu rồi thì chuyển thành comment
        displayDatabaseInfo(); // hiển thị vào recycler view
    }

    // hiển thị vào recycler view
    private void displayDatabaseInfo(){
        ArrayList<Subject> subjects=new ArrayList<Subject>();
        String[] projectionSubject = {QuizContract.SubjectEntry.COLUMN_SUBJECT_ID,QuizContract.SubjectEntry.COLUMN_SUBJECT_NAME, QuizContract.SubjectEntry.COLUMN_SUBJECT_DESCRIPTION};
        Cursor cursor = getContentResolver().query(QuizContract.SubjectEntry.CONTENT_URI_SUBJECT, projectionSubject, null, null, null);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listOfSubjects);
        recyclerViewAdapterSubject = new RecyclerViewAdapterSubject(this,cursor);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewAdapterSubject.setClickListener(this);
        recyclerView.setAdapter(recyclerViewAdapterSubject);

    }

    // insert dữ liệu đã viết sẵn
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

    // tạo dữ liệu đã viết sẵn
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

    //onClick trên mỗi item cardview, chuyển subjectID và subjectName cho activity Chapter để tạo đúng chapter ở subject vừa chọn
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ChapterActivity.class);
        String subjectID = recyclerViewAdapterSubject.getSubjectID(position);
        String subjectName = recyclerViewAdapterSubject.getSubjectName(position);
        intent.putExtra("SubjectID", subjectID);
        intent.putExtra("SubjectName", subjectName);
        startActivity(intent);
    }
}