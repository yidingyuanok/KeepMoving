package com.mycheering.vpf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mycheering.vpf.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zdyok on 2016/11/22.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private int downY;
    private int headerViewHeight;//头布局的高度
    private View headerView;

    private final int DOWN_PULL = 0;//下拉刷新状态
    private final int RELEASE_REFRESH = 1;//松开刷新状态
    private final int REFRESHING = 2;//正在刷新状态

    private int currentState = DOWN_PULL;
    private RotateAnimation upAnimation;//向上的动画
    private RotateAnimation downAnimation;//向下的动画
    private ImageView iv_arrow;
    private ProgressBar pb;
    private TextView tv_state;
    private TextView tv_time;
    private OnRefreshListener mOnRefreshListener;//刷新回调接口对象

    private boolean isLoadedMore = false;//记录当前是否处于加载更多的状态
    private View footerView;
    private int footerViewHeight;//脚布局的高度

    // 代码创建控件时，调用该构造方法，在构造方法中用this相互调用，少参数的调用多参数的
    public RefreshListView(Context context) {
        this(context, null);
    }

    //xml引用时，调用该构造方法
    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initHeaderView();
        initAnimtion();
        initFooterView();
        this.setOnScrollListener(this);
    }

    private void initAnimtion() {
        //初始化向上动画
        upAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);
        //初始化向下的动画
        downAnimation = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    // 在控件初始化的时候，添加头布局
    private void initHeaderView() {
        // 创建出头布局
        headerView = View.inflate(getContext(), R.layout.listview_header, null);

        iv_arrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) headerView.findViewById(R.id.pb);
        tv_state = (TextView) headerView.findViewById(R.id.tv_state);
        tv_time = (TextView) headerView.findViewById(R.id.tv_time);

        headerView.measure(0, 0);//让系统测量一下headerView的宽高
        //headerView.getHeight();    当控件完全显示到了屏幕上时，才通过该方法获取宽高  否则是0；
        //拿到头布局的高度
        headerViewHeight = headerView.getMeasuredHeight();
        //通过设置paddingtop来隐藏头布局
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        // 给listview加上头布局
        this.addHeaderView(headerView);
    }

    //初始化脚布局
    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.listview_footer, null);

        //隐藏脚布局
        //让系统帮助我们测量脚布局的宽高
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                if (currentState == REFRESHING) {
                    break;
                }
                int moveY = (int) ev.getY();
                //计算间距 = 移动后的点- 按下的点
                int deltaY = moveY - downY;
                //计算paddingtop = -头布局高度+间距
                int paddingtop = -headerViewHeight + deltaY;
                //拿到当前界面第一条数据的索引值
                int firstVisiblePosition = this.getFirstVisiblePosition();
                //如果不加判断就消费了所以触摸事件，阻碍了listview的上下滑动
                //只有 下滑并且当前的可见条目是第一条时 才拦截消费事件
                if (paddingtop > -headerViewHeight && firstVisiblePosition == 0) {
                    if (paddingtop > 0 && currentState == DOWN_PULL) {
                        System.out.println("松开刷新状态");
                        currentState = RELEASE_REFRESH;
                        //当状态改变时，调用onStateChange来刷新界面
                        onStateChanged();
                    } else if (paddingtop <= 0 && currentState == RELEASE_REFRESH) {
                        System.out.println("下拉刷新状态");
                        currentState = DOWN_PULL;
                        //当状态改变时，调用onStateChange来刷新界面
                        onStateChanged();
                    }
                    headerView.setPadding(0, paddingtop, 0, 0);
                    return true;//自己消费了触摸事件
                }
                break;
            case MotionEvent.ACTION_UP://抬起
                if (currentState == DOWN_PULL) {
                    //当前为下拉刷新，直接隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                } else if (currentState == RELEASE_REFRESH) {
                    //当前为松开刷新，状态变为正在刷新
                    currentState = REFRESHING;
                    //当状态改变时，调用onStateChange来刷新界面
                    onStateChanged();
                    //下拉刷新接口回调
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onFresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);//listview自己处理触摸事件，滚动事件
    }

    //根据当前的状态来改变界面，仅仅是更改界面
    private void onStateChanged() {
        switch (currentState) {
            case DOWN_PULL://下拉刷新
                iv_arrow.startAnimation(downAnimation);
                tv_state.setText("下拉刷新");
                break;
            case RELEASE_REFRESH://松开刷新
                iv_arrow.startAnimation(upAnimation);
                tv_state.setText("松开刷新");
                break;
            case REFRESHING://正在刷新
                //清除箭头动画并隐藏
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(View.INVISIBLE);
                //进度圈显示
                pb.setVisibility(View.VISIBLE);
                //改变文本
                tv_state.setText("正在刷新...");
                //头布局正好完全显示
                headerView.setPadding(0, 0, 0, 0);
                break;
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    //定义刷新回调接口
    public interface OnRefreshListener {
         void onFresh();
         void onLoadMore();
    }

    //刷新结束，通知方法
    public void onRefreshFinish() {
        if (isLoadedMore) {//加载更多完成后的处理
            isLoadedMore = false;
            footerView.setPadding(0, -footerViewHeight, 0, 0);
        } else {
            //下拉刷新完成后的处理
            iv_arrow.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            tv_state.setText("下拉刷新");
            currentState = DOWN_PULL;
            headerView.setPadding(0, -headerViewHeight, 0, 0);  //隐藏头布局
            tv_time.setText("最新刷新时间：" + getTime());
        }
    }

    //获取当前时间
    private String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    //当listview滚动状态发生改变的时候，会调用该方法（三种状态之间改变）

    /**
     * OnScrollListener.SCROLL_STATE_FLING;//手指未触摸屏幕，处于一个惯性的滑动状态 2
     * OnScrollListener.SCROLL_STATE_IDLE;//手指未触摸屏幕，处于停滞状态 0
     * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;//手指触摸着屏幕，进行滑动状态 1
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int lastVisiblePosition = this.getLastVisiblePosition();
        //当前列表最后一条数据的索引为getcount-1，并且手指离开屏幕的时候，进行加载更多的操作
        if (lastVisiblePosition == (getCount() - 1)
                && scrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && !isLoadedMore) {
            isLoadedMore = true;      //   防止加载两次，fling 和 idle
            System.out.println("加载更多");

            //显示脚布局
            footerView.setPadding(0, 0, 0, 0);
            //让listview显示到加载前的最后一条数据
            this.setSelection(getCount() - 1);

            //调用加载更多的回调方法
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}