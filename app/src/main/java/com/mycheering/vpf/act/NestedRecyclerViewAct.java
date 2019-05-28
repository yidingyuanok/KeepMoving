package com.mycheering.vpf.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.bean.BaseEntity;
import com.mycheering.vpf.holder.MutilRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class NestedRecyclerViewAct extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);


//        setContentView(R.activity_move.activity_nested_recyclerview);
//        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
//        setRecyclerView();

    }

    protected List<BaseEntity> getDate() {
        List<BaseEntity> list = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            if (i==3) {
                BaseEntity entity = new BaseEntity();
                for (int j = 0; j < 8; j++) {
                    entity.ideas.add("nested " + j);
                }
                list.add(entity);
                continue ;
            }
            BaseEntity entity = new BaseEntity();
            entity.name="OneKKK "+ i ;
            list.add(entity);

        }
        System.out.println("");
        return list;
    }

    protected void setRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            mRecyclerView.setAdapter(new MutilRVAdapter(this.getApplicationContext(), getDate()));
        }
    }

}
