package com.hfad.mymp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicListItemAdapter extends BaseAdapter {

    Context context;
    ListItem[] items;

    public MusicListItemAdapter(Context context , ListItem[] listItem) {
        this.context = context;
        this.items = listItem;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null){
            view = convertView;
        }
        else{
            LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
            view = (View)inflater.inflate(R.layout.listview, parent, false);
        }

        TextView titleView = (TextView)view.findViewById(R.id.listname);
        ImageView imageView = (ImageView)view.findViewById(R.id.item_image);

        titleView.setText(items[position].getName());
        imageView.setImageResource(items[position].getResId());

        return view;
    }
}
