package com.mycheering.vpf.holder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mycheering.vpf.R;
import com.mycheering.vpf.bean.BaseEntity;

/**
 * Created by zdy on 2017/1/17.
 */

public class TextViewHolder<T> extends BaseHolder<T> {

    TextView tv;

    public TextViewHolder(View itemView) {
        super(itemView);
    }


    @Override
    protected void initViewHolderView(View itemView) {
        tv = (TextView) itemView.findViewById(R.id.tv);
    }

    @Override
    public void bindViewHolderDate(BaseHolder holder, int position, T entity) {

        if (entity instanceof String) {
            tv.setText(((String) entity));
        } else if (entity instanceof BaseEntity){
            tv.setText(((BaseEntity) entity).name);
        }


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
}