package com.example.notes;

public class firebasemodel {


    private String title;
    private String content; // must be matched with create note string name


    public firebasemodel(){

    }

    public firebasemodel(String title, String content){
        this.title = title;
        this.content = content;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
