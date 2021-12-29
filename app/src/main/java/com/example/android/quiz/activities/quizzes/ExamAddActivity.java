package com.example.android.quiz.activities.quizzes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quiz.R;
import com.example.android.quiz.data.QuizContract;
import com.example.android.quiz.data.QuizDbHelper;
import com.example.android.quiz.model.ListViewCursorAdapterQuestion;
import com.example.android.quiz.model.Question;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.Arrays;

//gần hoàn thiện về mặt logic
public class ExamAddActivity extends AppCompatActivity {
    String quizID,quizName,level;
    int numOfQuestions,totalTime, currentQuestionCount =1;
    RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    EditText questionEditText, option1EditText, option2EditText, option3EditText, option4EditText;
    TextView currentQuestionView, totalTimeTextView;
    ImageView importQuestion;
    AutoCompleteTextView autoCompleteTextView;
    Button previous,next;
    ArrayList<Question> questions;
    String[] items ={"Easy","Medium","Difficult"};
    ArrayAdapter<String> adapterItems;
    Boolean[] checkForQuestion;
    Cursor cursor;
    SQLiteDatabase sqLiteDatabaseObj;
    QuizDbHelper quizDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_test);

        //nhận đúng quizID ,quizName, numberOfQuestions) để tạo đúng quiz trên subject đó
        Intent intent=getIntent();
        quizID=intent.getStringExtra("QuizID");
        quizName=intent.getStringExtra("QuizName");
        numOfQuestions=intent.getIntExtra("NumOfQuestions",0);
        totalTime=intent.getIntExtra("TotalTime",0);

        checkForQuestion = new Boolean[numOfQuestions+1];
        Arrays.fill(checkForQuestion, Boolean.FALSE);

        initialView();
        //set totalTime(bổ sung vào database) và currentQuestion ban đầu
        currentQuestionView.setText("Question 1 of "+numOfQuestions);
        totalTimeTextView.setText(String.valueOf(totalTime)+":00");

        adapterItems= new ArrayAdapter<String>(this,R.layout.dropdown_item,items);
        autoCompleteTextView.setAdapter(adapterItems);

        quizDbHelper = new QuizDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        allowChangeRadioButton();
        if(currentQuestionCount==1) previous.setVisibility(View.INVISIBLE);
        if(currentQuestionCount!=1) previous.setVisibility(View.VISIBLE);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                level = parent.getItemAtPosition(position).toString();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu là câu cuối đã nhập
                if((checkForQuestion[currentQuestionCount]==true) && (currentQuestionCount == numOfQuestions)){
                    Log.v("test","test");
                    insertDataQuestion();
                    //về giao diện hiển thị danh sách câu hỏi quiz
                    Intent intent = new Intent(ExamAddActivity.this, DoExamAcitivity.class);
                    intent.putExtra("TotalTime", totalTime);
                    intent.putExtra("NumOfQuestions", numOfQuestions);
                    startActivity(intent);
                }
                //nếu câu hiện tại chưa nhập
                else if(checkForQuestion[currentQuestionCount]==false) {
                    addQuestion();
                    Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                    if (currentQuestionCount == numOfQuestions)
                        currentQuestionCount++;
                    if (currentQuestionCount < numOfQuestions)
                        resetView();
                        Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                    //lần cuối cùng: khi vừa chạy resetView lần cuối-> currentQuestionCount=numOfQuestions
                    if (currentQuestionCount == numOfQuestions + 1) {
                        insertDataQuestion();
                        //về giao diện hiển thị danh sách câu hỏi quiz
                        Intent intent = new Intent(ExamAddActivity.this, DoExamAcitivity.class);
                        intent.putExtra("TotalTime", totalTime);
                        intent.putExtra("NumOfQuestions", numOfQuestions);
                        startActivity(intent);
                    }
                }
                //nếu câu tiếp theo đã nhập thì giờ cần phục hồi (tức là đang ở chế độ review previous, giờ next quay lại ban đầu)
                else if(checkForQuestion[currentQuestionCount+1]==true){
                    saveRecentlyEdited();
                    restoreNextView();
                    Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                    //nếu câu hiện tại đã nhập,đã insert và vừa sửa lại, câu tiếp theo chưa nhập
                } else if(checkForQuestion[currentQuestionCount+1]==false){
                    saveRecentlyEdited();
                    resetView();
                    Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForQuestion[currentQuestionCount]==false) {
                    addQuestion();
                    Log.v("Test",currentQuestionCount+":"+checkForQuestion[currentQuestionCount].toString());
                    restorePreView();
                    Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                }else if(checkForQuestion[currentQuestionCount]==true) {
                    saveRecentlyEdited();
                    restorePreView();
                    Toast.makeText(ExamAddActivity.this,"This is "+currentQuestionCount,Toast.LENGTH_SHORT).show();
                }
            }
        });

        importQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ExamAddActivity.this);
                builderSingle.setTitle("Select a question form question bank");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ExamAddActivity.this, android.R.layout.select_dialog_singlechoice);
                setUpDialogListQuestion(arrayAdapter);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("Item at", ""+which);
                        String strName = arrayAdapter.getItem(which);
                        cursor.moveToPosition(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(ExamAddActivity.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Your selected question is:");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                importToForm();
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();
            }
        });
    }

    private void initialView(){
        totalTimeTextView = findViewById(R.id.time);
        questionEditText = findViewById(R.id.question);
        option1EditText = findViewById(R.id.option1);
        option2EditText = findViewById(R.id.option2);
        option3EditText = findViewById(R.id.option3);
        option4EditText = findViewById(R.id.option4);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        questions = new ArrayList<Question>();
        rbOption1=findViewById(R.id.rbOption1);
        rbOption2=findViewById(R.id.rbOption2);
        rbOption3=findViewById(R.id.rbOption3);
        rbOption4=findViewById(R.id.rbOption4);
        currentQuestionView=findViewById(R.id.currentQuestion);
        autoCompleteTextView=findViewById(R.id.auto_complete_txt);
        importQuestion =findViewById(R.id.importQuestion);
    }


    private void addQuestion(){
        String question = questionEditText.getText().toString();

        String option1= option1EditText.getText().toString();
        String option2= option2EditText.getText().toString();
        String option3= option3EditText.getText().toString();
        String option4= option4EditText.getText().toString();

        //convention: answer chỉ nhận trong 4 giá trị cho ngắn gọn
        String answer="";
        if(rbOption1.isChecked()==true) answer="option1";
        else if(rbOption2.isChecked()==true) answer="option2";
        else if(rbOption3.isChecked()==true) answer="option3";
        else if(rbOption4.isChecked()==true) answer="option4";

        Question oneQuestion = new Question(question,option1,option2,option3,option4,answer,level);
        questions.add(oneQuestion);
        checkForQuestion[questions.size()]=true;
    }

    // insert dữ liệu vào database
    private void insertDataQuestion(){
        for(Question question:questions){
            ContentValues values = new ContentValues();
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION,question.getQuestion());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1,question.getOption1());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2,question.getOption2());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3,question.getOption3());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4,question.getOption4());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER,question.getAnswer());
            values.put(QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY,question.getDifficulty());
            values.put(QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE,quizID);
            getContentResolver().insert(QuizContract.QuestionEntry.CONTENT_URI_QUESTION,values);
        }
    }

    //hiển thị sau khi ấn next
    private void resetView(){
        questionEditText.setText("");
        option1EditText.setText("");
        option2EditText.setText("");
        option3EditText.setText("");
        option4EditText.setText("");
        rbOption1.setChecked(false);
        rbOption2.setChecked(false);
        rbOption3.setChecked(false);
        rbOption4.setChecked(false);
        currentQuestionCount++;
        currentQuestionView.setText("Question " + currentQuestionCount + " of " + numOfQuestions);
        if(currentQuestionCount!=1) previous.setVisibility(View.VISIBLE);
    }
    private void allowChangeRadioButton(){
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

    private void restorePreView(){
        currentQuestionCount--;
        if(currentQuestionCount==1) previous.setVisibility(View.INVISIBLE);
        questionEditText.setText(questions.get(currentQuestionCount-1).getQuestion());
        option1EditText.setText(questions.get(currentQuestionCount-1).getOption1());
        option2EditText.setText(questions.get(currentQuestionCount-1).getOption2());
        option3EditText.setText(questions.get(currentQuestionCount-1).getOption3());
        option4EditText.setText(questions.get(currentQuestionCount-1).getOption4());
       // autoCompleteTextView.setText(questions.get(currentQuestionCount-1).getDifficulty());
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option1")){
            rbOption1.setChecked(true);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option2")) {
            rbOption2.setChecked(true);
            rbOption1.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option3")){
            rbOption3.setChecked(true);
            rbOption4.setChecked(false);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);

        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option4")){
            rbOption4.setChecked(true);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
        }

        currentQuestionView.setText("Question " + currentQuestionCount + " of " + numOfQuestions);
    }

    private void restoreNextView(){
        currentQuestionCount++;
        if(currentQuestionCount!=1) previous.setVisibility(View.VISIBLE);
        questionEditText.setText(questions.get(currentQuestionCount-1).getQuestion());
        option1EditText.setText(questions.get(currentQuestionCount-1).getOption1());
        option2EditText.setText(questions.get(currentQuestionCount-1).getOption2());
        option3EditText.setText(questions.get(currentQuestionCount-1).getOption3());
        option4EditText.setText(questions.get(currentQuestionCount-1).getOption4());
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option1")){
            rbOption1.setChecked(true);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option2")) {
            rbOption2.setChecked(true);
            rbOption1.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option3")){
            rbOption3.setChecked(true);
            rbOption4.setChecked(false);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);

        }
        if(questions.get(currentQuestionCount - 1).getAnswer().equals("option4")){
            rbOption4.setChecked(true);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
        }
        //autoCompleteTextView.setText(questions.get(currentQuestionCount-1).getDifficulty());
        currentQuestionView.setText("Question " + currentQuestionCount + " of " + numOfQuestions);
    }

    private void saveRecentlyEdited(){
        questions.get(currentQuestionCount-1).setQuestion(questionEditText.getText().toString());
        questions.get(currentQuestionCount-1).setOption1(option1EditText.getText().toString());
        questions.get(currentQuestionCount-1).setOption2(option2EditText.getText().toString());
        questions.get(currentQuestionCount-1).setOption3(option3EditText.getText().toString());
        questions.get(currentQuestionCount-1).setOption4(option4EditText.getText().toString());
        String answer="";
        if(rbOption1.isChecked()==true) answer="option1";
        else if(rbOption2.isChecked()==true) answer="option2";
        else if(rbOption3.isChecked()==true) answer="option3";
        else if(rbOption4.isChecked()==true) answer="option4";
        questions.get(currentQuestionCount-1).setAnswer(answer);
    }

    private void setUpDialogListQuestion(ArrayAdapter<String> array){
        sqLiteDatabaseObj = quizDbHelper.getReadableDatabase();
        cursor=sqLiteDatabaseObj.rawQuery("SELECT "+QuizContract.QuestionEntry.COLUMN_QUESTION_ID + " AS " +QuizContract._ID +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION +","+ QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION1 +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION2 +","+QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION3 +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_OPTION4 +","+QuizContract.QuestionEntry.COLUMN_QUESTION_ANSWER +","
                +QuizContract.QuestionEntry.COLUMN_QUESTION_DIFFICULTY +" FROM "+ QuizContract.QuestionEntry.TABLE_NAME +" WHERE " + QuizContract.QuestionEntry.COLUMN_QUIZ_REFERENCE+" IS NULL",null);
        while(cursor.moveToNext()){
            Log.v("Position",""+cursor.getPosition());
            int IDCollumnIndex = cursor.getColumnIndex(QuizContract._ID);
            int questionCollumnIndex = cursor.getColumnIndex(QuizContract.QuestionEntry.COLUMN_QUESTION_QUESTION);
            int ID = cursor.getInt(IDCollumnIndex);
            String question = cursor.getString(questionCollumnIndex);
            array.add(question);
        }
    }
    private void importToForm(){
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

        questionEditText.setText(question);
        option1EditText.setText(option1);
        option2EditText.setText(option2);
        option3EditText.setText(option3);
        option4EditText.setText(option4);
        // autoCompleteTextView.setText(questions.get(currentQuestionCount-1).getDifficulty());
        if(answer.equals("option1")){
            rbOption1.setChecked(true);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(answer.equals("option2")) {
            rbOption2.setChecked(true);
            rbOption1.setChecked(false);
            rbOption3.setChecked(false);
            rbOption4.setChecked(false);
        }
        if(answer.equals("option3")){
            rbOption3.setChecked(true);
            rbOption4.setChecked(false);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);

        }
        if(answer.equals("option4")){
            rbOption4.setChecked(true);
            rbOption1.setChecked(false);
            rbOption2.setChecked(false);
            rbOption3.setChecked(false);
        }
    }

}