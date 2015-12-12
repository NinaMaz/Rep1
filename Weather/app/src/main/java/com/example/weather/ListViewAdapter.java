package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public ListViewAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list, parent, false);
        TextView data = (TextView) rowView.findViewById(R.id.datatext);
        TextView textTemp = (TextView) rowView.findViewById(R.id.textfortemp);
        String s = values[position];

        data.setText(values[position].substring(0,2));
        textTemp.setText(values[position].substring(6,values[position].length()));
        // change the icon for Windows and iPhone


        return rowView;
    }
}

