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
    String quizID,quizName;
    int numOfQuestions,totalTime, currentQuestionCount =1;
    RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    EditText totalTimeEditText, questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    TextView currentQuestionView;
    Button previous,next;
    ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_exam);


        //test
//        Intent intent=getIntent();
//        quizID=intent.getStringExtra("QuizID");
//        quizName=intent.getStringExtra("QuizName");
//        numOfQuestions=intent.getIntExtra("NumOfQuestions",0);
//        totalTime=intent.getIntExtra("TotalTime",0);
//
//        initialView();
//        setUpListQuestion();
//        // set thông tin examm lần đầu tiên
//        resetView();

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        allowChangeRadioButton();
//
//      // cập nhật khi ấn next
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentQuestionCount<numOfQuestions) {
//                    currentQuestionCount++;
//                    currentQuestionView.setText("Question " + currentQuestionCount + " of " + numOfQuestions);
//                    resetView();
//                }
//                    //show result
//                }
//
//        });
//    }
//
//    private void initialView(){
//        totalTimeEditText = findViewById(R.id.time);
//        questionEditText = findViewById(R.id.question);
//        option1EditText = findViewById(R.id.option1);
//        option2EditText = findViewById(R.id.option2);
//        option3EditText = findViewById(R.id.option3);
//        option4EditText = findViewById(R.id.option4);
//        previous = findViewById(R.id.previous);
//        next = findViewById(R.id.next);
//        questions = new ArrayList<Question>();
//        rbOption1=findViewById(R.id.rbOption1);
//        rbOption2=findViewById(R.id.rbOption2);
//        rbOption3=findViewById(R.id.rbOption3);
//        rbOption4=findViewById(R.id.rbOption4);
//        currentQuestionView=findViewById(R.id.currentQuestion);
//    }
//
//
//    //hiển thị trước khi ấn next
//    private void resetView(){
//        // bắt đầu bởi chỉ số 0
//        questionEditText.setText(questions.get(currentQuestionCount-1).getQuestion());
//        option1EditText.setText(questions.get(currentQuestionCount-1).getOption1());
//        option2EditText.setText(questions.get(currentQuestionCount-1).getOption2());
//        option3EditText.setText(questions.get(currentQuestionCount-1).getOption3());
//        option4EditText.setText(questions.get(currentQuestionCount-1).getOption4());
//        rbOption1.setChecked(false);
//        rbOption2.setChecked(false);
//        rbOption3.setChecked(false);
//        rbOption4.setChecked(false);
//
//    }
//    private void allowChangeRadioButton(){
//        rbOption1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rbOption1.setChecked(true);
//                if(rbOption2.isChecked())
//                    rbOption2.setChecked(false);
//                if(rbOption3.isChecked())
//                    rbOption3.setChecked(false);
//                if(rbOption4.isChecked())
//                    rbOption4.setChecked(false);
//            }
//        });
//        rbOption2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rbOption2.setChecked(true);
//                if(rbOption1.isChecked())
//                    rbOption1.setChecked(false);
//                if(rbOption3.isChecked())
//                    rbOption3.setChecked(false);
//                if(rbOption4.isChecked())
//                    rbOption4.setChecked(false);
//            }
//        });
//        rbOption3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rbOption3.setChecked(true);
//                if(rbOption1.isChecked())
//                    rbOption1.setChecked(false);
//                if(rbOption2.isChecked())
//                    rbOption2.setChecked(false);
//                if(rbOption4.isChecked())
//                    rbOption4.setChecked(false);
//            }
//        });
//        rbOption4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(rbOption1.isChecked())
//                    rbOption1.setChecked(false);
//                rbOption4.setChecked(true);
//                if(rbOption2.isChecked())
//                    rbOption2.setChecked(false);
//                if(rbOption3.isChecked())
//                    rbOption3.setChecked(false);
//
//            }
//        });
//
//    }
//
//    private void setUpListQuestion(){
//        //SELECT questionID AS _ID, question, option1, option2, option3, option4 FROM question (JOIN quiz) WHERE quizID(FK)= quizID(PK) (nhận từ intent)
//        String[] projectionQuestion = {QuizContract.QuestionEntry.COLUMN_QUESTION_ID + " AS " +QuizContract._ID , QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION , QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1,QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2,QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3,QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4,QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER};
//        String selectionQuestion = QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE + "=?";
//        String[] selectionArgsQuestion = {quizID} ;
//        Cursor cursor = getContentResolver().query(QuizContract.QuestionEntry.CONTENT_URI_QUESTION, projectionQuestion, selectionQuestion, selectionArgsQuestion, null);
//        while(cursor.moveToNext()){
//            int indexOfQuestion = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION);
//            int indexOfOption1 = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1);
//            int indexOfOption2 = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2);
//            int indexOfOption3 = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3);
//            int indexOfOption4 = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4);
//            int indexOfAnswer = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER);
//            String question = cursor.getString(indexOfQuestion);
//            String option1 = cursor.getString(indexOfOption1);
//            String option2 = cursor.getString(indexOfOption2);
//            String option3 = cursor.getString(indexOfOption3);
//            String option4 = cursor.getString(indexOfOption4);
//            String answer= cursor.getString(indexOfAnswer);
//
//            Question oneQuestion = new Question(question,option1,option2,option3,option4,answer);
//            questions.add(oneQuestion);
//        }
//
//    }
}