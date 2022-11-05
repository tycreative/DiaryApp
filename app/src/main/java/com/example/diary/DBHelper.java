package com.example.diary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

public class DBHelper extends SQLiteOpenHelper {

    // Database helper constructor
    public DBHelper(Context context) {
        super(context, "diary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create database table for diary entries
        db.execSQL("CREATE TABLE DIARY(ID INTEGER PRIMARY KEY AUTOINCREMENT, SUBJECT TEXT, CONTENT TEXT, CREATED TEXT, UPDATED TEXT, TIME INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS DIARY");
    }

    // Function to get specified entry from database
    public Cursor getEntry(String id) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM DIARY WHERE ID IS " + id, null);
    }

    // Function to get all entries from database
    @SuppressLint("Range")
    public ArrayList<String[]> getEntries() {
        ArrayList<String[]> entries = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM DIARY ORDER BY TIME DESC, ID DESC", null); // Sorts by most recently updated
        // While cursor found something, add entry to arraylist
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("ID"));
            String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
            String updated = "Last Updated: " + cursor.getString(cursor.getColumnIndex("UPDATED"));
            entries.add(new String[] {id, subject, updated});
        }
        return entries;
    }

    // Function to find a specified entry
    @SuppressLint("Range")
    public ArrayList<String[]> findEntry(String keyword) {
        ArrayList<String[]> entries = new ArrayList<>();
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM DIARY WHERE SUBJECT LIKE '%" + keyword + "%' OR CONTENT LIKE '%" + keyword + "%' COLLATE NOCASE ORDER BY DATE(UPDATED) DESC, ID DESC", null);
        // While cursor found something, add entry to arraylist
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("ID"));
            String subject = cursor.getString(cursor.getColumnIndex("SUBJECT"));
            String updated = "Last Updated: " + cursor.getString(cursor.getColumnIndex("UPDATED"));

            // If keyword found in content, find and format area of content in which keyword was found
            String output = formatResult(cursor.getString(cursor.getColumnIndex("CONTENT")).replaceAll("\n+", " "), keyword);
            if (!output.isEmpty()) updated = output;
            entries.add(new String[] {id, subject, updated});
        }
        return entries;
    }

    // Helper function to format where keyword is found in content
    public String formatResult(String content, String keyword) {
        int index = content.toLowerCase().indexOf(keyword.toLowerCase());
        int start = 0, end = content.length();
        String output = "";
        // If keyword found in content
        if (index >= 0) {
            // Update start position of substring if matches formatting criteria
            if (index + 10 > end && index - 30 >= start) start = index - 30;
            else if (index - 20 >= start) start = index - 20;
            // Update end position of substring if matches formatting criteria
            if (index - 10 < start && index + 30 <= end) end = index + 30;
            else if (index + 20 <= end) end = index + 20;
            // Format output
            if (start > 0) output += "...";
            output += content.substring(start, end).trim();
            if (end < content.length()) output += "...";
        }
        return output;
    }

    // Function to add a new entry to the database
    public boolean createEntry(String subject, String content) {
        String date = new SimpleDateFormat("MM/dd/yy").format(new GregorianCalendar().getTime());
        ContentValues cv = new ContentValues();
        // Insert values into database table
        cv.put("SUBJECT", subject);
        cv.put("CONTENT", content);
        cv.put("CREATED", date);
        cv.put("UPDATED", date);
        cv.put("TIME", System.currentTimeMillis());
        return this.getWritableDatabase().insert("DIARY", null, cv) >= 0;
    }

    // Function to update an existing entry in the database
    public boolean updateEntry(String id, String subject, String content) {
        ContentValues cv = new ContentValues();
        // Insert values into database table
        cv.put("SUBJECT", subject);
        cv.put("CONTENT", content);
        cv.put("UPDATED", new SimpleDateFormat("MM/dd/yy").format(new GregorianCalendar().getTime()));
        cv.put("TIME", System.currentTimeMillis());
        return this.getWritableDatabase().update("DIARY", cv, "ID = ?", new String[] {id}) >= 0;
    }

    // Function to delete an entry from the database
    public boolean deleteEntry(String id) {
        return this.getWritableDatabase().delete("DIARY", "ID = ?", new String[] {id}) >= 0;
    }
}
