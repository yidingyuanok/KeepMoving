package com.mycheering.vpf.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mycheering.vpf.R;
import com.mycheering.vpf.adapter.NoDieVPAdapter;
import com.mycheering.vpf.base.BaseActivity;
import com.mycheering.vpf.framement.PageFragmentA;
import com.mycheering.vpf.framement.PageFragmentB;
import com.mycheering.vpf.framement.PageFragmentC;
import com.mycheering.vpf.framement.PageFragmentD;


public class RVActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager);


        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);

//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(new PageFragmentA());
//        fragments.add(new PageFragmentB());
//        fragments.add(new PageFragmentC());
//        fragments.add(new PageFragmentD());
//        mViewPager.setAdapter(new FragmentVPAdapter(getSupportFragmentManager(),fragments));

        NoDieVPAdapter noDieVPAdapter = new NoDieVPAdapter(getSupportFragmentManager(), this.getApplicationContext(), mViewPager);
        noDieVPAdapter.addItem(PageFragmentA.class ,null ,"one");
        noDieVPAdapter.addItem(PageFragmentB.class ,null ,"two");
        noDieVPAdapter.addItem(PageFragmentC.class ,null ,"three");
        noDieVPAdapter.addItem(PageFragmentD.class ,null ,"原来如此");

//        noDieVPAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(noDieVPAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


}
