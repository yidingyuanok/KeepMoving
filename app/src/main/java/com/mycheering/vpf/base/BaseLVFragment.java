package com.mycheering.vpf.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mycheering.vpf.adapter.ListViewAdapter;

import java.util.List;

/**
 * Created by zdyok on 2016/11/16.
 */
public abstract class BaseLVFragment extends android.support.v4.app.Fragment {


   protected ListView mListView ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("zdy  BaseLVFragment.onCreate  " + this.getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("zdy  BaseLVFragment.onCreateView  " + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("zdy  BaseLVFragment.onActivityCreated  " + this.getClass().getSimpleName());

        setListView();

    }


    private void setListView() {
        if (mListView != null) {
            List<String> date = getDate();
            mListView.setAdapter(new ListViewAdapter(getActivity(),date));
        }
    }

    protected abstract List<String> getDate() ;

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("zdy  BaseLVFragment.onResume  " + this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("zdy  BaseLVFragment.onPause  " + this.getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("zdy  BaseLVFragment.onStop  " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("zdy  BaseLVFragment.onDestroyView  " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("zdy  BaseLVFragment.onDestroy  " + this.getClass().getSimpleName());
    }

}
