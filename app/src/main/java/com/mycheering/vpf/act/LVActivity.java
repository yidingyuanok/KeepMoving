package com.mycheering.vpf.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mycheering.vpf.R;
import com.mycheering.vpf.adapter.FragmentVPAdapter;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.framement.PageLVFragment1;
import com.mycheering.vpf.framement.PageLVFragment2;
import com.mycheering.vpf.framement.PageLVFragment3;
import com.mycheering.vpf.framement.PageLVFragment4;

import java.util.ArrayList;
import java.util.List;

public class LVActivity extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new PageLVFragment1());
        fragments.add(new PageLVFragment2());
        fragments.add(new PageLVFragment3());
        fragments.add(new PageLVFragment4());
        mViewPager.setAdapter(new FragmentVPAdapter(getSupportFragmentManager(),fragments));

    }


}
