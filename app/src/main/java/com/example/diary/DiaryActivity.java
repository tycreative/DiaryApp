package com.example.diary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DiaryActivity extends AppCompatActivity {
    // Layout variables
    ListView entryList;
    EditText search;
    TextView today;
    // Internal variables
    ArrayList<String[]> entries;
    DBHelper db;

    @Override
    @SuppressLint("Range")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        // Initialize layout variables
        entryList = (ListView) findViewById(R.id.entryList);
        search = (EditText) findViewById(R.id.entrySearch);
        today = (TextView) findViewById(R.id.today);

        // Reset today textview to display today's date
        today.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        today.setText(new SimpleDateFormat("EEEE\nMMMM dd, yyyy").format(new GregorianCalendar().getTime()));

        // Grab database and list the entries
        db = new DBHelper(DiaryActivity.this);
        listEntries(db.getEntries());

        // Check for when an entry is selected
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
                // Call entry activity on specific entry selected
                startActivity(new Intent(DiaryActivity.this, EntryActivity.class).putExtra("id", entries.get(index)[0]));
                finish();
            }
        });

        // Check for when search bar is changed
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Hide away today textview
                today.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                if (search.getText().toString().equals("")) {
                    // Reset view if search bar is empty
                    listEntries(db.getEntries());
                    today.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                } else {
                    // Otherwise display found results
                    listEntries(db.findEntry(search.getText().toString()));
                }
            }

            @Override public void afterTextChanged(Editable editable) { }
        });
    }

    // Function to display the entries given in the array
    public void listEntries(ArrayList<String[]> array) {
        entryList.setAdapter(new EntryAdapter(DiaryActivity.this, entries = array));
    }

    // Function to create new entry by switching to entry activity
    public void createEntry(View view) {
        startActivity(new Intent(DiaryActivity.this, EntryActivity.class));
        finish();
    }
}
