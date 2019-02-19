package com.mycheering.vpf.act;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.mycheering.vpf.R;

/**
 * Created by Turing on 2017/7/7.
 */

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_animation;
    private Context mContext;
    private TextView tv_popup;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);



    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animationview);
        mContext = this;

        tv_animation = (TextView) findViewById(R.id.tv_animation);
        tv_popup = (TextView) findViewById(R.id.tv_popup);

        findViewById(R.id.btn_animation).setOnClickListener(this);





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_animation:
//                setAnimation();
//                setAnimator();
//                setTvText();
//                tv_animation.scrollBy(-20, -20);


                setPopupAnimator();


                break;
        }
    }

    private void setPopupAnimator() {
        final int width = tv_popup.getWidth();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                ViewGroup.LayoutParams layoutParams = tv_popup.getLayoutParams();
                layoutParams.width = (int) (width * fraction + 0.5);
//                layoutParams.width = (int) (width * (1 - fraction) + 0.5);
                tv_popup.requestLayout();



            }
        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setAnimator() {
        setRotateAnimator(tv_animation);
        setAnimatorSet(tv_animation);
        setAnimatorset2(tv_animation);
        setValueAnimator(tv_animation);
        // 属性动画的简写形式
        tv_animation.animate().alpha(0).y(300).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                // TODO: 2017/7/11
            }
        }).start();
    }

    private void setAnimation() {
                setTranslateAnimation(tv_animation);
                setScaleAnimation(tv_animation);
                setAlphaAnimation(tv_animation);
                setRotateAnimation(tv_animation);
                Animation ta = AnimationUtils.loadAnimation(mContext, R.anim.translate_animation);
                tv_animation.startAnimation(ta);
    }

    private void setTvText() {
        new Thread(){
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper()); // 没有思考的复写是没有用的！！！
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_animation.setText("fuck uuu");
                    }
                });
//                        tv_animation.setText("fuck uuu");

            }
        }.start();
    }

    private void setValueAnimator(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setTarget(tv_animation);
        valueAnimator.setDuration(2000).start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                // TODO: 2017/7/11
            }
        });
    }

    private void setAnimatorset2(View view) {
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("rotation", 0,360);
        PropertyValuesHolder valuesHolder2 =PropertyValuesHolder.ofFloat("translationX", 0,200);
        PropertyValuesHolder valuesHolder3 =PropertyValuesHolder.ofFloat("translationY", 0,200);
        ObjectAnimator.ofPropertyValuesHolder(view, valuesHolder1,valuesHolder2,valuesHolder3).setDuration(2000).start();
    }

    private void setAnimatorSet( View view) {
        ObjectAnimator.ofFloat( view, "rotation", 0,360).setDuration(2000).start();
        ObjectAnimator.ofFloat( view, "translationX", 0,200).setDuration(2000).start();
        ObjectAnimator.ofFloat( view, "translationY", 0,200).setDuration(2000).start();
    }

    private void setRotateAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 45, 360);
        animator.setDuration(2000);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });



        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationStart(Animator animation) {

            }
        });
    }

    private void setTranslateAnimation(View view){
//        TranslateAnimation animation = new TranslateAnimation(0, 50, 0, 40);
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 2,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 2);

        animation.setDuration(2000);
//        ta.setRepeatCount(1);
//        ta.setRepeatMode(Animation.REVERSE);
//        ta.setFillAfter(true);

        view.startAnimation(animation);
    }

    private void setScaleAnimation(View view){
//        ScaleAnimation animation = new ScaleAnimation(0.1f, 4, 0.1f, 4);
//        ScaleAnimation animation = new ScaleAnimation(0, 4, 0, 4);
        ScaleAnimation animation = new ScaleAnimation(1, 4, 1, 4);

//        ScaleAnimation animation = new ScaleAnimation(0.1f, 4, 0.1f, 4, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);

        animation.setDuration(2000);
        view.startAnimation(animation);
    }

    private void setAlphaAnimation(View view) {
        AlphaAnimation animation = new AlphaAnimation(0, 0.5f);
        animation.setDuration(2000);
        view.startAnimation(animation);
    }

    private void setRotateAnimation(View view) {
        RotateAnimation animation = new RotateAnimation(45, 360);
        animation.setDuration(2000);
        view.startAnimation(animation);

    }

    private void setAnimationSet(View view) {
        AnimationSet animation = new AnimationSet(false);

    }



}
