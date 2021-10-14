package com.example.sqlite_example;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sqlite_example.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static final int MENU_ITEM_VIEW =111;
    static final int MENU_ITEM_EDIT = 222;
    static final int MENU_ITEM_CREATE=333;
    static final int MENU_TTEM_DELETE =444;
    static final int MY_REQUEST_CODE =1000;

    final List<Note> noteList = new ArrayList<>();
    ArrayAdapter<Note> listViewApdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get listview object from xml
        this.listView = findViewById(R.id.listView);

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.CreateDefaultNoteIfNeed();

        List<Note> list= db.getAllNotes();
        this.noteList.addAll(list);
        //
        this.listViewApdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, this.noteList);

        // assign adapter to listview
        this.listView.setAdapter(this.listViewApdapter);

        // register the listview for context nemu
        registerForContextMenu(this.listView);
    }
    ////////////////////////////
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        menu.setHeaderTitle("Select the Action");
        // groupID, itemID, order, title
        menu.add(0,MENU_ITEM_VIEW,0,"View Note");
        menu.add(0,MENU_ITEM_CREATE,1,"Create Note");
        menu.add(0,MENU_ITEM_EDIT, 2, "Edit Note");
        menu.add(0,MENU_TTEM_DELETE,3,"Delete Note");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Note selectedNote = (Note) this.listView.getItemAtPosition(info.position);
        if (item.getItemId()==MENU_ITEM_VIEW){
            Toast.makeText(getApplicationContext(),selectedNote.noteContent,Toast.LENGTH_LONG).show();
        }
        else if (item.getItemId()==MENU_ITEM_CREATE){
            Intent intent = new Intent(this,AddEditNoteActivity.class);
            //
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId()==MENU_ITEM_EDIT){
            Intent intent = new Intent(this, AddEditNoteActivity.class);
            intent.putExtra("note",selectedNote);
            //
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId()==MENU_TTEM_DELETE){
            new AlertDialog.Builder(this)
                    .setMessage(selectedNote.noteTitle+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> deleteNote(selectedNote))
                    .setNegativeButton("No",null)
                    .show();
        }
        else{
            return false;
        }
        return true;
    }

    // deleteNote
    private void deleteNote(Note note){
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deleteNote(note);
        // refresh listview
        this.listViewApdapter.notifyDataSetChanged();
    }

    // when AddEditNoteActivity completed, it sends feedback
    // (if you start it using startActivityForResult())
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode== Activity.RESULT_OK && requestCode==MY_REQUEST_CODE){
            boolean needRefresh = data.getBooleanExtra("needFresh",true);
            //refresh listview
            if(needRefresh){
                this.noteList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Note> list = db.getAllNotes();
                this.noteList.addAll(list);

                //
                this.listViewApdapter.notifyDataSetChanged();
            }
        }
    }


}