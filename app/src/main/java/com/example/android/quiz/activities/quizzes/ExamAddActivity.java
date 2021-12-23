package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

//gần hoàn thiện về mặt logic
public class ExamAddActivity extends AppCompatActivity {
    String quizID,quizName;
    int numOfQuestions,totalTime, currentQuestionCount =1;
    RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    TextView currentQuestionView, totalTimeTextView;
    Button previous,next;
    ArrayList<Question> questions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        //nhận đúng quizID ,quizName, numberOfQuestions) để tạo đúng quiz trên subject đó
        Intent intent=getIntent();
        quizID=intent.getStringExtra("QuizID");
        quizName=intent.getStringExtra("QuizName");
        numOfQuestions=intent.getIntExtra("NumOfQuestions",0);
        totalTime=intent.getIntExtra("TotalTime",0);

        initialView();
        //set totalTime(bổ sung vào database) và currentQuestion ban đầu
        currentQuestionView.setText("Question 1 of "+numOfQuestions);
        totalTimeTextView.setText(String.valueOf(totalTime)+":00");
    }

    @Override
    protected void onStart() {
        super.onStart();
        allowChangeRadioButton();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuestion();
                resetView();
                //lần cuối cùng: khi vừa chạy resetView lần cuối-> currentQuestionCount=numOfQuestions
                if(currentQuestionCount==numOfQuestions) {
                    insertDataQuestion();
                    //về giao diện hiển thị danh sách câu hỏi quiz
                    Intent intent = new Intent(ExamAddActivity.this,DoExamAcitivity.class );
                    intent.putExtra("TotalTime", totalTime);
                    intent.putExtra("NumOfQuestions", numOfQuestions);
                    startActivity(intent);
                }
            }
        });
    }

    private void initialView(){
        totalTimeTextView = findViewById(R.id.time);
        questionEditText = findViewById(R.id.question);
        option1EditText = findViewById(R.id.option1);
        option2EditText = findViewById(R.id.option2);
        option3EditText = findViewById(R.id.option3);
        option4EditText = findViewById(R.id.option4);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        questions = new ArrayList<Question>();
        rbOption1=findViewById(R.id.rbOption1);
        rbOption2=findViewById(R.id.rbOption2);
        rbOption3=findViewById(R.id.rbOption3);
        rbOption4=findViewById(R.id.rbOption4);
        currentQuestionView=findViewById(R.id.currentQuestion);
    }


    private void addQuestion(){
        String question = questionEditText.getText().toString();

        String option1= option1EditText.getText().toString();
        String option2= option2EditText.getText().toString();
        String option3= option3EditText.getText().toString();
        String option4= option4EditText.getText().toString();

        //convention: answer chỉ nhận trong 4 giá trị cho ngắn gọn
        String answer="";
        if(rbOption1.isChecked()==true) answer="option1";
        else if(rbOption2.isChecked()==true) answer="option2";
        else if(rbOption3.isChecked()==true) answer="option3";
        else if(rbOption4.isChecked()==true) answer="option4";

        Question oneQuestion = new Question(question,option1,option2,option3,option4,answer);
        questions.add(oneQuestion);
    }

    // insert dữ liệu vào database
    private void insertDataQuestion(){
        for(Question question:questions){
            ContentValues values = new ContentValues();
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION,question.getQuestion());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1,question.getOption1());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2,question.getOption2());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3,question.getOption3());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4,question.getOption4());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER,question.getAnswer());
            values.put(QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE,quizID);
            getContentResolver().insert(QuizContract.QuestionEntry.CONTENT_URI_QUESTION,values);
        }
    }

    //hiển thị sau khi ấn next
    private void resetView(){
        questionEditText.setText("");
        option1EditText.setText("");
        option2EditText.setText("");
        option3EditText.setText("");
        option4EditText.setText("");
        rbOption1.setChecked(false);
        rbOption2.setChecked(false);
        rbOption3.setChecked(false);
        rbOption4.setChecked(false);
        if(currentQuestionCount <numOfQuestions) {
            currentQuestionCount++;
            currentQuestionView.setText("Question " + currentQuestionCount + " of " + numOfQuestions);
        }else{
            currentQuestionView.setText("Question 1 of " + numOfQuestions);
        }
    }
    private void allowChangeRadioButton(){
        rbOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbOption1.setChecked(true);
                if(rbOption2.isChecked())
                    rbOption2.setChecked(false);
                if(rbOption3.isChecked())
                    rbOption3.setChecked(false);
                if(rbOption4.isChecked())
                    rbOption4.setChecked(false);
            }
        });
        rbOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbOption2.setChecked(true);
                if(rbOption1.isChecked())
                    rbOption1.setChecked(false);
                if(rbOption3.isChecked())
                    rbOption3.setChecked(false);
                if(rbOption4.isChecked())
                    rbOption4.setChecked(false);
            }
        });
        rbOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbOption3.setChecked(true);
                if(rbOption1.isChecked())
                    rbOption1.setChecked(false);
                if(rbOption2.isChecked())
                    rbOption2.setChecked(false);
                if(rbOption4.isChecked())
                    rbOption4.setChecked(false);
            }
        });
        rbOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbOption1.isChecked())
                    rbOption1.setChecked(false);
                rbOption4.setChecked(true);
                if(rbOption2.isChecked())
                    rbOption2.setChecked(false);
                if(rbOption3.isChecked())
                    rbOption3.setChecked(false);

            }
        });

    }
}