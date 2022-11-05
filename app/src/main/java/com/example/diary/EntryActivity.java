package com.example.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class EntryActivity extends AppCompatActivity {
    // Layout variables
    EditText entrySubject, entryContent;
    TextView dateCreated, dateUpdated;
    // Internal variables
    boolean updating;
    DBHelper db;

    @Override
    @SuppressLint("Range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        // Initializing layout variables
        entrySubject = (EditText) findViewById(R.id.entrySubject);
        entryContent = (EditText) findViewById(R.id.entryContent);
        dateCreated = (TextView) findViewById(R.id.entryCreated);
        dateUpdated = (TextView) findViewById(R.id.entryUpdated);
        // Initializing internal variables
        updating = false;
        db = new DBHelper(EntryActivity.this);
        // Setting date textviews appropriately
        String date = new SimpleDateFormat("MM/dd/yy").format(new GregorianCalendar().getTime());
        dateUpdated.setText("Updated: " + date); dateCreated.setText("Created: " + date);
        // Determine if entry already exists in database, if so - then updating
        Cursor entry = db.getEntry(this.getIntent().getStringExtra("id"));
        if (entry.getCount() > 0 && entry.moveToFirst()) {
            // Fill out layout variables from existing entry to allow for updating
            entrySubject.setText(entry.getString(entry.getColumnIndex("SUBJECT")));
            entryContent.setText(entry.getString(entry.getColumnIndex("CONTENT")));
            dateUpdated.setText("Updated: " + entry.getString(entry.getColumnIndex("UPDATED")));
            dateCreated.setText("Created: " + entry.getString(entry.getColumnIndex("CREATED")));
            updating = true;
        }
    }

    // Override when the phone back button is pressed
    @Override
    public void onBackPressed() {
        backButton(this.getCurrentFocus());
    }

    // Function to handle when the back button is pressed
    @SuppressLint("Range")
    public void backButton(View view) {
        // If updating existing entry
        if (updating) {
            // Get existing entry information
            Cursor entry = db.getEntry(this.getIntent().getStringExtra("id"));
            if (entry.getCount() > 0 && entry.moveToFirst()) {
                String subject = entry.getString(entry.getColumnIndex("SUBJECT"));
                String content = entry.getString(entry.getColumnIndex("CONTENT"));
                // Determine if entry information was actually updated or not
                if (!subject.equals(entrySubject.getText().toString()) || !content.equals(entryContent.getText().toString())) {
                    // Determine if entry is missing either a subject or content
                    if (entrySubject.getText().toString().isEmpty() || entryContent.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Entry Needs Subject & Content", Toast.LENGTH_SHORT).show();
                    } else {
                        // Determine if entry was able to be updated or not
                        if (db.updateEntry(this.getIntent().getStringExtra("id"), entrySubject.getText().toString(), entryContent.getText().toString())) {
                            Toast.makeText(this, "Entry Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Unable to Update Entry", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } else { // Otherwise creating new entry
            // Determine if entry is missing either a subject or content
            if (entrySubject.getText().toString().isEmpty() || entryContent.getText().toString().isEmpty()) {
                Toast.makeText(this, "Entry Needs Subject & Content", Toast.LENGTH_SHORT).show();
            } else {
                // Determine if entry was able to be inserted or not
                if (db.createEntry(entrySubject.getText().toString(), entryContent.getText().toString())) {
                    Toast.makeText(this, "Entry Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to Create Entry", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // Return to diary activity
        startActivity(new Intent(EntryActivity.this, DiaryActivity.class));
        finish();
    }

    // Function to handle when delete button is pressed
    public void deleteEntry(View view) {
        Cursor entry = db.getEntry(this.getIntent().getStringExtra("id"));
        // Determine if entry exists in the database (or user is creating new entry)
        if (entry.getCount() > 0 && entry.moveToFirst()) {
            // Determine if entry was able to be deleted or not
            if (db.deleteEntry(this.getIntent().getStringExtra("id"))) {
                Toast.makeText(this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EntryActivity.this, DiaryActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Unable to Delete Entry", Toast.LENGTH_SHORT).show();
            }
        } else { // Otherwise just clear edit texts
            entrySubject.setText("");
            entryContent.setText("");
        }
    }
}
