package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterActivity;
import com.example.android.quiz.activities.chapters.ChapterAddDialog;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizDbHelper;
import com.example.android.quiz.model.ListViewCursorAdapterChapter;
import com.example.android.quiz.model.ListViewCursorAdapterQuestion;

public class QuestionBankActivity extends AppCompatActivity  implements QuestionAddDialog.AddQuestionDialogListener, QuestionEditDialog.EditQuestionDialogListener {
    ListViewCursorAdapterQuestion adapterQuestion;
    ListView listQuestion;
    Cursor cursor;
    SQLiteDatabase sqLiteDatabaseObj;
    QuizDbHelper quizDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        quizDbHelper = new QuizDbHelper(this);

        listQuestion = findViewById(R.id.listOfQuestions);

        //đổ vào listView
        displayQuestion();

        //nút tạo chapter và ấn vào hiển thị dialog tạo chapter
        ImageView createQuestion= (ImageView) findViewById(R.id.createQuestion);
        createQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddQuestionDialog();
            }
        });

        //sửa lại question
        Button editQuestion = findViewById(R.id.editQuestion);
        editQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang cam và set lại onclick item để mở Edit Dialog
                for (int i = 0; i < listQuestion.getChildCount(); i++) {
                    View listItem = listQuestion.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuestion);
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
                        //set lại onClick trên mỗi item về mặc định Cursor không có gì thay đổi
                        clickToNone();
                    }

                }
            }
        });

        Button removeQuestion = findViewById(R.id.removeQuestion);
        removeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang đỏ. //set lại onclick item
                for (int i = 0; i < listQuestion.getChildCount(); i++) {
                    View listItem = listQuestion.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuestion);
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
                        //set lại onClick trên mỗi item về mặc định. Cursor không có gì thay đổi
                        clickToNone();
                    }
                }
            }
        });

    }

    //đổ vào listView
    private void displayQuestion(){
        sqLiteDatabaseObj = quizDbHelper.getWritableDatabase();
        cursor=sqLiteDatabaseObj.rawQuery("SELECT "+QuizContract.QuestionEntry.COLUMN_QUESTION_ID + " AS " +QuizContract._ID +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION +","+ QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1 +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2 +","+QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3 +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4 +","+QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY +" FROM "+ QuizContract.QuestionEntry.TABLE_NAME +" WHERE " + QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE+" IS NULL",null);

        adapterQuestion = new ListViewCursorAdapterQuestion(this, cursor);
        listQuestion.setAdapter(adapterQuestion);
    }

    public void displayAddQuestionDialog() {
        QuestionAddDialog questionAddDialog = new QuestionAddDialog();
        questionAddDialog.show(getSupportFragmentManager(),"addQuestion");
    }

    @Override
    public void add(String question, String option1, String option2, String option3, String option4, String answer, String difficulty) {
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION,question);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1,option1);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2,option2);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3,option3);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4,option4);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER,answer);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY,difficulty);
        getContentResolver().insert(QuizContract.QuestionEntry.CONTENT_URI_QUESTION,values);
        adapterQuestion.notifyDataSetChanged();
        displayQuestion();
    }

    private void clickToEdit(){
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Log.v("HUHUHUHUH",""+position);
                displayEditQuestionDialog();
            }
        });
    }

    private void displayEditQuestionDialog(){
        int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
        int questionCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION);
        int option1CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1);
        int option2CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2);
        int option3CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3);
        int option4CollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4);
        int answerCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER);
        int difficultyCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY);

        int ID = cursor.getInt(IDCollumnIndex);
        String question = cursor.getString(questionCollumnIndex);
        String option1 = cursor.getString(option1CollumnIndex);
        String option2 = cursor.getString(option2CollumnIndex);
        String option3 = cursor.getString(option3CollumnIndex);
        String option4 = cursor.getString(option4CollumnIndex);
        String answer = cursor.getString(answerCollumnIndex);
        String difficulty = cursor.getString(difficultyCollumnIndex);

        Bundle bundle = new Bundle();
        bundle.putInt("ID",ID);
        bundle.putString("Question",question);
        bundle.putString("Option1",option1);
        bundle.putString("Option2",option2);
        bundle.putString("Option3",option3);
        bundle.putString("Option4",option4);
        bundle.putString("Answer",answer);
        bundle.putString("Difficulty",difficulty);

        QuestionEditDialog questionEditDialog = new QuestionEditDialog();
        questionEditDialog.setArguments(bundle);
        questionEditDialog.show(getSupportFragmentManager(),"editQuestion");

    }

    private void clickToRemove(){
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Log.v("HUHUHUHUH",""+position);
                displayRemoveQuestionDialog();
            }
        });
    }

    private void displayRemoveQuestionDialog(){
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                int questionID=cursor.getInt(IDCollumnIndex);
                Uri CONTENT_URI_QUESTION_ID=Uri.withAppendedPath(QuizContract.QuestionEntry.CONTENT_URI_QUESTION,String.valueOf(questionID));
                new AlertDialog.Builder(QuestionBankActivity.this).setTitle("Are you sure?").setMessage("Do you want to delete this chapter?")
                        .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(CONTENT_URI_QUESTION_ID,null,null);
                        adapterQuestion.notifyDataSetChanged();
                        displayQuestion();

                    }
                }).show();

            }

        });
    }

    @Override
    public void save(int ID, String question, String option1, String option2, String option3, String option4, String answer, String difficulty) {
        //update database
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION,question);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1,option1);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2,option2);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3,option3);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4,option4);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER,answer);
        values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY,difficulty);
        Uri CONTENT_URI_QUESTION_ID=Uri.withAppendedPath(QuizContract.QuestionEntry.CONTENT_URI_QUESTION,String.valueOf(ID));
        getContentResolver().update(CONTENT_URI_QUESTION_ID,values,null,null);
        adapterQuestion.notifyDataSetChanged();
        displayQuestion();
    }

    private void clickToNone(){
        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //None to do
            }
        });
    }
}