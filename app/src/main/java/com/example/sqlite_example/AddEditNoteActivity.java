package com.example.sqlite_example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sqlite_example.bean.Note;

public class AddEditNoteActivity extends AppCompatActivity {
    static final  int MODE_CREATE =1;
    static final  int MODE_EDIT =2;

    EditText textTitle,textContent;
    Button buttonSave, buttonCancel;

    Note note;
    boolean needRefresh;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        this.textTitle = this.findViewById(R.id.editText_note_title);
        this.textContent = this.findViewById(R.id.editText_note_content);

        this.buttonSave = this.findViewById(R.id.Save);
        this.buttonCancel = this.findViewById(R.id.Cancel);

        // click save
        this.buttonSave.setOnClickListener(v -> buttonSaveClicked());

        // click cancel
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClicked();
            }
        });

        // intent
        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");

        if (note==null){
            this.mode = MODE_CREATE;
        }else {
            this.mode = MODE_EDIT;
            this.textTitle.setText(note.noteTitle);
            this.textContent.setText(note.noteContent);
        }
    }

    //
    public void buttonSaveClicked(){
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        String title = this.textTitle.getText().toString();
        String content = this.textContent.getText().toString();
        if (title.equals("")||content.equals("")){
            Toast.makeText(getApplicationContext(),"Please enter title and content",Toast.LENGTH_LONG).show();
            return;
        }
        if(mode == MODE_CREATE){
            this.note = new Note(title,content);
            db.addNote(note);
        }
        else {
            this.note.noteContent.toString();
            db.updateNote(note);
        }
        this.needRefresh = true;
        //
        this.onBackPressed();
    }
    //
    public void buttonCancelClicked(){
       // do nothing, back MainActivity
       this.onBackPressed();
    }
    //
    @Override
    public void finish(){
        Intent data = new Intent();
        //
        data.putExtra("needRefresh", needRefresh);
        //
        this.setResult(Activity.RESULT_OK,data);
        super.finish();
    }
}
