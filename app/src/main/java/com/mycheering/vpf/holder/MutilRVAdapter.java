package com.mycheering.vpf.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseFragment;
import com.mycheering.vpf.framement.PageFragmentA;

import java.util.List;

/**
 * Created by zdyok on 2016/11/16.
 */
public class MutilRVAdapter<T> extends RecyclerView.Adapter {
    private static final int TYPE_NESTED = 0;
    private static final int TYPE_NORMAL = 1;
    private final Context mContext;
    private  BaseFragment mFragment;
    List<T> mDate;
    LayoutInflater mLayoutInflater;

    public MutilRVAdapter(Context context, BaseFragment fragment, List<T> date) {
        mDate = date;
        mLayoutInflater = LayoutInflater.from(context);
        mFragment = fragment;
        mContext = context;
    }

    public MutilRVAdapter(Context context,  List<T> date) {
        mDate = date;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mFragment instanceof PageFragmentA) {
            switch (viewType) {
                case TYPE_NORMAL:
                    return new TextViewHolder(mLayoutInflater.inflate(R.layout.ll_text, parent, false));
                case TYPE_NESTED:
                    View nestedView = mLayoutInflater.inflate(R.layout.recyclerview1, parent, false);
//                    return new NestedHolder(nestedView,NestedHolder.GRADVIEW_VERTICAL,3);
                    return new NestedHolder(nestedView, NestedHolder.GRADVIEW_VERTICAL);
            }
        }else{
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.ll_text, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        BaseHolder baseViewHolder = (BaseHolder) holder;
        baseViewHolder.bindViewHolderDate(baseViewHolder, position, mDate.get(position));

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 3:
                return TYPE_NESTED;
            default:
                return TYPE_NORMAL;
        }

    }

    @Override
    public int getItemCount() {
        return mDate == null ? 0 : mDate.size();
    }


}
