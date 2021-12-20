package com.example.android.quiz.activities.chapters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterAddDialog;
import com.example.android.quiz.activities.quizzes.QuizActivity;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizProvider;
import com.example.android.quiz.model.ListViewCursorAdapterChapter;

//dùng listview + cursor adapter
public class ChapterActivity extends AppCompatActivity implements ChapterAddDialog.ChapterDialogListener  {
    private QuizProvider quizProvider;
    String subjectID;
    String subjectName;
    ListViewCursorAdapterChapter adapterChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        //nhận đúng subjectID và subjectName để tạo đúng chapter trên subject đó
        Intent intent = getIntent();
        subjectID = intent.getStringExtra("SubjectID");
        subjectName = intent.getStringExtra("SubjectName");
        Log.v("SubjectIntent", subjectID +" " + subjectName);


        //SELECT chapterID AS _ID, chapterName, chapterDescription FROM chapter (JOIN subject) WHERE subjectID(FK)= subjectID(PK) (nhận từ intent)
        String[] projectionChapter = {QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " AS " +QuizContract._ID , QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME , QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION};
        String selectionChapter = QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + "=?";
        String[] selectionArgsChapter = {subjectID} ;

        Cursor cursor = getContentResolver().query(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER, projectionChapter, selectionChapter, selectionArgsChapter, null);
        ListView listChapter = (ListView) findViewById(R.id.listOfChapters);
        adapterChapter = new ListViewCursorAdapterChapter(this, cursor);
        listChapter.setAdapter(adapterChapter);

        //onClick trên listview item, trả về chapterID và chapterName để chuyển cho activity Quiz
        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                //chú ý ở đây dùng alias nên dùng _ID mà không phải là chapterID
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                int nameCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME);
                String chapterID=cursor.getString(IDCollumnIndex);
                String name= cursor.getString(nameCollumnIndex);
                Toast.makeText(ChapterActivity.this, "This is a message " + position+" "+ chapterID+" "+ name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChapterActivity.this, QuizActivity.class);
                intent.putExtra("ChapterID",chapterID);
                intent.putExtra("ChapterName", name);
                startActivity(intent);
            }
        });

        //nút tạo chapter và ấn vào hiển thị dialog tạo chapter
        ImageView createChapter = (ImageView) findViewById(R.id.createChapter);
        createChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddChapterDialog();
            }
        });
//        TextView textView = (TextView) findViewById(R.id.testChapter);
//        getContentResolver().call(QuizContract.ChapterEntry.CONTENT_URI_FUNCTION,QuizContract.ChapterEntry.PATH_FUNCTION,null,null);


    }

    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterChapter.notifyDataSetChanged();
    }

    public void displayAddChapterDialog() {
        ChapterAddDialog chapterAddDialog = new ChapterAddDialog();
        chapterAddDialog.show(getSupportFragmentManager(),"chapter");
    }

    //sau khi ấn nút Add ở dialog chapter, sẽ lưu lại ID, name , description Chapter để insert vào database
    @Override
    public void save(String ID, String name, String description) {
        Log.v("Chapter", ID+ " "+ name +" "+ description);
        ContentValues values = new ContentValues();
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_ID, ID);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME, name);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION, description);
        values.put(QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE, subjectID);
        getContentResolver().insert(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER,values);
        adapterChapter.notifyDataSetChanged();
    }
}