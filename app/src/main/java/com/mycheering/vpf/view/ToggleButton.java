package com.mycheering.vpf.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zdyok on 2016/11/22.
 */

public class ToggleButton extends View {
    private Bitmap bgBitmap;// 滑动开关的背景图片
    private Bitmap slidBitmap;// 滑动块的背景图片
    private boolean currentState;// 记录滑动块的状态
    private int currScrollX;// 记录我们手指的X轴的位置
    private boolean isTouching = false;//记录是否触摸控件
    private onStateChangedListener mlistener;//状态改变监听接口
    // 在xml引用该控件时，调用该构造方法
    public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    // 在xml引用该控件时，调用该构造方法
    public ToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //命名空间
        String namespace = "http://schemas.android.com/apk/res/com.itheima.togglebuttondemo";
        //获取xml里面设置的属性值
        currentState = attrs.getAttributeBooleanValue(namespace, "CurrentState", false);
        int switchbgId = attrs.getAttributeResourceValue(namespace, "SwitchBtnBackgroud", -1);
        int slidebgId = attrs.getAttributeResourceValue(namespace, "SlidBtnBackgroud", -1);
        setBtnBg(switchbgId);
        setSlidBg(slidebgId);
    }
    // 在代码中创建控件时，调用该构造方法
    public ToggleButton(Context context) {
        super(context);
    }
    // 设置滑动按钮的背景
    public void setBtnBg(int switchBackground) {
        bgBitmap = BitmapFactory.decodeResource(getResources(),switchBackground);
    }
    // 设置滑动按钮的滑动块背景
    public void setSlidBg(int slideButtonBackground) {
        slidBitmap = BitmapFactory.decodeResource(getResources(),slideButtonBackground);
    }
    // 设置滑动按钮的状态
    public void setCurrentState(boolean isOpen) {
        currentState = isOpen;
    }
    // 1、测量宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置滑动开关的宽度和高度
        setMeasuredDimension(bgBitmap.getWidth(), bgBitmap.getHeight());
    }
    // 当手指触摸控件时，调用该方法，此方法为onDrown方法提供参数，和回调接口
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 手指按下
                isTouching = true;
                currScrollX = (int) event.getX();  //   手指触摸点相对于按钮背景图的坐标
                break;
            case MotionEvent.ACTION_MOVE:// 手指滑动
                isTouching = true;
                currScrollX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:// 手指抬起
                isTouching = false;
                int center = bgBitmap.getWidth()/2;
                currScrollX = (int) event.getX();
                boolean state = currentState;
                //如果手指触摸点大于中心线，这时候滑动块显示到右侧，并且状态为开
                currentState = currScrollX > center;

                //调用接口方法，传递状态值，只有状态值改变了才去回调子类方法
                if(mlistener !=null && state!=currentState){
                    mlistener.onStateChanged(currentState);
                }
                break;
        }
        invalidate();// 强制控件重新绘制，重新调用ondraw方法
        return true;// 自己处理触摸事件，重写了ACTION_MOVE 必须返回 true
    }
    // 2、滑动开关绘制
    /**
     * canvas 画布，将控件绘制到画布上，才能显示到屏幕上
     */
    @Override
    protected void onDraw(Canvas canvas) {

       /*
         在画布上绘制图片，先画背景图片,该方法成为了突破口，开始寻找left
         drawBitmap( Bitmap bitmap, float left, float top,  Paint paint)
         @param left   The position of the left side of the bitmap being drawn
         @param top    The position of the top side of the bitmap being drawn
         */
        canvas.drawBitmap(bgBitmap, 0, 0, null);
        //根据触摸状态来绘制滑动块
        int diffWidth = bgBitmap.getWidth() - slidBitmap.getWidth();  // 宽度差值
        if(isTouching){//当前为触摸状态，根据手指触摸的位置来绘制滑动块的位置
            int left = currScrollX - slidBitmap.getWidth() / 2;     // 让手指的触摸点永远在滑动块的中心点
            if (left < 0){ // 设置左边界
                left = 0;
            }
            if(left > diffWidth){  //设置右边界
                left = diffWidth;
            }
            canvas.drawBitmap(slidBitmap, left, 0, null);
        }else{//当前不是触摸状态，根据滑动开关的状态值来绘制滑动块背景
            if(currentState){//当前为开状态，滑动块显示在右侧
                canvas.drawBitmap(slidBitmap, diffWidth, 0, null);
            }else{//当前为关状态，滑动块显示在左侧
                canvas.drawBitmap(slidBitmap, 0, 0, null);
            }
        }
    }
    //    对外暴露公共的访问方法，接收接口的实现类对象
    public void setOnStateChangedListener(onStateChangedListener listener){
        mlistener = listener;
    }
    //状态值返回接口
    public interface onStateChangedListener{
        void onStateChanged(boolean currentState);
    }
}
