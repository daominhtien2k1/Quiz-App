package com.example.android.quiz.activities.quizzes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterActivity;
import com.example.android.quiz.activities.chapters.ChapterEditDialog;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.model.ListViewCursorAdapterQuiz;
import com.example.android.quiz.model.Question;
import com.example.android.quiz.model.Quiz;
import com.example.android.quiz.model.Subject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//dùng listview + cursor adapter
public class QuizActivity extends AppCompatActivity implements QuizAddDialog.AddQuizDialogListener, QuizEditDialog.EditQuizDialogListener {
    String chapterID;
    String chapterName;
    ListViewCursorAdapterQuiz adapterQuiz;
    ListView listQuiz;
    Cursor cursor;
    String quizID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        //nhận đúng chapterID và chapterName để tạo đúng quiz trên subject đó
        Intent intent = getIntent();
        chapterID = intent.getStringExtra("ChapterID");
        chapterName = intent.getStringExtra("ChapterName");

        listQuiz = (ListView) findViewById(R.id.listOfQuizzes);

        //đổ vào listView
        displayQuiz();
        //mặc định sẽ set onClick trên mỗi item để mở Questions
        clickToOpenQuestions();

        //button tạo Quiz và hiển thị dialog tạo quiz
        ImageView createQuiz = (ImageView) findViewById(R.id.createQuiz);
        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddQuizDialog();
            }
        });


        //sửa lại quiz
        Button editQuiz = findViewById(R.id.editQuiz);
        editQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listQuiz.getChildCount(); i++) {
                    View listItem = listQuiz.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuiz);
                    ColorDrawable viewColor  = (ColorDrawable) background.getBackground();
                    int colorID= viewColor.getColor();
                    //ấn button Edit để vào chế độ Edit. Màu background chuyển sang màu vàng
                    if(colorID!= Color.parseColor("#FFFF00")) {
                        background.setBackgroundColor(Color.parseColor("#FFFF00"));
                        //set lại onClick trên mỗi item, điều hướng đến dialog Edit
                        clickToEdit();
                    }
                    //ấn button Edit lần nữa để quay trở lại trạng thái ban đầu (không ở chế độ edit). lúc này background chuyển sang màu xanh
                    else {
                        background.setBackgroundColor(Color.parseColor("#55A4F1"));
                        //set lại onClick trên mỗi item về mặc định, điều hướng đến activity quiz. Cursor không có gì thay đổi
                        clickToOpenQuestions();
                    }


                }
            }
        });


        //xóa Quiz
        Button removeQuiz = findViewById(R.id.removeQuiz);
        removeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang đỏ. //set lại onclick item
                for (int i = 0; i < listQuiz.getChildCount(); i++) {
                    View listItem = listQuiz.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuiz);
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
                        clickToOpenQuestions();
                    }
                }
            }
        });

        EditText searchQuizText = (EditText) findViewById(R.id.searchQuizText);

        ImageView searchQuiz = (ImageView) findViewById(R.id.searchQuiz);
        searchQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quiz = searchQuizText.getText().toString();
                displayQuizFilter(quiz);
                clickToOpenQuestions();
            }
        });


        ImageView exportExcel = (ImageView) findViewById(R.id.exportExcel);
        exportExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //đổi background sang xanh. //set lại onclick item
                for (int i = 0; i < listQuiz.getChildCount(); i++) {
                    View listItem = listQuiz.getChildAt(i);
                    ImageView background = (ImageView) listItem.findViewById(R.id.backgroundQuiz);
                    ColorDrawable viewColor  = (ColorDrawable) background.getBackground();
                    int colorID= viewColor.getColor();
                    //ấn button Remove để vào chế độ Remove. Màu background chuyển sang màu đỏ
                    if(colorID!=Color.parseColor("#88D969")) {
                        background.setBackgroundColor(Color.parseColor("#88D969"));
                        clickToExportExcel();
                    }
                    //ấn button Remove lần nữa để quay trở lại trạng thái ban đầu (không ở chế độ Remove). lúc này background chuyển sang màu xanh
                    else {
                        background.setBackgroundColor(Color.parseColor("#55A4F1"));
                        //set lại onClick trên mỗi item về mặc định, điều hướng đến activity quiz. Cursor không có gì thay đổi
                        clickToOpenQuestions();
                    }
                }
            }
        });
    }



    //sửa lại Quiz




    //cập nhật listview
    @Override
    protected void onResume() {
        super.onResume();
        adapterQuiz.notifyDataSetChanged();
    }

    public void displayAddQuizDialog() {
        QuizAddDialog quizAddDialog= new QuizAddDialog();
        Log.v("Open dialog","OPENED");
        quizAddDialog.show(getSupportFragmentManager(),"quiz");
    }

    //sau khi ấn nút Add ở dialog chapter, sẽ lưu lại ID, name , numOfQuestions Quiz để insert vào database
    @Override
    public void add(String ID, String name, int numberOfQuestions, int totalTime) {
        Log.v("Quiz", ID+ " "+ name+" "+numberOfQuestions+ " "+totalTime);
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_ID, ID);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NAME, name);
        values.put(QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE, chapterID);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS, numberOfQuestions);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, totalTime);
        getContentResolver().insert(QuizContract.QuizEntry.CONTENT_URI_QUIZ,values);
        adapterQuiz.notifyDataSetChanged();
        //cập nhật lại listView, lúc này cursor được cập nhật
        displayQuiz();
    }

    private void displayQuiz(){
        //SELECT quizID AS _ID, quizName, totalTime, numOfQuestions FROM quiz (JOIN chapter) WHERE chapterID(FK)= chapterID(PK) (nhận từ intent)
        String[] projectionQuiz = {QuizContract.QuizEntry.COLUMN_QUIZ_ID + " AS " + QuizContract._ID, QuizContract.QuizEntry.COLUMN_QUIZ_NAME,QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS};
        String selectionQuiz = QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + "=?";
        String[] selectionArgsQuiz = {chapterID} ;
        cursor = getContentResolver().query(QuizContract.QuizEntry.CONTENT_URI_QUIZ, projectionQuiz, selectionQuiz, selectionArgsQuiz, null);

        adapterQuiz = new ListViewCursorAdapterQuiz(this, cursor);
        listQuiz.setAdapter(adapterQuiz);
    }

    private void clickToOpenQuestions(){
        //onClick trên item listview Quiz, nhận ID, name Quiz để đến activity tạo câu hỏi trong quiz
        listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                //chú ý ở đây dùng alias nên dùng _ID mà không phải là quizID
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                int nameCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
                int numOfQuestionsCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS);
                int totalTimeCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME);

                String quizID=cursor.getString(IDCollumnIndex);
                String name= cursor.getString(nameCollumnIndex);
                int numOfQuestions =cursor.getInt(numOfQuestionsCollumnIndex);
                int totalTime = cursor.getInt(totalTimeCollumnIndex);

                Toast.makeText(QuizActivity.this, "This is a message " + position+" "+ quizID+" "+ name+" "+numOfQuestions, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(QuizActivity.this, ExamAddActivity.class);
                intent.putExtra("QuizID",quizID);
                intent.putExtra("QuizName",name);
                intent.putExtra("TotalTime", totalTime);
                intent.putExtra("NumOfQuestions", numOfQuestions);
                startActivity(intent);

            }
        });
    }

    private void clickToEdit(){
        listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                displayEditQuizDialog();
            }
        });
    }

    public void displayEditQuizDialog() {
        int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
        int nameCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NAME);
        int numOfQuestionsCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS);
        int totalTimeCollumnIndex = cursor.getColumnIndex(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME);

        String quizID=cursor.getString(IDCollumnIndex);
        String name= cursor.getString(nameCollumnIndex);
        int numOfQuestions =cursor.getInt(numOfQuestionsCollumnIndex);
        int totalTime = cursor.getInt(totalTimeCollumnIndex);

        QuizEditDialog quizEditDialog= new QuizEditDialog();
        Bundle bundle = new Bundle();
        bundle.putString("QuizID",quizID);
        bundle.putString("QuizName",name);
        bundle.putInt("NumOfQuestions",numOfQuestions);
        bundle.putInt("TotalTime",totalTime);
