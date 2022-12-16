package com.example.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private RecyclerView recyclerView;
    private final List<Note> noteList = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private static final int Edit_REQ = 1;
    private final String filename = "database.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.myrecycler);
        noteAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new AsyncRead().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menuCreate:
                intent = new Intent(this, EditActivity.class);
                Note note = new Note(noteList.size(), "", "", "");
                intent.putExtra("note", note);
                startActivityForResult(intent, Edit_REQ);
                return true;
            case R.id.menuInfo:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Edit_REQ) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra("note");
                Log.d("noteStringMain", "" + note.getID() + note.getTitle() + note.getContent() + note.getDate());

                if (note.getID() < noteList.size()) {
                    int i = 0;
                    for (Iterator iter = noteList.iterator(); iter.hasNext(); ) {
                        Note tmp = (Note) iter.next();
                        if (tmp.getID() == note.getID()) break;
                        i++;
                    }
                    noteList.remove(i);
                }
                noteList.add(0, note);
                noteAdapter.notifyDataSetChanged();

            } else {
                Log.d("Main", "onActivityResult: result Code: " + resultCode);
            }
        } else {
            Log.d("Main", "onActivityResult: request Code: " + requestCode);
        }
    }

    @Override
    public boolean onLongClick(final View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteList.remove(pos);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Deleted note", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Note note = noteList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, Edit_REQ);
        Log.d("onclick", "123");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            FileOutputStream fos = openFileOutput(filename, 0);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("    ");
            writer.beginArray();
            for(Iterator iter = noteList.iterator(); iter.hasNext();){
                Note note = (Note) iter.next();
                writer.beginObject();
                writer.name("id").value(note.getID());
                writer.name("title").value(note.getTitle());
                writer.name("date").value(note.getDate());
                writer.name("content").value(note.getContent());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class AsyncRead extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileInputStream fis = openFileInput(filename);
                JsonReader reader = new JsonReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
                reader.beginArray();
                while(reader.hasNext()){
                    reader.beginObject();
                    String title="";
                    String content="";
                    String date="";
                    int id=0;
                    while(reader.hasNext()){
                        String name = reader.nextName();
                        if (name.equals("id")){
                            id = reader.nextInt();
                        }else if(name.equals("title")){
                            title = reader.nextString();
                        }else if (name.equals("content")){
                            content = reader.nextString();
                        }else if (name.equals("date")){
                            date = reader.nextString();
                        }else{
                            reader.skipValue();
                        }
                    }
                    Note note = new Note(id,date,title,content);
                    noteList.add(note);
                    reader.endObject();
                }
                reader.endArray();
                reader.close();
                noteAdapter.notifyDataSetChanged();

            } catch (FileNotFoundException e) {
                try {
                    FileOutputStream fos = openFileOutput(filename,0);
                    fos.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
