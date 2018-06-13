package com.hfad.mymp3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicListItemAdapter extends BaseAdapter {

    Context context;
    ListItem[] items;

    MyOnItemClickListener myOnItemClickListener;

    interface MyOnItemClickListener {
        public void onItemClick(final ListItem item);
    }

    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout view;
        if(convertView != null){
            view = (LinearLayout)convertView;
        }
        else{
            LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context);
            view = (LinearLayout) inflater.inflate(R.layout.listview, parent, false);
        }

        view.setOnClickListener(new View.OnClickListener() {            // 임의의 클릭 리스너를 만들고 뷰를 눌렀을 때 그 리스너가 실행되도록합니다.
            @Override
            public void onClick(View v) {
                if(myOnItemClickListener !=null){
                    myOnItemClickListener.onItemClick(items[position]);
                }
            }
        });

        TextView titleView = (TextView)view.findViewById(R.id.listname);
        ImageView imageView = (ImageView)view.findViewById(R.id.item_image);

        titleView.setText(items[position].getName());
        imageView.setImageResource(items[position].getResId());

        return view;
    }
}
