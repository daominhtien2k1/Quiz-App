package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.quiz.R;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.ListViewCursorAdapterQuiz;

//dùng listview + cursor adapter
public class QuizActivity extends AppCompatActivity implements QuizAddDialog.QuizDialogListener  {
    String chapterID;
    String chapterName;
    ListViewCursorAdapterQuiz adapterQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //nhận đúng chapterID và chapterName để tạo đúng quiz trên subject đó
        Intent intent = getIntent();
        chapterID = intent.getStringExtra("ChapterID");
        chapterName = intent.getStringExtra("ChapterName");

        //SELECT quizID AS _ID, quizName, totalTime, numOfQuestions FROM quiz (JOIN chapter) WHERE chapterID(FK)= chapterID(PK) (nhận từ intent)
        String[] projectionQuiz = {QuizContract.QuizEntry.COLUMN_QUIZ_ID + " AS " + QuizContract._ID, QuizContract.QuizEntry.COLUMN_QUIZ_NAME,QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS};
        String selectionQuiz = QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + "=?";
        String[] selectionArgsQuiz = {chapterID} ;

        Cursor cursor = getContentResolver().query(QuizContract.QuizEntry.CONTENT_URI_QUIZ, projectionQuiz, selectionQuiz, selectionArgsQuiz, null);
        ListView listQuiz = (ListView) findViewById(R.id.listOfQuizzes);
        adapterQuiz = new ListViewCursorAdapterQuiz(this, cursor);
        listQuiz.setAdapter(adapterQuiz);

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

                Toast.makeText(QuizActivity.this, "This is a message " + position+" "+ quizID+" "+ name+" "+numOfQuestions, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(QuizActivity.this, ExamAddActivity.class);
                intent.putExtra("QuizID",quizID);
                intent.putExtra("QuizName",name);
                intent.putExtra("TotalTime", totalTime);
                intent.putExtra("NumOfQuestions", numOfQuestions);
                startActivity(intent);

            }
        });

        //button tạo Quiz và hiển thị dialog tạo quiz
        ImageView createQuiz = (ImageView) findViewById(R.id.createQuiz);
        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddQuizDialog();
            }
        });
    }

    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterQuiz.notifyDataSetChanged();
    }

    public void displayAddQuizDialog() {
        QuizAddDialog quizAddDialog= new QuizAddDialog();
        Log.v("Open dialog","OPENED");
        quizAddDialog.show(getSupportFragmentManager(),"quiz");
    }

    //sau khi ấn nút Add ở dialog chapter, sẽ lưu lại ID, name , numOfQuestions Quiz để insert vào database
    @Override
    public void save(String ID, String name, int numberOfQuestions, int totalTime) {
        Log.v("Quiz", ID+ " "+ name+" "+numberOfQuestions+ " "+totalTime);
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_ID, ID);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NAME, name);
        values.put(QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE, chapterID);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS, numberOfQuestions);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, totalTime);
        getContentResolver().insert(QuizContract.QuizEntry.CONTENT_URI_QUIZ,values);
        adapterQuiz.notifyDataSetChanged();
    }
}