package com.example.android.quiz.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.android.quiz.R;
import com.example.android.quiz.data.QuizContract;

//bind view gồm có Name và NumOfQuestions Quiz
public class ListViewCursorAdapterQuestion extends CursorAdapter {
    public ListViewCursorAdapterQuestion(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_question,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView levelText = (TextView) view.findViewById(R.id.level);
        TextView questionText = (TextView) view.findViewById(R.id.question);
        TextView option1Text = (TextView) view.findViewById(R.id.option1);
        TextView option2Text = (TextView) view.findViewById(R.id.option2);
        TextView option3Text = (TextView) view.findViewById(R.id.option3);
        TextView option4Text = (TextView) view.findViewById(R.id.option4);



        int questionCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION);
        int option1CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1);
        int option2CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2);
        int option3CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3);
        int option4CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4);
        int difficultCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY);


        String question = cursor.getString(questionCollumnIndex);
        String option1 = cursor.getString(option1CollumnIndex);
        String option2 = cursor.getString(option2CollumnIndex);
        String option3 = cursor.getString(option3CollumnIndex);
        String option4 = cursor.getString(option4CollumnIndex);
        String difficulty = cursor.getString(difficultCollumnIndex);

       levelText.setText("Level: "+difficulty);
       questionText.setText(question);
       option1Text.setText(option1);
       option2Text.setText(option2);
       option3Text.setText(option3);
       option4Text.setText(option4);


    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}
