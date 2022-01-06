package com.example.android.quiz.activities.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterAddDialog;
import com.example.android.quiz.activities.chapters.ChapterEditDialog;
import com.example.android.quiz.activities.quizzes.QuizActivity;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizProvider;
import com.example.android.quiz.model.ListViewCursorAdapterChapter;

//dùng listview + cursor adapter
public class DisplayChapter extends AppCompatActivity {
    String subjectID;
    String subjectName;
    ListViewCursorAdapterChapter adapterChapter;
    ListView listChapter;
    Cursor cursor;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_display_chapter);
        back = findViewById(R.id.btn_back);

        //nhận đúng subjectID và subjectName để tạo đúng chapter trên subject đó
        Intent intent = getIntent();
        subjectID = intent.getStringExtra("SubjectID");
        subjectName = intent.getStringExtra("SubjectName");
        Log.v("SubjectIntent", subjectID +" " + subjectName);

        listChapter = (ListView) findViewById(R.id.lv_chapter);

        //đổ vào listView
        displayChapter();
        //mặc định sẽ set onClick trên mỗi item để mở Quiz
        clickToOpenQuiz();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayChapter.this, HomeScreen.class);
                startActivity(i);
            }
        });
    }

    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterChapter.notifyDataSetChanged();
    }

    //đổ vào listView
    private void displayChapter(){
        //SELECT chapterID AS _ID, chapterName, chapterDescription FROM chapter (JOIN subject) WHERE subjectID(FK)= subjectID(PK) (nhận từ intent)
        String[] projectionChapter = {QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " AS " +QuizContract._ID , QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME , QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION};
        String selectionChapter = QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + "=?";
        String[] selectionArgsChapter = {subjectID} ;
        cursor = getContentResolver().query(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER, projectionChapter, selectionChapter, selectionArgsChapter, null);

        adapterChapter = new ListViewCursorAdapterChapter(this, cursor);
        listChapter.setAdapter(adapterChapter);
    }

    private void clickToOpenQuiz(){
        //onClick trên listview item, trả về chapterID và chapterName để chuyển cho activity Quiz
        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                //chú ý ở đây dùng alias nên dùng _ID mà không phải là chapterID
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                int nameCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME);

                String chapterID=cursor.getString(IDCollumnIndex);
                String name= cursor.getString(nameCollumnIndex);

//                Toast.makeText(ChapterActivity.this, "This is a message " + position+" "+ chapterID+" "+ name, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DisplayChapter.this, DisplayQuiz.class);
                intent.putExtra("ChapterID",chapterID);
                intent.putExtra("ChapterName", name);
                startActivity(intent);
            }
        });
    }

    //đổ vào listView
    private void displayChapterFilter(String chapterName){
        //SELECT chapterID AS _ID, chapterName, chapterDescription FROM chapter (JOIN subject) WHERE subjectID(FK)= subjectID(PK) (nhận từ intent) AND chapterName= chapterName( nhận từ ô search)
        String[] projectionChapter = {QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " AS " +QuizContract._ID , QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME , QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION};
        String selectionChapter = QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + "=?" + " AND " + QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME + "=?";
        String[] selectionArgsChapter = {subjectID,chapterName} ;
        Log.v("Helo",chapterName);
        cursor = getContentResolver().query(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER, projectionChapter, selectionChapter, selectionArgsChapter, null);

        adapterChapter = new ListViewCursorAdapterChapter(this, cursor);
        listChapter.setAdapter(adapterChapter);
    }

}