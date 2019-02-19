package com.yuenan.shame.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 功能描述：史上最简洁高效的圆形ImageView
 *
 * @author (作者) edward（冯丰枫）
 * @link http://www.jianshu.com/u/f7176d6d53d2
 * 创建时间： 2018/4/17 0017
 */
public class CircleImageView extends AppCompatImageView {
    private float width;
    private float height;
    private float radius;
    private boolean isMeasured = false;
    private Paint paint;
    private Matrix matrix;
    private BitmapShader bitmapShader;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);   //设置抗锯齿
        matrix = new Matrix();      //初始化缩放矩阵
    }

    /**
     *测量控件的宽高，并获取其内切圆的半径
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            radius = Math.min(width, height) / 2;
            isMeasured = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initBitmapAndShader();
        paint.setShader(bitmapShader);//将着色器设置给画笔
        canvas.drawCircle(width / 2, height / 2, radius, paint);//使用画笔在画布上画圆
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到与绘制区域等大，避免边界填充
     */
    private void initBitmapAndShader() {
        Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        matrix.setScale(width / bitmap.getWidth(), height / bitmap.getHeight());
        bitmapShader.setLocalMatrix(matrix);
    }
}