//        bundle.putString("ChapterCreatAt",chapterID);
//        bundle.putString("ChapterModifyAt",chapterID);
        quizEditDialog.setArguments(bundle);
        quizEditDialog.show(getSupportFragmentManager(),"editQuiz");
    }

    @Override
    public void save(String id, String name, int numberOfQuestions, int totalTime) {
        //update database
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_ID,id);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NAME,name);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS,numberOfQuestions);
        values.put(QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME,totalTime);
        Uri CONTENT_URI_QUIZ_ID=Uri.withAppendedPath(QuizContract.QuizEntry.CONTENT_URI_QUIZ,id);
        getContentResolver().update( CONTENT_URI_QUIZ_ID, values, null, null);
        //sau khi edit, cursor được cập nhật->list view được cập nhật. Nhưng vẫn ở chế độ edit, màu nền lại xanh
        displayQuiz();
        clickToOpenQuestions();
    }

    private void clickToRemove(){
        listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                String quizID=cursor.getString(IDCollumnIndex);
                Uri CONTENT_URI_QUIZ_ID=Uri.withAppendedPath(QuizContract.QuizEntry.CONTENT_URI_QUIZ,quizID);
                new AlertDialog.Builder(QuizActivity.this).setTitle("Are you sure?").setMessage("Do you want to delete this quiz?")
                        .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getContentResolver().delete(CONTENT_URI_QUIZ_ID,null,null);
                        adapterQuiz.notifyDataSetChanged();
                        displayQuiz();
                        clickToOpenQuestions();
                    }
                }).show();

            }

        });
    }

    private void displayQuizFilter(String quiz){
        //SELECT quizID AS _ID, quizName, totalTime, numOfQuestions FROM quiz (JOIN chapter) WHERE chapterID(FK)= chapterID(PK) (nhận từ intent) AND quizName= quizName( nhận từ ô search)
        String[] projectionQuiz = {QuizContract.QuizEntry.COLUMN_QUIZ_ID + " AS " + QuizContract._ID, QuizContract.QuizEntry.COLUMN_QUIZ_NAME,QuizContract.QuizEntry.COLUMN_QUIZ_TOTALTIME, QuizContract.QuizEntry.COLUMN_QUIZ_NUMOFQUESTIONS};
        String selectionQuiz = QuizContract.QuizEntry.COLUMN_CHAPTER_REFERENCE + "=?"+ " AND " + QuizContract.QuizEntry.COLUMN_QUIZ_NAME + "=?";
        String[] selectionArgsQuiz = {chapterID,quiz} ;
        cursor = getContentResolver().query(QuizContract.QuizEntry.CONTENT_URI_QUIZ, projectionQuiz, selectionQuiz, selectionArgsQuiz, null);

        adapterQuiz = new ListViewCursorAdapterQuiz(this, cursor);
        listQuiz.setAdapter(adapterQuiz);
    }

    public void clickToExportExcel(){
        listQuiz.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
                quizID=cursor.getString(IDCollumnIndex);
                new AlertDialog.Builder(QuizActivity.this).setTitle("Export excel").setMessage("Do you want to export this quiz?")
                        .setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                requestPermissions(permissions, 1);
                            } else {
                                exportData();
                            }
                        } else {
                            exportData();
                        }
                    }
                }).show();
            }
        });
    }

    public void exportData(){
        createXlFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportData();
        } else {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void createXlFile() {
        ArrayList<Question> questions = new ArrayList<Question>();
        String[] projectionQuestion = {QuizContract.QuestionEntry.COLUMN_QUESTION_ID + " AS " +QuizContract._ID , QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION , QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1, QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2, QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3, QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4, QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER, QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY};
        String selectionQuestion = QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE + "=?";
        String[] selectionArgsQuestion = {quizID} ;
        Cursor cursor = getContentResolver().query(QuizContract.QuestionEntry.CONTENT_URI_QUESTION, projectionQuestion, selectionQuestion, selectionArgsQuestion, null);
        while (cursor.moveToNext()) {
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
            Question tempQuestion;
            tempQuestion = new Question(ID,question,option1,option2,option3,option4,answer,difficulty);
            questions.add(tempQuestion);

        }

        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        Sheet sheet = null;
        sheet = wb.createSheet("Demo Excel Sheet");
        //Now column and row
        Row row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Question ID");

        cell = row.createCell(1);
        cell.setCellValue("Question");

        cell = row.createCell(2);
        cell.setCellValue("Option 1");

        cell = row.createCell(3);
        cell.setCellValue("Option 2");

        cell = row.createCell(4);
        cell.setCellValue("Option 3");

        cell = row.createCell(5);
        cell.setCellValue("Option 4");

        //column width
        sheet.setColumnWidth(0, (30 * 200));
        sheet.setColumnWidth(1, (30 * 200));
        sheet.setColumnWidth(2, (30 * 200));
        sheet.setColumnWidth(3, (30 * 200));
        sheet.setColumnWidth(4, (30 * 200));
        sheet.setColumnWidth(5, (30 * 200));


        for (int i = 0; i < questions.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(questions.get(i).getQuestionID());

            cell = row1.createCell(1);
            cell.setCellValue(questions.get(i).getQuestion());
            //  cell.setCellStyle(cellStyle);

            cell = row1.createCell(2);
            cell.setCellValue(questions.get(i).getOption1());

            cell = row1.createCell(3);
            cell.setCellValue(questions.get(i).getOption2());

            cell = row1.createCell(4);
            cell.setCellValue(questions.get(i).getOption3());

            cell = row1.createCell(5);
            cell.setCellValue(questions.get(i).getOption4());


            sheet.setColumnWidth(0, (30 * 200));
            sheet.setColumnWidth(1, (30 * 200));
            sheet.setColumnWidth(2, (30 * 200));
            sheet.setColumnWidth(3, (30 * 200));
            sheet.setColumnWidth(4, (30 * 200));
            sheet.setColumnWidth(5, (30 * 200));

        }
        String folderName = "Export Excel";
        String fileName = folderName + System.currentTimeMillis() + ".xls";
        String path = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(getApplicationContext(), "Excel Created in " + path, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Not OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }


    }
}