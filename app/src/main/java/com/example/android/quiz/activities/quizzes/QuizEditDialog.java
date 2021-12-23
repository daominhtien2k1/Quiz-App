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
public class QuizEditDialog extends DialogFragment{
    private EditQuizDialogListener listener;
    private EditText quizIDView;
    private EditText quizNameView;
    private EditText quizNumberOfQuestionsView;
    private EditText quizTotalTimeView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_quiz, null);

        Bundle bundle = getArguments();
        String id = bundle.getString("QuizID");
        String name = bundle.getString("QuizName");
        int numOfQuestions = bundle.getInt("NumOfQuestions",0);
        int totalTime = bundle.getInt("TotalTime",0);
        quizIDView =(EditText)view.findViewById(R.id.inputQuizID);
        quizNameView =(EditText) view.findViewById(R.id.inputQuizName);
        quizNumberOfQuestionsView = (EditText) view.findViewById(R.id.inputQuizNumOfQuestions);
        quizTotalTimeView = (EditText) view.findViewById(R.id.inputQuizTotalTime);
        quizIDView.setText(id);
        quizNameView.setText(name);
        quizNumberOfQuestionsView.setText(String.valueOf(numOfQuestions));
        quizTotalTimeView.setText(String.valueOf(totalTime));

        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID = quizIDView.getText().toString();
                        String name = quizNameView.getText().toString();
                        int numberOfQuestions = Integer.valueOf(quizNumberOfQuestionsView.getText().toString());
                        int totalTime = Integer.valueOf(quizTotalTimeView.getText().toString());
                        listener.save(ID,name,numberOfQuestions,totalTime);
                    }
                });

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditQuizDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement1 EditQuizDialogListener");
        }
    }



    public interface EditQuizDialogListener {
        public void save(String id, String name, int numberOfQuestions, int totalTime);
    }
}