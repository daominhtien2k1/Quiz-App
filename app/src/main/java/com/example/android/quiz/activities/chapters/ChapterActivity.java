package com.example.android.quiz.activities.chapters;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.quizzes.QuizActivity;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizProvider;
import com.example.android.quiz.model.ListViewCursorAdapterChapter;

//dùng listview + cursor adapter
public class ChapterActivity extends AppCompatActivity implements ChapterAddDialog.AddChapterDialogListener, ChapterEditDialog.EditChapterDialogListener{
    private QuizProvider quizProvider;
    String subjectID;
    String subjectName;
    ListViewCursorAdapterChapter adapterChapter;
    ListView listChapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        //nhận đúng subjectID và subjectName để tạo đúng chapter trên subject đó
        Intent intent = getIntent();
        subjectID = intent.getStringExtra("SubjectID");
        subjectName = intent.getStringExtra("SubjectName");
        Log.v("SubjectIntent", subjectID +" " + subjectName);

        listChapter = (ListView) findViewById(R.id.listOfChapters);

        //đổ vào listView
        displayChapter();
        //mặc định sẽ set onClick trên mỗi item để mở Quiz
        clickToOpenQuiz();

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

        Button editChapter = findViewById(R.id.editChapter);


        //sửa lại chapter
        editChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang cam và set lại onclick item để mở Edit Dialog
                for (int i = 0; i < listChapter.getChildCount(); i++) {
                    View listItem = listChapter.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundChapter);
                    ColorDrawable viewColor  = (ColorDrawable) background.getBackground();
                    int colorID= viewColor.getColor();
                    //ấn button Edit để vào chế độ Edit. Màu background chuyển sang màu vàng
                    if(colorID!=Color.parseColor("#FFFF00")) {
                        background.setBackgroundColor(Color.parseColor("#FFFF00"));
                        //set lại onClick trên mỗi item, điều hướng đến dialog Edit
                        clickToEdit();
                    }
                    //ấn button Edit lần nữa để quay trở lại trạng thái ban đầu (không ở chế độ edit). lúc này background chuyển sang màu xanh
                    else {
                        background.setBackgroundColor(Color.parseColor("#55A4F1"));
                        //set lại onClick trên mỗi item về mặc định, điều hướng đến activity quiz. Cursor không có gì thay đổi
                        clickToOpenQuiz();
                    }

                }
            }
        });

        //xóa chapter
        Button removeChapter = findViewById(R.id.removeChapter);
        removeChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang đỏ. //set lại onclick item
                for (int i = 0; i < listChapter.getChildCount(); i++) {
                    View listItem = listChapter.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundChapter);
                    ColorDrawable viewColor  = (ColorDrawable) background.getBackground();
                    int colorID= viewColor.getColor();
                    //ấn button Remove để vào chế độ Remove. Màu background chuyển sang màu đỏ
                    if(colorID!=Color.parseColor("#FF0000")) {
                        background.setBackgroundColor(Color.parseColor("#FF0000"));
                        clickToRemove();
                    }
                    //ấn button Remove lần nữa để quay trở lại trạng thái ban đầu (không ở chế độ Remove). lúc này background chuyển sang màu xanh
                    else {
                        background.setBackgroundColor(Color.parseColor("#55A4F1"));
                        //set lại onClick trên mỗi item về mặc định, điều hướng đến activity quiz. Cursor không có gì thay đổi
                        clickToOpenQuiz();
                    }
                }
            }
        });

        EditText searchChapterText = (EditText) findViewById(R.id.searchChapterText);

        ImageView searchChapter = (ImageView) findViewById(R.id.searchChapter);
        searchChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chapter = searchChapterText.getText().toString();
                displayChapterFilter(chapter);
                clickToOpenQuiz();
            }
        });

    }

    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterChapter.notifyDataSetChanged();
    }

    public void displayAddChapterDialog() {
        ChapterAddDialog chapterAddDialog = new ChapterAddDialog();
        chapterAddDialog.show(getSupportFragmentManager(),"addChapter");
    }

    //sau khi ấn nút Add ở dialog chapter, sẽ lưu lại ID, name , description Chapter để insert vào database
    @Override
    public void add(String ID, String name, String description) {
        Log.v("Chapter", ID+ " "+ name +" "+ description);
        ContentValues values = new ContentValues();
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_ID, ID);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME, name);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION, description);
        values.put(QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE, subjectID);
        getContentResolver().insert(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER,values);
        adapterChapter.notifyDataSetChanged();
        //cập nhật lại listView, lúc này cursor được cập nhật
        displayChapter();
    }


    //đổ vào listView
    private void displayChapter(){
        //SELECT chapterID AS _ID, chapterName, chapterDescription FROM chapter (JOIN subject) WHERE subjectID(FK)= subjectID(PK) (nhận từ intent)
        String[] projectionChapter = {QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " AS " +QuizContract._ID , QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME , QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION};
        String selectionChapter = QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + "=?";
        String[] selectionArgsChapter = {subjectID} ;
        cursor = getContentResolver().query(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER, projectionChapter, selectionChapter, selectionArgsChapter, null);

        adapterChapter = new ListViewCursorAdapterChapter(this, cursor);
        listChapter.setAdapter(adapterChapter);

    }

    private void clickToOpenQuiz(){
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
    }

    private void clickToEdit(){
        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                displayEditChapterDialog();
            }
        });
    }

    public void displayEditChapterDialog() {
        int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
        int nameCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME);
        int descriptionCollumnIndex = cursor.getColumnIndex(QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION);
        String chapterID=cursor.getString(IDCollumnIndex);
        String name= cursor.getString(nameCollumnIndex);
        String description = cursor.getString(descriptionCollumnIndex);
        ChapterEditDialog chapterEditDialog= new ChapterEditDialog();
        Bundle bundle = new Bundle();
        bundle.putString("ChapterID",chapterID);
        bundle.putString("ChapterName",name);
        bundle.putString("ChapterDescription",description);
