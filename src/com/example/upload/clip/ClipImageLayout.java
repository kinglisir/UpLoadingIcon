package com.example.upload.clip;
import java.io.File;
import java.io.IOException;




import com.example.upload.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * @author zhy
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;

	/**
	 * ������ԣ�ֱ��д���˴�С������ʹ�ù����У�������ȡΪ�Զ�������
	 */
	private int mHorizontalPadding = 20;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
		// ����padding��px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}
	/**
	 * ����ͼƬ
	 * @param imagePath
	 */
	public void setImagePath(String imagePath){
		/**
		 * ������ԣ�ֱ��д����ͼƬ������ʹ�ù����У�������ȡΪ�Զ�������
		 */
		try {
			Log.e("imagePath", imagePath);
			mZoomImageView.setImageBitmap(BitmapUtils.decodeFile(new File(imagePath)));
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	/**
	 * ���⹫�����ñ߾�ķ���,��λΪdp
	 * 
	 * @param mHorizontalPadding
	 */
	public void setHorizontalPadding(int mHorizontalPadding)
	{
		this.mHorizontalPadding = mHorizontalPadding;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
