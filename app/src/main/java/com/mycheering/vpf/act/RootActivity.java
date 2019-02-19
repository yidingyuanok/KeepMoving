package com.mycheering.vpf.act;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Turing on 2017/4/7.
 */

public class RootActivity extends ListActivity {

    private String [] mTitles = new String[] {

    };


    private Class[] mClazz = new Class[]{

    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,mTitles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        startActivity(new Intent(this, mClazz[position]));
    }
}
