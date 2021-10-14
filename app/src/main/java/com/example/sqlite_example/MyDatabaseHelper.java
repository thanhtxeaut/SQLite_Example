package com.example.sqlite_example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sqlite_example.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG="SQLite";

    // database version
    private static final int DATABASE_VERSION =1;

    // Database name
    private static final String DATABASE_NAME ="Note_Manager";

    // Table name: Note
    private static final String TABLE_NOTE ="Note";

    // column name
    private static final String COLUMN_ID="Note_Id";
    private static final String COLUMN_TITLE="Note_Title";
    private static final String COLUMN_CONTENT="Note_Content";

    // Constructor
    public MyDatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    // Creat table
    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i(TAG,"MyDatabaseHelper.onCreate ...");
        // script
        String script = "CREATE TABLE " + TABLE_NOTE+"("
                +COLUMN_ID+" INTEGER PRIMARY KEY,"+
                COLUMN_TITLE+" TEXT,"+
                COLUMN_CONTENT+" TEXT"+")";
        // execSQL  Script
        db.execSQL(script);
    }

    // onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.i(TAG,"MyDatabaseHelper.onUpgrade ...");
        // drop older table of existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTE);
        // Create table again
        onCreate(db);
    }
    // If Note table has no data
    // default, insert two record
    public void CreateDefaultNoteIfNeed(){
        int count = this.getNoteCount();
        if (count==0)
        {
            Note note1 = new Note("Ghi chu dau tien","Noi dung dau tien");
            Note note2 = new Note("Note 2", " Content for note 2");
            this.addNote(note1);
            this.addNote(note2);
        }
    }

    // getnotecount
    public int getNoteCount(){
        Log.i(TAG, "MyDatabaseHelper.getNoteCount...");
        String countQuery ="SELECT * FROM "+TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery,null);
        int count = cursor.getCount();


        return count;
    }
    // addNote
    public void addNote(Note note){
        Log.i(TAG, "MyDatabaseHelper.addNote...");
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,note.noteTitle);
        values.put(COLUMN_CONTENT,note.noteContent);
        // inserting row
        db.insert(TABLE_NOTE,null,values);
        // close
        db.close();
    }

    // Get Note from data base
    public Note getNote(int id){
        Log.i(TAG, "MyDatabaseHelper.addNote..."+id);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =db.query(TABLE_NOTE,new String[]
                {
                  COLUMN_ID,COLUMN_TITLE,COLUMN_CONTENT
                },COLUMN_ID+"=?",
                new String[]{
                        String.valueOf(id)
                },null,null,null,null
                );

        if (cursor!=null)
            cursor.moveToFirst();

        Note note = new Note (
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2)
                );


        return note;
    }
    // get allNotes
    public List<Note> getAllNotes(){
        Log.i(TAG, "MyDatabaseHelper.getAllNotes...");
        
        List<Note> noteList = new ArrayList<Note>();
        
        // slect all query
        String selectQuery =" SELECT *FROM "+TABLE_NOTE;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        
        // looping through all row adding to list
        if (cursor.moveToFirst()){
            do{
               Note note = new Note();
               note.noteId = Integer.parseInt(cursor.getString(0));
               note.noteTitle = cursor.getString(1);
               note.noteContent = cursor.getString(2);
               // add note to list
                noteList.add(note);
            }while (cursor.moveToNext());
        }
        //
        return noteList;
    }

    // update note
    public int updateNote(Note note){
        Log.i(TAG, "MyDatabaseHelper.updateNote...");
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,note.noteTitle);
        values.put(COLUMN_CONTENT,note.noteContent);
        // update row
        return db.update(TABLE_NOTE,values,COLUMN_ID +" =?",
                new String[] {String.valueOf(note.noteId)});
    }
    // delete
    public void deleteNote(Note note){
        Log.i(TAG, "MyDatabaseHelper.deleteNote..."+note.noteTitle);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_ID+" =?",
                new String[] {String.valueOf(note.noteId)});
        db.close();
    }

}
