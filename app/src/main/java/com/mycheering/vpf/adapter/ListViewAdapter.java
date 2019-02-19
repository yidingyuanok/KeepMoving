package com.mycheering.vpf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mycheering.vpf.R;

import java.util.List;

/**
 * Created by zdyok on 2016/11/17.
 */
public class ListViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<String> date;

    public ListViewAdapter(Context context, List<String> date) {
         mContext = context;
        this.date = date;
    }

    @Override
    public int getCount() {
        return date == null ? 0 : date.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.ll_text, viewGroup, false);
        }
        TextView tv = (TextView) view.findViewById(R.id.tv);
        tv.setText(date.get(i));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击text", Toast.LENGTH_SHORT).show();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击条目", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
