package com.mycheering.vpf.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mycheering.vpf.R;
import com.mycheering.vpf.bean.BaseEntity;

/**
 * Created by zdy on 2017/1/17.
 */

public class NestedHolder<T> extends BaseHolder<T> {

    public static final int LISTVIEW_VERTICAL = 0;
    public static final int LISTVIEW_HORIZONTAL = 1;
    public static final int GRADVIEW_VERTICAL = 2;
    public static final int GRADVIEW_HORIZONTAL = 3;
    private final int mDefaultSpanCount = 2 * 3 * 4 * 5 * 6;
    RecyclerView mRecyclerView;
    private int mType;
    private int mBlockCount;

    public NestedHolder(View itemView) {
        super(itemView);
    }

    public NestedHolder(View itemView ,int type ) {
        super(itemView);
        mType = type;
        mBlockCount = mDefaultSpanCount;

    }
    public NestedHolder(View itemView ,int type ,int blockCount ) {
        super(itemView);
        mType = type;
        mBlockCount = blockCount;

    }

    @Override
    void initViewHolderView(View itemView) {
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv);

    }

    @Override
 public  void bindViewHolderDate(BaseHolder holder, int position, T entity) {

//        mRecyclerView.getLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
//        mRecyclerView.getLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));

//        mRecyclerView.getLayoutManager(new GridLayoutManager(mContext,2,GridLayoutManager.HORIZONTAL,false));
        mRecyclerView.setLayoutManager(getLayoutManager(mType,mBlockCount));
        configSpanCount();
        mRecyclerView.setAdapter(new MutilRVAdapter(itemView.getContext(), ((BaseEntity) entity).ideas));

    }


    public RecyclerView.LayoutManager getLayoutManager(int type , int spanCount) {
        RecyclerView.LayoutManager layoutManager = null;
        switch (type) {
            case LISTVIEW_VERTICAL:
                layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                break;
            case LISTVIEW_HORIZONTAL:
                layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                break;
            case GRADVIEW_VERTICAL:
                layoutManager = new GridLayoutManager(mContext, spanCount, GridLayoutManager.VERTICAL, false);
                break;
            case GRADVIEW_HORIZONTAL:
                layoutManager = new GridLayoutManager(mContext, spanCount, GridLayoutManager.HORIZONTAL, false);
                break;
        }

        return layoutManager;
    }

    public void configSpanCount(){
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() instanceof GridLayoutManager) {
            ((GridLayoutManager) mRecyclerView.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
//                    if (position == 3) {
//                        return mDefaultSpanCount / (2 * 3 * 5 * 6);
//                    }
//                    return ((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount();
                    return mDefaultSpanCount / 4;
                }
            });
        }
    }

}
