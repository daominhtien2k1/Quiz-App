package com.example.android.quiz.model;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.quiz.R;
import com.example.android.quiz.data.QuizContract;

//bind view gồm có Name và NumOfQuestions Quiz
public class ListViewCursorAdapterQuiz extends CursorAdapter {
    public ListViewCursorAdapterQuiz(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_quiz,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.quizName);
        TextView numOfQuestions = (TextView) view.findViewById(R.id.quizNumOfQuestions);
        TextView totalTime = (TextView) view.findViewById(R.id.quizTotalTime);

        int nameCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
        int numOfQuestionsCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS);
        int totalTimeCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME);

        String quizName = cursor.getString(nameCollumnIndex);
        int quizNumOfQuestions = cursor.getInt(numOfQuestionsCollumnIndex);
        int quizTotalTime = cursor.getInt(totalTimeCollumnIndex);

        name.setText(quizName);
        numOfQuestions.setText(String.valueOf(quizNumOfQuestions)+" questions");
        totalTime.setText(String.valueOf(quizTotalTime) + " minutes");

    }

}
