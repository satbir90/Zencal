package com.zeninno.zencal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeninno.zencal.Models.EventModel;
import com.zeninno.zencal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Satbir on 27-Jul-17.
 */

public class EventAdapter extends BaseAdapter {
    Context context;
    List<EventModel> model = new ArrayList<>();
    List<EventModel> filteredModel = new ArrayList<>();
    View view;
    public EventAdapter(Context context, List<EventModel> model){
        //super(context, id, model);
        this.context = context;
        this.model = model;
        this.filteredModel = new ArrayList<>();
        this.filteredModel.addAll(this.model);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        EventModel event = model.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_view_event_item, null);

        TextView eventNameTV = (TextView) view.findViewById(R.id.eventNameTV);
        TextView eventDateTV = (TextView) view.findViewById(R.id.eventDateTV);
        TextView eventdateDiffTV = (TextView) view.findViewById(R.id.eventdateDiffTV);

        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        int diffInDays = (int) ((event.startDate.getTime() - new Date().getTime()) / (1000 * 60 * 60 * 24));

        eventNameTV.setText(event.title);
        eventDateTV.setText(format.format(event.startDate));
        eventdateDiffTV.setText(String.valueOf(diffInDays));

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        model.clear();
        if (charText.length() == 0) {
            model.addAll(filteredModel);
        } else {
            for (EventModel wp : filteredModel) {
                if (wp.title.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    model.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