//        bundle.putString("ChapterCreatAt",chapterID);
//        bundle.putString("ChapterModifyAt",chapterID);
        chapterEditDialog.setArguments(bundle);
        chapterEditDialog.show(getSupportFragmentManager(),"editChapter");
    }

    //sau khi ấn nút Save ở dialog chapter, sẽ lưu lại ID, name , description Chapter để update vào database
    @Override
    public void save(String id, String name, String description) {
        //update database
        ContentValues values = new ContentValues();
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_ID, id);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME, name);
        values.put(QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION, description);
        Uri CONTENT_URI_CHAPTER_ID=Uri.withAppendedPath(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER,id);
        getContentResolver().update( CONTENT_URI_CHAPTER_ID, values, null, null);
        //sau khi edit, cursor được cập nhật->list view được cập nhật. Nhưng vẫn ở chế độ edit, màu nền lại xanh
        displayChapter();
        clickToOpenQuiz();
    }

    private void clickToRemove(){
        listChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                String chapterID=cursor.getString(IDCollumnIndex);
                Uri CONTENT_URI_CHAPTER_ID=Uri.withAppendedPath(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER,chapterID);
                new AlertDialog.Builder(ChapterActivity.this).setTitle("Are you sure?").setMessage("Do you want to delete this chapter?")
                        .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(CONTENT_URI_CHAPTER_ID,null,null);
                        adapterChapter.notifyDataSetChanged();
                        displayChapter();
                        clickToOpenQuiz();
                    }
                }).show();

            }

        });
    }

    //đổ vào listView
    private void displayChapterFilter(String chapterName){
        //SELECT chapterID AS _ID, chapterName, chapterDescription FROM chapter (JOIN subject) WHERE subjectID(FK)= subjectID(PK) (nhận từ intent) AND chapterName= chapterName( nhận từ ô search)
        String[] projectionChapter = {QuizContract.ChapterEntry.COLUMN_CHAPTER_ID + " AS " +QuizContract._ID , QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME , QuizContract.ChapterEntry.COLUMN_CHAPTER_DESCRIPTION};
        String selectionChapter = QuizContract.ChapterEntry.COLUMN_SUBJECT_REFERENCE + "=?" + " AND " + QuizContract.ChapterEntry.COLUMN_CHAPTER_NAME + "=?";
        String[] selectionArgsChapter = {subjectID,chapterName} ;
        Log.v("Helo",chapterName);
        cursor = getContentResolver().query(QuizContract.ChapterEntry.CONTENT_URI_CHAPTER, projectionChapter, selectionChapter, selectionArgsChapter, null);

        adapterChapter = new ListViewCursorAdapterChapter(this, cursor);
        listChapter.setAdapter(adapterChapter);

    }

}