package com.example.android.quiz.model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.quiz.R;
import com.example.android.quiz.activities.chapters.ChapterActivity;
import com.example.android.quiz.data.QuizContract;

import org.w3c.dom.Text;

import java.util.ArrayList;

//bind view gồm có Image (không được đẩy vào dữ liệu), Name và Description Subject
public class RecyclerViewAdapterSubject extends RecyclerView.Adapter<RecyclerViewAdapterSubject.ViewHolderSubject> {
    private Context context;
    private ArrayList<Subject> subjects;
    private Cursor cursor;
    private ItemClickListener mClickListener;

    //nhận cursor và chuyển cursor thành arraylist<subject>
    public RecyclerViewAdapterSubject(Context context, Cursor cursor){
        this.context=context;
        this.cursor= cursor;
        this.subjects=initialSubject();
    }

    public RecyclerViewAdapterSubject(Context context,ArrayList<Subject> subjects){
        this.context=context;
        this.subjects=subjects;
    }
    @NonNull
    @Override
    public ViewHolderSubject onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.cardview_item_subject,parent,false);
       return new ViewHolderSubject(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSubject holder, int position) {
        holder.subjectImage.setImageResource(subjects.get(position).getImage());
        holder.subjectName.setText(subjects.get(position).getName());
        holder.subjectDescription.setText(subjects.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    private ArrayList<Subject> initialSubject() {
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        while (cursor.moveToNext()) {
            int indexOfSubjectID = cursor.getColumnIndex(QuizContract.SubjectEntry.COLUMN_SUBJECT_ID);
            String subjectID = cursor.getString(indexOfSubjectID);
            int indexOfSubjectName = cursor.getColumnIndex(QuizContract.SubjectEntry.COLUMN_SUBJECT_NAME);
            String name = cursor.getString(indexOfSubjectName);
            int indexOfSubjectDescription = cursor.getColumnIndex(QuizContract.SubjectEntry.COLUMN_SUBJECT_DESCRIPTION);
            String description = cursor.getString(indexOfSubjectDescription);
            Subject tempSubject;
            switch (name) {
                case "JavaScript":
                    tempSubject = new Subject(subjectID ,name, description, R.drawable.javascript);
                    break;
                case "HTML":
                    tempSubject = new Subject(subjectID,name, description, R.drawable.html);
                    break;
                case "Angular":
                    tempSubject = new Subject(subjectID,name, description, R.drawable.angular);
                    break;
                case "Bootstrap":
                    tempSubject = new Subject(subjectID,name, description, R.drawable.bootstrap);
                    break;
                default:
                    tempSubject = new Subject(subjectID,name, description, R.drawable.javascript);
                    break;
            }
            subjects.add(tempSubject);
        }
        return subjects;
    }
    public String getSubjectID(int position) {
        return subjects.get(position).getSubjectID();
    }
    public String getSubjectName(int position) {
        return subjects.get(position).getName();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolderSubject extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ImageView subjectImage;
        TextView subjectName;
        TextView subjectDescription;
        private ItemClickListener itemClickListener;
        public ViewHolderSubject(@NonNull View itemView) {
            super(itemView);
            subjectImage=itemView.findViewById(R.id.subjectImage);
            subjectName=itemView.findViewById(R.id.subjectName);
            subjectDescription=itemView.findViewById(R.id.subjectDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


}
