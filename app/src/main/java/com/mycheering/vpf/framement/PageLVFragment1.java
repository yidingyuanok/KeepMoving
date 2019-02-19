package com.mycheering.vpf.framement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mycheering.vpf.R;
import com.mycheering.vpf.base.BaseLVFragment;

import java.util.ArrayList;
import java.util.List;

public class PageLVFragment1 extends BaseLVFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.listview, container,false);
		mListView = (ListView) view.findViewById(R.id.lv);
		
		return view;
	}



	@Override
	protected List<String> getDate() {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 33; i++) {
			list.add("lv_One "+ i);
		}
		return list;
	}

}
