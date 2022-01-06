package com.example.android.quiz.activities.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.quiz.R;
import com.example.android.quiz.activities.quizzes.DoExamAcitivity;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.ListViewCursorAdapterQuiz;

//dùng listview + cursor adapter
public class DisplayQuiz extends AppCompatActivity {
    String chapterID;
    String chapterName;
    ListViewCursorAdapterQuiz adapterQuiz;
    ListView listQuiz;
    Cursor cursor;
    String quizID;
    ImageButton back;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_student_display_chapter);
        back = findViewById(R.id.btn_back);
        title = findViewById(R.id.tv_chapter_title);

        title.setText("Quizzes");

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        //nhận đúng chapterID và chapterName để tạo đúng quiz trên subject đó
        Intent intent = getIntent();
        chapterID = intent.getStringExtra("ChapterID");
        chapterName = intent.getStringExtra("ChapterName");

        listQuiz = (ListView) findViewById(R.id.lv_chapter);

        //đổ vào listView
        displayQuiz();
        //mặc định sẽ set onClick trên mỗi item để mở Questions
        clickToOpenQuestions();

//        EditText searchQuizText = (EditText) findViewById(R.id.searchQuizText);
//        ImageView searchQuiz = (ImageView) findViewById(R.id.searchQuiz);
//        searchQuiz.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String quiz = searchQuizText.getText().toString();
//                displayQuizFilter(quiz);
//                clickToOpenQuestions();
//            }
//        });

//        ImageView exportExcel = (ImageView) findViewById(R.id.exportExcel);
//        exportExcel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //đổi background sang xanh. //set lại onclick item
//                for (int i = 0; i < listQuiz.getChildCount(); i++) {
//                    View listItem = listQuiz.getChildAt(i);
//                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuiz);
//                    ColorDrawable viewColor  = (ColorDrawable) background.getBackground();
//                    int colorID= viewColor.getColor();
//                    //ấn button Remove để vào chế độ Remove. Màu background chuyển sang màu đỏ
//                    if(colorID!=Color.parseColor("#88D969")) {
//                        background.setBackgroundColor(Color.parseColor("#88D969"));
//                        clickToExportExcel();
//                    }
//                    //ấn button Remove lần nữa để quay trở lại trạng thái ban đầu (không ở chế độ Remove). lúc này background chuyển sang màu xanh
//                    else {
//                        background.setBackgroundColor(Color.parseColor("#55A4F1"));
//                        //set lại onClick trên mỗi item về mặc định, điều hướng đến activity quiz. Cursor không có gì thay đổi
//                        clickToOpenQuestions();
//                    }
//                }
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DisplayQuiz.this, DisplayChapter.class);
                startActivity(i);
            }
        });
    }

    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterQuiz.notifyDataSetChanged();
    }

    private void displayQuiz(){
        //SELECT quizID AS _ID, quizName, totalTime, numOfQuestions FROM quiz (JOIN chapter) WHERE chapterID(FK)= chapterID(PK) (nhận từ intent)
        String[] projectionQuiz = {QuizContract.QuizEntry.COLUMN_QUIZ_ID + " AS " + QuizContract._ID, QuizContract.QuizEntry.COLUMN_QUIZ_NAME,QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS};
        String selectionQuiz = QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + "=?";
        String[] selectionArgsQuiz = {chapterID} ;
        cursor = getContentResolver().query(QuizContract.QuizEntry.CONTENT_URI_QUIZ, projectionQuiz, selectionQuiz, selectionArgsQuiz, null);

        adapterQuiz = new ListViewCursorAdapterQuiz(this, cursor);
        listQuiz.setAdapter(adapterQuiz);
    }

    private void clickToOpenQuestions(){
        //onClick trên item listview Quiz, nhận ID, name Quiz để đến activity tạo câu hỏi trong quiz
        listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                //chú ý ở đây dùng alias nên dùng _ID mà không phải là quizID
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                int nameCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
                int numOfQuestionsCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS);
                int totalTimeCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME);

                String quizID=cursor.getString(IDCollumnIndex);
                String name= cursor.getString(nameCollumnIndex);
                int numOfQuestions =cursor.getInt(numOfQuestionsCollumnIndex);
                int totalTime = cursor.getInt(totalTimeCollumnIndex);

                Toast.makeText(DisplayQuiz.this, "This is a message " + position+" "+ quizID+" "+ name+" "+numOfQuestions, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(DisplayQuiz.this, DoExamAcitivity.class);
                intent.putExtra("QuizID",quizID);
                intent.putExtra("QuizName",name);
                intent.putExtra("TotalTime", totalTime);
                intent.putExtra("NumOfQuestions", numOfQuestions);
                startActivity(intent);

            }
        });
    }

//    private void displayQuizFilter(String quiz){
//        //SELECT quizID AS _ID, quizName, totalTime, numOfQuestions FROM quiz (JOIN chapter) WHERE chapterID(FK)= chapterID(PK) (nhận từ intent) AND quizName= quizName( nhận từ ô search)
//        String[] projectionQuiz = {QuizContract.QuizEntry.COLUMN_QUIZ_ID + " AS " + QuizContract._ID, QuizContract.QuizEntry.COLUMN_QUIZ_NAME,QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS};
//        String selectionQuiz = QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + "=?"+ " AND " + QuizContract.QuizEntry.COLUMN_QUIZ_NAME + "=?";
//        String[] selectionArgsQuiz = {chapterID,quiz} ;
//        cursor = getContentResolver().query(QuizContract.QuizEntry.CONTENT_URI_QUIZ, projectionQuiz, selectionQuiz, selectionArgsQuiz, null);
//
//        adapterQuiz = new ListViewCursorAdapterQuiz(this, cursor);
//        listQuiz.setAdapter(adapterQuiz);
//    }
}