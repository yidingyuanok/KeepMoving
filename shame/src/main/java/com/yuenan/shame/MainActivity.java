package com.yuenan.shame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yuenan.shame.view.PercentProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progress)
    PercentProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progress.setPercentProgress(45);
    }
}
