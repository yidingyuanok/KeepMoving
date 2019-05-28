package com.mycheering.vpf.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.mycheering.vpf.utils.L;

/**
 * Created by Turing on 2017/7/7.
 */

public class DragView extends View {
    private int mLastX;
    private int mLastY;
    private Scroller mScroller;
    private View mParentView;

    public DragView(Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:

                int diffX = x - mLastX;
                int diffY = y - mLastY;
//                activity_move(getLeft() + diffX, getTop() + diffY, getRight() + diffX, getBottom() + diffY);
                mParentView = (View) getParent();
                mParentView.scrollBy(-diffX, -diffY);
//                return true;
                break;
            case MotionEvent.ACTION_UP:

                mParentView = (View) getParent();
                mScroller.startScroll(mParentView.getScrollX(), mParentView.getScrollY(), 0 - mParentView.getScrollX(), 0 - mParentView.getScrollY());
                L.i("onTouchEvent() up mParentView.getScrollX() : " + mParentView.getScrollX());
                invalidate();

                break;


        }

//        return super.onTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
//            L.i("computeScroll() called mParentView : " + mParentView);
            if (mParentView != null) {
                mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                L.i("computeScroll() called mParentView mScroller.getCurrX()  :  " + mScroller.getCurrX());
            }
            invalidate();
        }
    }

//    public void setParentView(View view) {
//        mParentView = view;
//    }
}
