package com.example.android.quiz.activities.chapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.android.quiz.R;

//nhập ID bằng tay CH01_SB01
public class ChapterAddDialog extends DialogFragment{
    private AddChapterDialogListener listener;
    private EditText chapterID ;
    private EditText chapterName ;
    private EditText chapterDescription ;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_chapter, null);
        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID = chapterID.getText().toString();
                        String name = chapterName.getText().toString();
                        String description = chapterDescription.getText().toString();
                        listener.add(ID,name,description);
                    }
                });
        chapterID =(EditText)view.findViewById(R.id.inputChapterID);
        chapterName =(EditText) view.findViewById(R.id.inputChapterName);
        chapterDescription = (EditText) view.findViewById(R.id.inputChapterDescription);

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {  Log.v("SUCCESS","uhuh");
            listener = (AddChapterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement1 AddChapterDialogListener");
        }
    }




    public interface AddChapterDialogListener {
        public void add(String id, String name, String description);
    }
}