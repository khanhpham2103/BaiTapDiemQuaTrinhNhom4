package com.example.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText contentText;
    private String title;
    private String content;
    private Note note;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        intent = getIntent();
        titleText = (EditText) findViewById(R.id.titleText);
        contentText = (EditText) findViewById(R.id.contentText);
        note = (Note) intent.getSerializableExtra("note");
        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSave:
                title = titleText.getText().toString();
                content = contentText.getText().toString();

                noTitle();

                note.setTitle(title);
                note.setContent(content);
                note.setDate(getTime());

                intent.putExtra("note", note);
                setResult(RESULT_OK, intent);
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (note.getContent().equals(contentText.getText().toString()) && note.getTitle().equals(titleText.getText().toString())) {
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Do you want to save?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = titleText.getText().toString();
                content = contentText.getText().toString();

                noTitle();

                note.setTitle(title);
                note.setContent(content);
                note.setDate(getTime());
                Log.d("noteString", "" + note.getID() + note.getTitle() + note.getContent() + note.getDate());
                intent.putExtra("note", note);
                setResult(RESULT_OK, intent);
                Toast.makeText(EditActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                EditActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        //super.onBackPressed();
    }

    public String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        return df.format(c.getTime());
    }

    public void noTitle() {
        title = titleText.getText().toString();
        if (title.length() == 0) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            CharSequence text = "Cannot save note without title";
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            finish();
        }
    }

}
