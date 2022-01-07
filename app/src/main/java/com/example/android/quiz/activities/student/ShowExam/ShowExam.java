package com.example.android.quiz.activities.student.ShowExam;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.student.DisplayQuiz;
import com.example.android.quiz.model.Question;

import java.util.ArrayList;
import java.util.List;

public class ShowExam extends AppCompatActivity {
    private List<Question> questionList;
    LinearLayout linearLayout;
    Button Next_btn;
    ImageView Back_from_exam;
    ImageButton Close_dialog_btn;
    TextView txtQuestions, currQuestion, examName;
    private int position = 0;
    private int count = 0;
    private int score = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exam);
        linearLayout = findViewById(R.id.options_layout);
        txtQuestions = findViewById(R.id.tv_question);
        currQuestion = findViewById(R.id.currentQuestion);
        Next_btn = findViewById(R.id.btn_next);
        Back_from_exam = findViewById(R.id.btn_back_from_exam);
        examName = findViewById(R.id.tv_exam_name);

        examName.setText("Quiz 01");
        questionList = new ArrayList<>();
        questionList.add(new Question("Exception and Error are immediate subclasses of a class called ", "Object", "AWT", "Throwable", "Panel", "Throwable"));
        questionList.add(new Question("The order of the three top level elements of the java source file are", "Import, Package, Class", "Class, Import, Package", "Package, Import, Class", "Random order", "Package, Import, Class"));
        questionList.add(new Question("The minimum value of char type variable is", "‘\\u0020’", "‘\\u00ff’ ", "‘\\u0010’ ", "‘\\u0000’", "‘\\u0000’"));
        questionList.add(new Question("Java uses ___ to represent characters", "ASCII code", "Unicode", "Byte code ", "None of the above ", "Unicode"));
        questionList.add(new Question("Which one is not supported by OOP?", "Abstraction", "Polymorphism", "Encapsulation", "Global variables", "Global variables"));
        questionList.add(new Question("Java programs are ", "Platform-dependent", "Interpreter-dependent", "Platform-independent", "Interpreter-independent ", "Platform-independent"));
        questionList.add(new Question("Command to execute a compiled java program is :", "javac", "java", "run", "execute ", "javac"));
        questionList.add(new Question("______ is a mechanism for naming and visibility control of a class and its content", "Object", "Packages", "Interfaces", "None of the Mentioned", "Packages"));
        questionList.add(new Question("The java compiler", "creates executable ", "translates java source code to byte code ", "creates classes ", "produces java Interpreter ", "translates java source code to byte code "));
        questionList.add(new Question("Two threads cannot simultaneously enter into the methods of the same object if the methods are", "static ", "synchronized", "private", "package", "synchronized"));

        for (int i = 0; i < 4; i++) {
            linearLayout.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAns((Button) v);
                }
            });
        }

        playAnim(txtQuestions, 0, questionList.get(position).getQuestion());

        Next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next_btn.setEnabled(false);
                Next_btn.setAlpha(0.7f);
                enableOptions(true);
                position++;

                if (position == questionList.size()-1) {
                    //Score Activities
                    Next_btn.setText("Submit");
                }

                if (position == questionList.size()){
                    Dialog result = new Dialog(ShowExam.this, R.style.AnimateDialog);
                    result.setContentView(R.layout.activity_popup_score);
                    TextView tv_score = result.findViewById(R.id.tv_score);
                    tv_score.setText(score+"/"+questionList.size());
                    result.show();
                    return;
                }
                count = 0;
                playAnim(txtQuestions, 0, questionList.get(position).getQuestion());
            }
        });

        Back_from_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowExam.this, DisplayQuiz.class);
            }
        });
    }
    private void playAnim(final View view, final int value, final String data) {

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100).setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = questionList.get(position).getOption1();
                    } else if (count == 1) {
                        option = questionList.get(position).getOption2();
                    } else if (count == 2) {
                        option = questionList.get(position).getOption3();
                    } else if (count == 3) {
                        option = questionList.get(position).getOption4();
                    }
                    playAnim(linearLayout.getChildAt(count), 0, option);
                    count++;
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animator animation) {

                if (value == 0) {

                    try {
                        ((TextView) view).setText(data);
                        currQuestion.setText("Question "+(position+1)+" of "+questionList.size());
                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
    private void checkAns(Button selectedOptions) {
        enableOptions(false);
        Next_btn.setEnabled(true);
        Next_btn.setAlpha(1);
        if (selectedOptions.getText().toString().equals(questionList.get(position).getAnswer())) {
            //correct Answer
            score++;
            selectedOptions.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#14E39A")));
        } else {
            //wrong Answer
            selectedOptions.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF2B55")));
            Button correctOption = linearLayout.findViewWithTag(questionList.get(position).getAnswer());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#14E39A")));
        }
    }

    private void enableOptions(boolean enable) {
        for (int i = 0; i < 4; i++) {
            linearLayout.getChildAt(i).setEnabled(enable);
            if (enable) {
                linearLayout.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2133A0")));
            }
        }
    }
}
