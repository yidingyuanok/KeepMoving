package com.mycheering.vpf.act;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycheering.vpf.R;
import com.mycheering.vpf.adapter.MyRVAdapter;
import com.mycheering.vpf.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zdyok on 2016/11/17.
 */
public class RVHeaderActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayout llHead;

    int scrollDex;
    private Drawable llHeadBackground;
    private TextView tvMove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_header);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new MyRVAdapter(this.getApplicationContext(), getData()));
        mRecyclerView.addOnScrollListener(new RVScrollListener());

        tvMove = (TextView) findViewById(R.id.tv_popup);
        llHead = (LinearLayout) findViewById(R.id.ll_head);

        llHeadBackground = llHead.getBackground();
        llHeadBackground.setAlpha(0);

        findViewById(R.id.tv1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                down();
                 actionAnimation(RVHeaderActivity.this,tvMove,true);
            }
        });

        findViewById(R.id.tv2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                up();
                actionAnimation(RVHeaderActivity.this,tvMove,false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

       private void down() {
        int height = tvMove.getHeight();
        int margin = dp2px(RVHeaderActivity.this, 80);

        int bottom = tvMove.getBottom();
        int total = height + bottom ;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, total);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(1000);

        tvMove.startAnimation(translateAnimation);
    }

    private void up() {
        int height = tvMove.getHeight();
        int margin = dp2px(RVHeaderActivity.this, 80);

        int bottom = tvMove.getBottom();
        int total = height + bottom ;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, total ,0 );
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(1000);

        tvMove.startAnimation(translateAnimation);
    }

    private boolean isAnimationRunning;
    private boolean isShow = true;
    private float lastMoveDex;
    private void actionAnimation(Context context , View view , final boolean isDown) {
//        int height = tvMove.getHeight();
//        int bottom = tvMove.getBottom();
//        total = height + bottom ;

        float y = view.getY();
        float total = context.getResources().getDisplayMetrics().heightPixels - y;

        float startY, endY ;

        if (isDown){
            startY = 0;
            endY = total;
            lastMoveDex = total;
        }else {
            startY = y ;
            endY = 0 ;
        }

        System.out.println( startY);
        System.out.println( endY);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", startY, endY );
        objectAnimator.setDuration(500);
        objectAnimator.setInterpolator(new LinearInterpolator());


         objectAnimator.addListener(new Animator.AnimatorListener() {
             @Override
             public void onAnimationStart(Animator animator) {
                 isAnimationRunning = true;
             }

             @Override
             public void onAnimationEnd(Animator animator) {
                 isAnimationRunning = false;
                 isShow = !isDown;
             }

             @Override
             public void onAnimationCancel(Animator animator) {
                 isAnimationRunning = false;
             }

             @Override
             public void onAnimationRepeat(Animator animator) {

             }
         });

        objectAnimator.start();

    }





    private List<String> getData() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i < 99; i++) {
            strings.add("   scroll " + i);
        }
        return strings;
    }

//    public static final int SCROLL_STATE_IDLE = 0;
//    public static final int SCROLL_STATE_DRAGGING = 1;

//    The RecyclerView is currently animating to a final position while not under outside control.
//    public static final int SCROLL_STATE_SETTLING = 2;

    class RVScrollListener extends RecyclerView.OnScrollListener {




        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            System.out.println("zdy RVScrollListener.onScrollStateChanged **  " + newState);
        }

        // dy 表示单位时间内滑动的位移 向下滑为正
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            scrollDex += dy;

            setHeaderAlpha(scrollDex , dy);

            if (isAnimationRunning){
                return;
            }

            if (dy > 5) { // 向上滑， 圆图向下走
                if (isShow) {
                actionAnimation(RVHeaderActivity.this,tvMove,true);
                }
            } else if (dy < -5) {
                if (!isShow) {
                actionAnimation(RVHeaderActivity.this,tvMove,false);
                }
            }
        }
    }

    private int lastAlpha;
    private void setHeaderAlpha(int scrollDex , int dy) {
        scrollDex -= 300 ;
        scrollDex =  (scrollDex < 0 ) ? 0 : scrollDex;

        float percent = new Float(scrollDex) / 400;
      int  alpha = (int) (percent * 255);
        if (alpha >= 255) {
            alpha = 255;
        }
        if (alpha != lastAlpha) {
            llHeadBackground.setAlpha(alpha);
            lastAlpha = alpha;
        }
        System.out.println("zdy recyclerView scrollDex = [ " + scrollDex + " ], percent = [" + percent + "], dy = [" + dy + "]   alpha  = " + alpha);
    }

    public int dp2px(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

}
