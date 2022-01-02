package com.example.android.quiz.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.quizzes.QuestionBankActivity;
import com.example.android.quiz.activities.subjects.SubjectActivity;


public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

    }
    @Override
    protected void onStart() {
        super.onStart();
        ImageView block1 = (ImageView) findViewById(R.id.block1);
        ImageView block2 = (ImageView) findViewById(R.id.block2);
        block1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, SubjectActivity.class);
                startActivity(intent);
            }
        });
        block2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, QuestionBankActivity.class);
                startActivity(intent);
            }
        });
    }


}