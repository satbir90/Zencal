package com.zeninno.zencal.Fragments;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract.*;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.zeninno.zencal.Adapters.EventAdapter;
import com.zeninno.zencal.Models.EventModel;
import com.zeninno.zencal.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    View view;
    List<EventModel> model = new ArrayList<>();

    ListView eventsListView;
    EditText searchET;
    EventAdapter adapter;

    int backDays = -1;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        searchET = (EditText) view.findViewById(R.id.searchET);
        eventsListView = (ListView) view.findViewById(R.id.eventsListView);

        new eventsAsyncTask().execute();

    }

    private void initialize() throws SecurityException{

        String[] eventsProjection =
                new String[]{
                        Events._ID,
                        Events.TITLE,
                        Events.DESCRIPTION,
                        Events.DTSTART
                };

        Cursor eventCursor = getContext().getContentResolver().query(Events.CONTENT_URI,
                eventsProjection, Events.VISIBLE + " = 1", null, Events.DTSTART + " ASC");
        if(eventCursor.moveToFirst()){
            do {
                model.add(new EventModel(eventCursor.getString(0),
                        eventCursor.getString(1),eventCursor.getString(2),new Date(eventCursor.getLong(3))));
            } while(eventCursor.moveToNext());

        }

        removeolderEvents();
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = searchET.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }
        });

    }

    private class eventsAsyncTask extends AsyncTask<String, Void, String>{

        ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getContext());
            pd.setMessage("Loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            initialize();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pd.isShowing()) pd.hide();
            adapter = new EventAdapter(getContext(), model);
            eventsListView.setAdapter(adapter);

        }
    }

    private void removeolderEvents(){
        List<EventModel> listToRemove = new ArrayList<>();
        for (EventModel m : model) {

            int diffInDays = (int) ((m.startDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));

            if(diffInDays < backDays){
                listToRemove.add(m);
            }
        }

        for (EventModel m : listToRemove) {
            model.remove(m);
        }

    }
}
