package com.example.android.quiz.activities.quizzes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.android.quiz.R;

//nhập ID bằng tay Q01_CH01_SB01
public class QuizAddDialog extends DialogFragment{
    private AddQuizDialogListener listener;
    private EditText quizID ;
    private EditText quizName ;
    private EditText quizNumberOfQuestions;
    private EditText quizTotalTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_quiz, null);
        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID = quizID.getText().toString();
                        String name = quizName.getText().toString();
                        int numberOfQuestions = Integer.valueOf(quizNumberOfQuestions.getText().toString());
                        int totalTime = Integer.valueOf(quizTotalTime.getText().toString());
                        listener.add(ID,name,numberOfQuestions,totalTime);
                    }
                });
        quizID =(EditText)view.findViewById(R.id.inputQuizID);
        quizName =(EditText) view.findViewById(R.id.inputQuizName);
        quizNumberOfQuestions = (EditText) view.findViewById(R.id.inputQuizNumOfQuestions);
        quizTotalTime = (EditText) view.findViewById(R.id.inputQuizTotalTime);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddQuizDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement1 AddQuizDialogListener");
        }
    }



    public interface AddQuizDialogListener {
        public void add(String id, String name, int numberOfQuestions, int totalTime);
    }
}