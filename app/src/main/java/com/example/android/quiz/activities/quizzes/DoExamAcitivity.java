package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.student.ShowExam.ShowExam;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.Question;

import java.util.ArrayList;

//còn dang dở
public class DoExamAcitivity extends AppCompatActivity {
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_exam);

//        TextView tv_total_time = (TextView) findViewById(R.id.tv_total_time);
//        int totalTimeCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME);
//        int totalTime = cursor.getInt(totalTimeCollumnIndex);
//        tv_total_time.setText(totalTime);
        Button btnStartExam = findViewById(R.id.btn_startExam);
        btnStartExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoExamAcitivity.this, ShowExam.class);
                startActivity(intent);
            }
        });
    }
}