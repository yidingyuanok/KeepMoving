package com.mycheering.vpf.framement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycheering.vpf.R;
import com.mycheering.vpf.holder.MutilRVAdapter;
import com.mycheering.vpf.base.BaseFragment;
import com.mycheering.vpf.bean.BaseEntity;
import com.mycheering.vpf.divider.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PageFragmentC extends BaseFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.recyclerview, container,false);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
		return view;
	}

	@Override
	protected void setRecyclerView() {
		if (mRecyclerView != null) {
			mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
			mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
			mRecyclerView.setAdapter(new MutilRVAdapter(getActivity().getApplicationContext(), this, mDate));
		}
	}

	@Override
	protected List<BaseEntity> getDate() {
		List<BaseEntity> list = new ArrayList<>();
		for (int i = 0; i < 33; i++) {
			BaseEntity entity = new BaseEntity();
			entity.name="One C "+ i ;
			list.add(entity);
		}
		return list;
	}
	
}
