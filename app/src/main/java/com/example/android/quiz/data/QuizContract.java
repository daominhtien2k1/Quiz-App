package com.example.android.quiz.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class QuizContract {
    private QuizContract() {
    }

    // com.example.android.quiz
    public static final String CONTENT_AUTHORITY = "com.example.android.quiz";

    // content://com.example.android.quiz
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //ID có sẵn sử dụng để làm alias
    public final static String _ID = BaseColumns._ID;

    //Table Subject
    public static final class SubjectEntry implements BaseColumns{
        //convention: Truy vấn đến bảng->code trả về là số lẻ; Truy vấn đến cột->code trả về là số chẵn
       //Truy vấn đến bảng Subject, mã code trả về 1
        public static final String PATH_SUBJECT="subject";
        public static final int CODE_SUBJECT =1;

        //Truy vấn đến cột Subject, mã code trả về 2
        public static final String PATH_SUBJECT_ID="subject/#";
        public static final int CODE_SUBJECT_ID =2;

        //Uri truy vấn đến bảng Subject: content://com.example.android.quiz/subject
        public static final Uri CONTENT_URI_SUBJECT=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_SUBJECT);


        public static final String TABLE_NAME="subject";
        public static final String COLUMN_SUBJECT_ID="subjectID"; //primary key //convention: SB01 SB02
        public static final String COLUMN_SUBJECT_NAME="name"; // not null
        public static final String COLUMN_SUBJECT_DESCRIPTION ="description"; //not null
        public static final String COLUMN_SUBJECT_CREATAT="creatAt"; //allow null
        public static final String COLUMN_SUBJECT_MODIFYAT="modifyAt"; //allow null
    }

    //Table Chapter
    public static final class ChapterEntry implements BaseColumns{
        //Truy vấn đến bảng Chapter, mã code trả về 3
        public static final String PATH_CHAPTER="chapter";
        public static final int CODE_CHAPTER =3;

        //Truy vấn đến cột Chapter, mã code trả về 4
        public static final String PATH_CHAPTER_ID="chapter/#";
        public static final int CODE_CHAPTER_ID =4;

        //Uri truy vấn đến bảng Chapter: content://com.example.android.quiz/chapter
        public static final Uri CONTENT_URI_CHAPTER=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CHAPTER);

        public static final String PATH_FUNCTION="myfunction";
        public static final Uri CONTENT_URI_FUNCTION=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_FUNCTION);


        public static final String TABLE_NAME="chapter";
        public static final String COLUMN_CHAPTER_ID ="chapterID"; //primary key //convention: CH01_SB01 (chương 1 subject 1)  CH02_SB01 (chương 2 subject 1)
        public static final String COLUMN_CHAPTER_NAME ="name"; //not null //convention: Chapter 1 Chapter 2
        public static final String COLUMN_CHAPTER_DESCRIPTION ="description"; //not null
        public static final String COLUMN_CHAPTER_CREATAT ="creatAt";//allow null
        public static final String COLUMN_CHAPTER_MODIFYAT ="modifyAt";//allow null
        public static final String COLUMN_SUBJECT_REFERENCE = "subjectID";//foreign key not null

    }

    //Table Quiz
    public static final class QuizEntry implements BaseColumns{
        //Truy vấn đến bảng Quiz, mã code trả về 5
        public static final String PATH_QUIZ="quiz";
        public static final int CODE_QUIZ =5;

        //Truy vấn đến cột Quiz, mã code trả về 6
        public static final String PATH_QUIZ_ID="quiz/#";
        public static final int CODE_QUIZ_ID =6;

        //Uri truy vấn đến bảng Quiz: content://com.example.android.quiz/quiz
        public static final Uri CONTENT_URI_QUIZ=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_QUIZ);

        public static final String TABLE_NAME="quiz";
        public static final String COLUMN_QUIZ_ID ="quizID";//primary key //convention: Q01_CH01_SB01 (quiz 1 chương 1 subject 1)  Q01_CH02_SB01 (quiz 1 chương 2 subject 1)
        public static final String COLUMN_QUIZ_NAME ="name"; //not null //convention: Quiz 1 Quiz 2
        public static final String COLUMN_QUIZ_NUMOFQUESTIONS ="numOfQuestions"; //not null
        public static final String COLUMN_QUIZ_TOTALTIME="totalTime"; //not null
        public static final String COLUMN_QUIZ_CREATAT ="creatAt";//allow null
        public static final String COLUMN_QUIZ_MODIFYAT ="modifyAt";//allow null
        public static final String COLUMN_CHAPTER_REFERENCE = "chapterID";//foreign key not null

    }

    //Table Question
    public static final class QuestionEntry implements BaseColumns{
        //Truy vấn đến bảng Question, mã code trả về 7
        public static final String PATH_QUESTION="question";
        public static final int CODE_QUESTION =7;

        //Truy vấn đến cột Question, mã code trả về 8
        public static final String PATH_QUESTION_ID="question/#";
        public static final int CODE_QUESTION_ID =8;

        //Uri truy vấn đến bảng Question: content://com.example.android.quiz/question
        public static final Uri CONTENT_URI_QUESTION=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_QUESTION);

        public static final String TABLE_NAME="question";
        public static final String COLUMN_QUESTION_ID ="questionID";//primary key auto increment
        public static final String COLUMN_QUESTION_QUESTION ="question"; //not null
        public static final String COLUMN_QUESTION_OPTION1 ="option1";//not null
        public static final String COLUMN_QUESTION_OPTION2 ="option2";//not null
        public static final String COLUMN_QUESTION_OPTION3 ="option3";//not null
        public static final String COLUMN_QUESTION_OPTION4 = "option4";//not null
        public static final String COLUMN_QUESTION_ANSWER ="answer"; //not null convention: option1, option2, option3, option4
        public static final String COLUMN_QUESTION_DIFFICULTY ="difficulty"; //allow null
        public static final String COLUMN_QUIZ_REFERENCE = "quizID"; //allow null to tạo ngân hàng đề

    }
}
