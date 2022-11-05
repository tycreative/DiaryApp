package com.example.diary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// Custom adapter for handling how entries are displayed in list view
public class EntryAdapter extends BaseAdapter {
    // Internal variables
    Context context;
    ArrayList<String[]> entries;

    // Entry adapter constructor
    public EntryAdapter(Context context, ArrayList<String[]> entries) {
        this.context = context;
        this.entries = entries;
    }

    // Function to get number of entries
    @Override
    public int getCount() {
        return this.entries.size();
    }

    // Function to get a specific entry
    @Override
    public Object getItem(int i) {
        return this.entries.get(i);
    }

    // Function to get an entry id/position
    @Override
    public long getItemId(int i) {
        return i;
    }

    // Function to display each entry in item view
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Use custom item entry layout for displaying individual entries
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_entry, null);
        // Grab needed text views
        TextView entrySubject = (TextView) view.findViewById(R.id.entrySubject);
        TextView entryUpdated = (TextView) view.findViewById(R.id.entryUpdated);
        TextView entryID = (TextView) view.findViewById(R.id.entryID);
        //  Update text views appropriately
        entryID.setText(this.entries.get(i)[0]);
        entrySubject.setText(this.entries.get(i)[1]);
        entryUpdated.setText(this.entries.get(i)[2]);
        return view;
    }
}
