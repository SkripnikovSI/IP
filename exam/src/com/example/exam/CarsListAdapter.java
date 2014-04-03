package com.example.exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CarsListAdapter extends ArrayAdapter<Car> {

    public CarsListAdapter(Context context, Car[] cars) {
        super(context, R.layout.item_car, cars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item;
        if (convertView == null) {
            item = ((LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_car, parent, false);
        } else {
            item = convertView;
        }
        ((TextView) item.findViewById(R.id.text_view_model)).setText(getItem(position).model);
        ((TextView) item.findViewById(R.id.text_view_color)).setText(getItem(position).color);
        String g = Integer.toString(8 + getItem(position).time / 2);
        if (getItem(position).time % 2 == 0)
            ((TextView) item.findViewById(R.id.text_view_time)).setText(g + " : 00");
        else
        ((TextView) item.findViewById(R.id.text_view_time)).setText(g + " : 30");
        ((TextView) item.findViewById(R.id.text_view_box)).setText(Integer.toString(getItem(position).box));
        return item;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}