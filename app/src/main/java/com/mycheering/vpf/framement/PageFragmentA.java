package com.mycheering.vpf.framement;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseFragment;
import com.mycheering.vpf.bean.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class PageFragmentA extends BaseFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.recyclerview, container,false);
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);
//		mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
		return view;
	}


	@Override
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

}
