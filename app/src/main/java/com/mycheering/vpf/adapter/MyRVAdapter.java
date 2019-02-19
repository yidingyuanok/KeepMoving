package com.mycheering.vpf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mycheering.vpf.R;

import java.util.List;

/**
 * Created by zdyok on 2016/11/16.
 */
public class MyRVAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    List<String> mDate;
    LayoutInflater mLayoutInflater;

    public MyRVAdapter(Context context , List<String> date) {
        mDate = date;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextViewHolder(mLayoutInflater.inflate(R.layout.ll_text,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        TextView tv = ((TextViewHolder) holder).tv;
        tv.setText(mDate.get(position));
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击text", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "点击条目", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDate == null ? 0 : mDate.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public TextViewHolder(View itemView) {
            super(itemView);
             tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
