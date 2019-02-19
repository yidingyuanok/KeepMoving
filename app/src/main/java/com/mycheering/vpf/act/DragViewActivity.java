package com.mycheering.vpf.act;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycheering.vpf.R;
import com.mycheering.vpf.view.DragView;

import java.util.Random;

/**
 * Created by zdy on 2017/1/17.
 */
public class DragViewActivity extends AppCompatActivity implements View.OnClickListener {

    private DragView dragView;
    private LinearLayout ll_parent;
    private TextView tv_big;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.dragview);

        dragView = (DragView) findViewById(R.id.tv_drag);
        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);


        WindowManager WindowManager = (android.view.WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
         WindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;

        Random random = new Random();
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) dragView.getLayoutParams();
//        layoutParams.leftMargin= random.nextInt(widthPixels);
//        layoutParams.topMargin = random.nextInt(heightPixels);
//        dragView.setLayoutParams(layoutParams);

        tv_big = (TextView) findViewById(R.id.tv_big);
//        tv_big.scrollBy(30,30);

        tv_big.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_big:
                tv_big.scrollBy(20,20); // textview里面的文字往 左上角 移动，说明scrollBy移动的是内容
                break;
        }
    }
}
