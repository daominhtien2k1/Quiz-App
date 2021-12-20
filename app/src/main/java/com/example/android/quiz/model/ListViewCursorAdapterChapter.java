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


//bind view gồm có Name và Description Chapter
public class ListViewCursorAdapterChapter extends CursorAdapter {
    public ListViewCursorAdapterChapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.chapterName);
        TextView description = (TextView) view.findViewById(R.id.chapterDescription);
        int nameCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME);
        int descriptionCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION);
        String chapterName = cursor.getString(nameCollumnIndex);
        String chapterDescription = cursor.getString(descriptionCollumnIndex);
        name.setText(chapterName);
        description.setText(chapterDescription);

    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}
