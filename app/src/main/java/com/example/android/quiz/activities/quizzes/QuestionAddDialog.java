package com.example.android.quiz.activities.quizzes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.android.quiz.R;

//nhập ID bằng tay CH01_SB01
public class QuestionAddDialog extends DialogFragment{
    private AddQuestionDialogListener listener;
    private EditText question,option1,option2,option3,option4;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    String[] items ={"Easy","Medium","Difficult"};
    ArrayAdapter<String> adapterItems;
    AutoCompleteTextView autoCompleteTextView;
    String level;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_question, null);


        question =(EditText)view.findViewById(R.id.inputQuestion);
        option1 = (EditText) view.findViewById(R.id.inputOption1);
        option2 = (EditText) view.findViewById(R.id.inputOption2);
        option3 = (EditText) view.findViewById(R.id.inputOption3);
        option4 = (EditText) view.findViewById(R.id.inputOption4);
        rbOption1=(RadioButton) view.findViewById(R.id.rbOption1);
        rbOption2=(RadioButton) view.findViewById(R.id.rbOption2);
        rbOption3=(RadioButton) view.findViewById(R.id.rbOption3);
        rbOption4=(RadioButton) view.findViewById(R.id.rbOption4);

        autoCompleteTextView=view.findViewById(R.id.auto_complete_txt);
        adapterItems= new ArrayAdapter<String>(builder.getContext(),R.layout.dropdown_item,items);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                level = parent.getItemAtPosition(position).toString();
            }
        });

        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        allowChangeRadioButton();
                        String aQuestion = question.getText().toString();
                        String aOption1 = option1.getText().toString();
                        String aOption2 = option2.getText().toString();
                        String aOption3 = option3.getText().toString();
                        String aOption4 = option4.getText().toString();
                        String answer="";
                        if(rbOption1.isChecked()==true) answer="option1";
                        else if(rbOption2.isChecked()==true) answer="option2";
                        else if(rbOption3.isChecked()==true) answer="option3";
                        else if(rbOption4.isChecked()==true) answer="option4";
                        listener.add(aQuestion,aOption1,aOption2,aOption3,aOption4,answer,level);
                    }
                });


        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddQuestionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement1 AddChapterDialogListener");
        }
    }




    public interface AddQuestionDialogListener {
        public void add(String question, String option1, String option2, String option3, String option4,  String answer, String difficulty);
    }

    public void allowChangeRadioButton(){
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