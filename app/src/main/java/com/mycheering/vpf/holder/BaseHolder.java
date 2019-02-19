package com.mycheering.vpf.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zdy on 2017/1/17.
 */

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {

    public Context mContext;

    public BaseHolder(View itemView) {
        super(itemView);
        initViewHolderView(itemView);
        mContext = itemView.getContext();
    }

    abstract void initViewHolderView(View itemView);

    public abstract void bindViewHolderDate(BaseHolder holder, int position, T entity);


}
