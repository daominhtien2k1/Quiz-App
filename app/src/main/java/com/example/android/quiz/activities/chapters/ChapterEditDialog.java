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
public class ChapterEditDialog extends DialogFragment{
    private EditChapterDialogListener listener;
    private EditText chapterIDView;
    private EditText chapterNameView;
    private EditText chapterDescriptionView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Use the Builder class for convenient dialog construction
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_chapter, null);

        Bundle bundle = getArguments();
        String id = bundle.getString("ChapterID");
        String name = bundle.getString("ChapterName");
        String description = bundle.getString("ChapterDescription");
        chapterIDView =(EditText)view.findViewById(R.id.inputChapterID);
        chapterNameView =(EditText) view.findViewById(R.id.inputChapterName);
        chapterDescriptionView = (EditText) view.findViewById(R.id.inputChapterDescription);
        chapterIDView.setText(id);
        chapterNameView.setText(name);
        chapterDescriptionView.setText(description);

        builder.setView(view)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ID = chapterIDView.getText().toString();
                        String name = chapterNameView.getText().toString();
                        String description = chapterDescriptionView.getText().toString();
                        listener.save(ID,name,description);
                    }
                });


        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {  Log.v("SUCCESS","uhuh");
            listener = (EditChapterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement1 EditChapterDialogListener");
        }
    }



    public interface EditChapterDialogListener {
        public void save(String id, String name, String description);
    }
}