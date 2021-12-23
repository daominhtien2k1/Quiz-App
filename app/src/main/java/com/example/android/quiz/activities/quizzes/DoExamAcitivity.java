package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.quiz.R;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.Question;

import java.util.ArrayList;

//còn dang dở
public class DoExamAcitivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_exam);
    }


}