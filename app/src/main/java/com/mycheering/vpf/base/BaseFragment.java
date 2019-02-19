package com.mycheering.vpf.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycheering.vpf.holder.MutilRVAdapter;
import com.mycheering.vpf.bean.BaseEntity;

import java.util.List;

/**
 * Created by zdyok on 2016/11/16.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment {


   protected RecyclerView mRecyclerView ;
    protected List<BaseEntity> mDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("zdy  BaseFragment.onCreate  " + this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("zdy  BaseFragment.onCreateView  " + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("zdy  BaseFragment.onActivityCreated  " + this.getClass().getSimpleName());

        mDate = getDate();


        setRecyclerView();

    }

    protected void setRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

            mRecyclerView.setAdapter(new MutilRVAdapter(getActivity().getApplicationContext(),this, mDate));
        }
    }

    protected abstract List<BaseEntity> getDate() ;

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("zdy  BaseFragment.onResume  " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("zdy  BaseFragment.onPause  " + this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("zdy  BaseFragment.onStop  " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("zdy  BaseFragment.onDestroyView  " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("zdy  BaseFragment.onDestroy  " + this.getClass().getSimpleName());
    }

}
