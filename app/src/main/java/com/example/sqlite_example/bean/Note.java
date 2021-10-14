package com.example.sqlite_example.bean;

import java.io.Serializable;

public class Note implements Serializable {
    public int noteId;
    public String noteTitle;
    public String noteContent;

    public Note(){

    }
    public Note(String noteTitle, String noteContent){
        this.noteContent = noteContent;
        this.noteTitle = noteTitle;
    }

    public Note(int notId,String noteTitle, String noteContent){
        this.noteContent = noteContent;
        this.noteTitle = noteTitle;
        this.noteId = notId;
    }

    @Override
    public String toString(){
        return this.noteTitle;
    }
}
