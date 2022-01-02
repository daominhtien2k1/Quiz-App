package com.example.android.quiz.model;

public class Subject {
    private String subjectID;
    private String name;
    private String description;
    private String createAt;
    private String modifyAt;
    private int image;

    public Subject(String subjectID, String name, String description) {
        this.subjectID = subjectID;
        this.name = name;
        this.description = description;
    }

    public Subject(String subjectID, String name, String description, int image){
        this.subjectID=subjectID;
        this.name=name;
        this.description=description;
        this.image=image;
    }
    public Subject(String subjectID, String name, String description, String createAt, String modifyAt){
        this.subjectID=subjectID;
        this.name=name;
        this.description = description;
        this.createAt=createAt;
        this.modifyAt=modifyAt;
    }
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(String modifyAt) {
        this.modifyAt = modifyAt;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
