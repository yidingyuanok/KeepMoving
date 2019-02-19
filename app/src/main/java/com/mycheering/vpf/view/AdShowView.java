package com.mycheering.vpf.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.mycheering.vpf.utils.L;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class AdShowView extends View {
	private WindowManager mWManager; // WindowManager
	private WindowManager.LayoutParams mSpotParams; // WindowManager参数
	private Context mContext;
	
	private int mWidth;
	private int mHeight;
	
	private static final int DELETE_LI_ID = 10;
	private static final int LINE_ID = 11;
	private static final int AD_ID = 12;
	private static final String ICON_DELETE = StrUtils.deCrypt("ad_delete.png");
	private AdItem mSpotItem;
	
	public AdShowView(Context context, AdItem item, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mWManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		// 获取屏幕像素相关信息
		DisplayMetrics dm = new DisplayMetrics();
		mWManager.getDefaultDisplay().getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;
		mSpotItem = item;

		showFloatAdView(mContext, null);
	}
	
	public void showFloatAdView(Context context, Bitmap bitmap) {
		mSpotParams = new WindowManager.LayoutParams();
		
//		if ( getAppOps(context)){
			mSpotParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;// 系统提示window
//		}else{
//			mSpotParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//		}
		
		mSpotParams.format = PixelFormat.TRANSLUCENT;// 支持透明
		mSpotParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mSpotParams.flags = mSpotParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
		
		mSpotParams.width = mWidth;
		mSpotParams.height = mHeight;
		
		mSpotParams.x = 0;// 窗口位置的偏移量
		mSpotParams.y = 0;
		mSpotParams.gravity = Gravity.CENTER;
		
		mWManager.addView(addAdView(context, mSpotItem, bitmap), mSpotParams);
	}
	

	
	public RelativeLayout addAdView(Context context, AdItem item, Bitmap bitmap) {

		// 两张展现模式，主要是关闭按钮的位置区别
		int type =  rdScope(1, 4);
		if (type < 3) {
			return adTwoView(context, bitmap);
		} else {
			return adView(context, bitmap);
		}

	}


	
	private RelativeLayout adView(Context context, Bitmap bitmap) {
		final RelativeLayout  rlParent = new RelativeLayout(context);
		LayoutParams spotLp = new LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		rlParent.setLayoutParams(spotLp);
		rlParent.setPadding( getDip(context, 35),  getDip(context, 50),  getDip(context, 35),  getDip(context, 90));
		rlParent.setGravity(Gravity.CENTER_HORIZONTAL);
		rlParent.setBackgroundColor(0xe0000000);
		rlParent.getBackground().setAlpha(150);

		LinearLayout lLDel = new LinearLayout(context);
		LayoutParams delLlLp = new LayoutParams( getDip(context, 35),  getDip(context, 85));
		delLlLp.setMargins( getDip(context, 20), 0, 0, 0);
		lLDel.setLayoutParams(delLlLp);
		lLDel.setId(DELETE_LI_ID);
		lLDel.setOrientation(LinearLayout.VERTICAL);

		ImageView ivDel = new ImageView(context);
		LinearLayout.LayoutParams delLp = new LinearLayout.LayoutParams( getDip(context, 30),  getDip(context, 30));
		ivDel.setLayoutParams(delLp);
		ivDel.setImageDrawable( getBitmapdDrawable(ICON_DELETE));
		ivDel.setVisibility(View.VISIBLE);
		ivDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSpotView(rlParent);
			}
		});

		lLDel.addView(ivDel);

		View viLine = new View(context);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams( getDip(context, 3),  getDip(context, 56));
		lineLp.gravity = Gravity.CENTER_HORIZONTAL;
		viLine.setLayoutParams(lineLp);
		viLine.setId(LINE_ID);
		viLine.setBackgroundColor(0xff456456);
		lLDel.addView(viLine);
		rlParent.addView(lLDel);

		ImageView ivAd = new ImageView(context);
		LayoutParams adLp = new LayoutParams(mWidth -  getDip(context, 40),
				mHeight -  getDip(context, 220));
		adLp.addRule(RelativeLayout.BELOW, DELETE_LI_ID);
		adLp.setMargins(15, 15, 15, 15);
		ivAd.setLayoutParams(adLp);
		if(null != bitmap) {
			ivAd.setImageBitmap(bitmap);
		} else {
			//TODO
			ivAd.setBackgroundDrawable( getBitmapdDrawable(mContext, "ad_img.png"));
		}
		ivAd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeSpotView(rlParent);
			}
		});
		ivAd.setId(AD_ID);
		rlParent.addView(ivAd);
		return rlParent;
	}

	private RelativeLayout adTwoView(Context context,  Bitmap bitmap) {
		final RelativeLayout rlParent = new RelativeLayout(context);
		LayoutParams spotLp = new LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		rlParent.setLayoutParams(spotLp);
		rlParent.setPadding( getDip(context, 35),  getDip(context, 70),  getDip(context, 35),  getDip(context, 90));
		rlParent.setGravity(Gravity.CENTER_HORIZONTAL);
		rlParent.setBackgroundColor(0xe0000000);
		rlParent.getBackground().setAlpha(150);

		RelativeLayout bg = new RelativeLayout(context);
		LayoutParams bgLp = new LayoutParams(LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		bg.setLayoutParams(bgLp);
		bg.setPadding( getDip(context, 30),  getDip(context, 80),  getDip(context, 30),  getDip(context, 80));
		bg.setGravity(Gravity.CENTER_HORIZONTAL);
		 shapePic(bg, 0xffffffff);

		ImageView ivAd = new ImageView(context);
		LayoutParams adLp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT );
		adLp.bottomMargin =  getDip(context, 80);
		ivAd.setLayoutParams(adLp);
		if(null != bitmap) {
			ivAd.setImageBitmap(bitmap);
		} else {
			//TODO
			ivAd.setBackgroundDrawable( getBitmapdDrawable(mContext, "ad_img.png"));
		}
		ivAd.setId(AD_ID);
		ivAd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			removeSpotView(rlParent);
			}
		});

		ImageView ivDel = new ImageView(context);
		LayoutParams delLp = new LayoutParams( getDip(context, 35),  getDip(context, 35));
		delLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		delLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		delLp.topMargin =  getDip(context, 20);
		ivDel.setLayoutParams(delLp);
		ivDel.setImageDrawable( getBitmapdDrawable(ICON_DELETE));
		ivDel.setId(DELETE_LI_ID);
		ivDel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeSpotView(rlParent);
				
			}
		});
		
		rlParent.addView(ivAd);
		rlParent.addView(ivDel);
		
		return rlParent;
	}
	
	public void removeSpotView(View view) {
		if (view.isShown()) {
			mWManager.removeView(view);
		}
	}


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public static void shapePic(View view, int color) {
		/**
		 * 外部矩形弧度
		 */
		float[] outerRadian = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
		/**
		 * 内部矩形与外部矩形的距离
		 */
		//RectF insetDistance = new RectF(100, 100, 50, 50);
		/**
		 * 内部矩形弧度
		 */
		float[] insideRadian = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
		/**
		 * 如果insetDistance与insideRadian设为null亦可
		 */
		RoundRectShape roundRectShape = new RoundRectShape(outerRadian, null, insideRadian);
		ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
		/**
		 * 指定填充颜色
		 */
		drawable.getPaint().setColor(color);
		/**
		 * 指定填充模式
		 */
		drawable.getPaint().setStyle(Paint.Style.FILL);
		view.setBackground(drawable);
	}



	/**
	 * dip单位转换
	 *
	 * dp2px???
	 */
	public static int getDip(Context context,float size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
	}

	public static int  rdScope(int startNum, int endNum) {
		Random random = new Random();
		int s = random.nextInt(endNum) % (endNum - startNum + 1) + startNum;
		return s;
	}

	//该方法仅获取icon_delete.png图片的时候使用，切记!!!   获取其他的图片用getBitmapdDrawable(Context context, String fileName)
	public static Drawable getBitmapdDrawable(String fileName) {
		InputStream inputStream;
		if (USE_ASSETS_IMG){
			inputStream = getAssetStream(fileName);
		} else {
			String s = StrUtils.deCrypt( "-119,80,78,71,13,10,26,10,0,0,0,13,73,72,68,82,0,0,0,80,0,0,0,86,8,3,0,0,0,111,-106,-31,-126,0,0,1,92,80,76,84,69,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,-3,-3,-3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-2,-2,-2,-3,-3,-3,-1,-1,-1,-17,-17,-17,-19,-19,-19,-3,-3,-3,-10,-10,-10,-2,-2,-2,-1,-1,-1,-1,-1,-1,-5,-5,-5,-3,-3,-3,-1,-1,-1,-88,-88,-88,-9,-9,-9,-1,-1,-1,-1,-1,-1,-1,-1,-1,-4,-4,-4,-11,-11,-11,-1,-1,-1,-19,-19,-19,-30,-30,-30,-9,-9,-9,-3,-3,-3,-1,-1,-1,-1,-1,-1,-20,-20,-20,-45,-45,-45,-53,-53,-53,-67,-67,-67,115,115,115,-1,-1,-1,-90,-90,-90,-49,-49,-49,-66,-66,-66,-1,-1,-1,-36,-36,-36,-36,-36,-36,-77,-77,-77,-38,-38,-38,125,125,125,100,100,100,-66,-66,-66,-112,-112,-112,-90,-90,-90,-44,-44,-44,111,111,111,68,68,68,68,68,68,-114,-114,-114,91,91,91,-1,-1,-1,67,67,67,78,78,78,64,64,64,88,88,88,92,92,92,84,84,84,73,73,73,49,49,49,59,59,59,55,55,55,-6,-6,-6,-18,-18,-18,-15,-15,-15,98,98,98,117,117,117,-86,-86,-86,-83,-83,-83,120,120,120,-75,-75,-75,103,103,103,-49,-49,-49,-23,-23,-23,-91,-91,-91,-108,-108,-108,126,126,126,-27,-27,-27,-36,-36,-36,106,106,106,-32,-32,-32,-42,-42,-42,-56,-56,-56,-62,-62,-62,-70,-70,-70,-102,-102,-102,-114,-114,-114,-11,-11,-11,-121,-121,-121,111,111,111,-66,-66,-66,-96,-96,-96,67,-76,97,-10,0,0,0,75,116,82,78,83,0,5,11,18,31,24,66,51,45,39,77,91,58,97,107,85,115,-16,-118,-34,-51,-85,121,78,56,-7,-26,-97,-101,24,-41,-56,-61,-73,-96,-111,-114,98,65,49,10,-9,-15,-27,-36,-40,-44,-68,99,42,16,-61,-62,-125,127,109,83,79,43,32,-47,-60,-79,-84,-111,-122,116,116,-87,-107,116,116,101,60,38,95,-54,64,-81,0,0,5,-39,73,68,65,84,88,-61,-91,-40,-119,87,-38,48,28,7,-16,57,21,69,-90,-52,-51,123,-70,-61,-23,14,55,-99,-18,-66,-17,-69,-91,66,44,21,80,17,-71,-68,-64,-87,-5,-1,-33,91,-14,109,66,-102,80,91,-22,-66,-18,109,-104,52,-97,-9,-53,65,11,-69,16,-98,123,43,-29,-53,99,99,99,47,-106,-57,39,102,46,-4,111,-34,-52,79,95,55,60,121,48,-6,-30,-18,-7,-75,-15,-39,-85,-122,79,-122,71,-105,31,-97,67,91,-103,-98,52,-50,-50,-24,-99,-120,-36,-35,91,70,72,70,-58,35,112,51,-93,-34,-95,78,-83,116,88,44,22,27,-121,-91,-67,63,-34,-10,27,29,-81,-26,109,57,40,119,-48,-36,54,45,-103,-12,-47,-23,-114,-20,-99,125,-36,-47,108,71,-60,-11,127,26,5,-53,39,-7,-115,-106,-7,-96,-125,121,95,17,123,81,-85,-96,52,-33,84,-9,5,121,59,-52,-69,-55,47,92,63,-78,2,83,16,-28,-56,74,16,-9,-8,6,-33,-121,77,98,-123,-91,-110,51,-112,-85,19,1,-17,11,-66,124,123,121,-85,-125,100,14,-8,66,-98,121,38,103,-72,87,52,-83,-50,-46,116,-36,-73,-50,89,53,-70,-13,117,42,86,-57,57,-55,-71,-77,94,9,-40,15,-25,-40,-118,-112,-68,123,-126,-82,-33,-13,59,47,-82,-73,101,69,-54,106,13,-61,110,-74,123,19,-109,-16,-86,102,-60,-92,-41,33,94,105,3,-35,13,-87,-104,-111,115,-126,55,-8,-28,27,-33,-9,111,-47,60,71,-114,-3,38,61,-125,-58,58,-23,-48,-80,-20,-84,109,-103,34,69,-97,73,-33,-62,2,-26,-107,81,118,118,45,-101,-75,-4,-72,53,-92,-43,71,-10,112,26,-67,59,125,-57,96,-39,-48,71,97,-104,-65,-121,8,113,-53,-67,79,-24,71,-80,-90,76,24,35,32,-6,120,-92,-36,104,90,-98,-82,67,-120,-78,-60,9,-4,126,76,-117,111,-3,100,-23,-88,-45,-99,122,5,34,-111,63,-16,112,84,-10,-24,43,-117,55,-82,58,106,-119,-93,-40,17,83,43,112,31,-53,-96,-42,40,60,-106,-65,107,107,-74,-78,47,15,-124,119,15,-3,71,-98,2,25,-104,55,16,-120,122,125,-56,33,-21,-32,-19,105,-108,56,-50,-63,101,-36,81,-119,-74,80,5,-61,35,-22,-11,-95,3,21,-14,28,-32,-31,-22,61,51,-101,68,9,-67,120,-35,35,-38,104,84,-68,-11,-44,-38,-102,73,68,-86,-84,-23,62,7,-47,-65,-85,-126,116,83,78,28,69,-44,-68,92,-98,22,78,100,-48,62,33,15,97,-115,-88,97,-69,82,80,69,83,-9,80,-96,-56,-87,124,-73,-116,-31,93,76,-76,88,109,98,-96,71,-114,-16,-20,7,-8,8,-121,-112,-76,-119,-39,108,86,-118,89,22,-113,71,127,83,60,-110,70,7,-64,97,-10,106,-107,-124,-120,65,30,-126,59,-19,93,113,-93,-55,17,-97,-104,-118,24,-32,33,56,56,-13,98,79,-22,36,84,108,4,123,100,67,-20,-54,11,-100,-6,-108,111,20,81,-15,-38,-81,-83,-32,104,-73,54,57,21,36,-122,121,8,110,-36,-113,-60,-45,110,51,21,40,-122,120,72,85,-100,-101,105,-10,-94,-100,10,22,-125,61,-92,-128,7,52,5,103,113,43,58,19,36,-30,-68,32,13,-6,-101,-27,127,-31,46,62,67,-56,10,-61,61,100,19,98,64,-123,98,13,-61,-68,112,17,107,56,34,118,-71,17,-26,-123,-117,71,98,-105,-25,-39,-117,82,-72,-105,115,-126,-59,50,-21,-101,22,79,-88,-67,-116,79,-120,109,-37,-7,-106,-73,-67,-43,18,105,-69,-103,-47,-45,96,93,99,93,-12,99,48,-98,-15,-103,-108,-2,-93,123,-74,-83,-119,-38,-27,-5,120,-86,80,-80,11,95,-24,10,97,-11,-39,52,-127,53,-30,51,-45,12,3,-35,-125,-104,9,-85,-81,93,84,-82,-33,-62,-87,-71,-56,64,119,87,50,106,82,109,94,-118,4,-42,-120,-101,-51,108,-41,5,10,-66,-58,-73,-90,-76,10,90,109,94,38,-93,-118,104,-109,-63,18,-50,3,-68,-120,69,-84,-24,11,-104,-42,61,77,108,42,37,-26,-47,-4,-38,5,-89,49,103,-67,-64,125,-51,-45,-59,29,-37,-74,-28,0,-100,-62,-91,-117,46,-8,10,7,-25,-60,11,-38,54,17,-34,46,60,-98,-108,20,-1,120,-63,85,124,27,-72,-58,-63,110,-52,-7,84,5,77,71,-13,116,-15,-64,59,-27,-118,-127,25,-69,96,87,-9,53,-108,-72,-21,1,77,-37,62,84,60,85,-52,-79,-9,-106,-73,3,-97,97,111,117,83,16,37,-10,78,-94,-60,85,-103,-108,101,-111,67,-57,-88,-77,47,125,41,-76,120,-69,44,-78,117,66,-1,54,91,109,110,-127,-81,-70,-69,4,-24,-106,88,-11,12,51,-39,-128,-108,37,60,93,68,50,-94,37,-115,21,124,-44,-117,2,-79,-120,-65,81,98,-35,51,42,-61,68,-59,-45,-5,76,120,72,-111,23,40,64,-108,-56,82,86,10,49,-23,32,-126,81,62,-90,-73,-67,-22,96,5,123,0,-14,18,123,-106,-60,-92,-93,-121,127,-127,-4,-59,102,44,-63,-105,104,92,-33,-114,-18,-91,75,24,122,77,22,-120,109,-119,-69,-109,-82,-89,-39,37,-54,-11,-38,31,-83,71,44,-32,82,-100,22,8,80,-108,-40,-65,-124,-114,18,70,40,-90,26,-67,111,3,-61,-122,95,-54,2,-7,-74,-12,-68,28,-26,98,-92,-72,-98,-15,57,-34,-117,67,-88,-108,-8,-39,64,-10,-13,17,-68,-94,-127,124,-21,71,-127,30,-112,-119,125,-49,12,-92,86,72,-53,-84,-22,127,16,-2,42,95,50,-112,15,125,-18,10,42,98,119,111,92,-120,78,-77,-77,-14,-86,59,6,-14,-16,-110,44,80,-99,-12,37,42,34,7,-69,29,120,-101,14,-9,98,-16,116,16,98,108,-18,-66,-127,-28,-54,97,-36,81,-51,-32,-13,-115,-11,-11,104,19,-106,59,-35,23,-5,50,-59,-81,-37,41,-25,-125,-72,-70,-8,-1,-45,-71,24,93,64,49,97,125,-46,76,28,120,103,-16,-84,23,79,-4,-75,-19,-14,-98,-72,102,-22,11,-9,-4,64,38,-58,-87,56,55,108,-120,-44,-53,5,93,-37,109,-106,28,67,-28,73,-110,122,-54,2,-22,34,106,-4,-119,34,69,-99,-91,98,-13,-72,-80,77,-91,-83,106,101,-13,-96,102,-56,44,126,31,8,-16,-124,72,-9,122,32,-7,124,-47,8,-51,-44,92,98,64,-50,55,88,-116,13,36,-98,-65,13,-26,30,-50,13,38,99,-12,-4,5,122,16,-23,94,-77,-123,76,14,-2,120,58,117,-106,-74,-16,-28,-7,32,43,47,-44,-125,-120,34,65,14,125,122,-1,118,-95,-67,-74,-89,31,-121,40,71,-53,99,-53,7,47,88,-60,-76,-29,108,-34,-55,-60,-32,-48,-27,79,95,-97,46,78,45,-48,-13,126,127,97,106,-15,-55,-5,-113,67,67,67,-125,73,-54,-11,-59,81,30,-9,-62,-117,4,9,-109,10,-105,121,24,-106,72,12,8,78,-108,23,46,10,-78,-113,-103,20,77,36,6,105,-24,63,73,-118,81,-83,-97,113,-94,-68,40,36,51,25,42,-62,48,-90,69,-28,36,9,51,-34,-33,79,89,22,-6,34,-50,52,76,-42,-27,34,-110,48,123,-87,-54,3,-116,106,-110,-117,104,2,-91,-86,12,48,-123,-117,110,10,22,-106,-122,-99,-97,-19,-100,-6,7,21,83,-68,-125,58,-92,-59,-35,0,0,0,0,73,69,78,68,-82,66,96,-126");
			String[] strs = s.split(StrUtils.deCrypt(","));
			int length = strs.length;
			byte[] bytes = new byte[length];

			for (int i=0; i<length; i++){
				bytes[i] = Byte.valueOf(strs[i]);
			}
			inputStream = new ByteArrayInputStream(bytes);
		}

		BitmapDrawable mBitmapDrawable = new BitmapDrawable(null,inputStream);
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return mBitmapDrawable;
	}

	public static boolean USE_ASSETS_IMG = true;
	public static Drawable getBitmapdDrawable(Context context, String fileName) {
		if (USE_ASSETS_IMG){
			return getBitmapdDrawable(fileName);
		} else {
			InputStream inputStream;
			try {
				inputStream = getAssetStream(context, fileName);
				BitmapDrawable mBitmapDrawable = new BitmapDrawable(null,inputStream);
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return mBitmapDrawable;
			} catch (Exception e1) {
				L.e("图片获取失败");
				e1.printStackTrace();
			}
			return null;
		}
	}

	public final static String ASSETS_PATH = StrUtils.deCrypt("/assets/");

	public static InputStream getAssetStream(Context context, String fileName) throws FileNotFoundException {
		String path = context.getFilesDir().toString() + ASSETS_PATH + fileName;
		return new FileInputStream(path);
	}

	public static InputStream getAssetStream(String fileName) {
		String path = ASSETS_PATH + fileName;
		return AdShowView.class.getResourceAsStream(path);
	}

}
