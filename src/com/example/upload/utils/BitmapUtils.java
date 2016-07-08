package com.example.upload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class BitmapUtils {

	/**
     * 通过文件获取图片
     * 
     * @param f
     * @return
     * @throws CrashHandler
     */
    public static Bitmap decodeFile(File f) throws IOException {
	try {
	    BitmapFactory.Options option = new BitmapFactory.Options();
	    option.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(new FileInputStream(f), null, option);

	    final int size = 240;
	    int widthTemp = option.outWidth;
	    int heightTemp = option.outHeight;
	    int scale = 1;
	    while (true) {
		if (widthTemp / 2 < size || heightTemp / 2 < size)
		    break;
		widthTemp /= 2;
		heightTemp /= 2;
		scale *= 2;
	    }

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inSampleSize = scale;
	    return BitmapFactory.decodeStream(new FileInputStream(f), null,
		    options);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	    throw new IOException(e);
	}

    }
    
    /**
     * 根据路径获得图片并压缩,返回bitmap用于显示
     * 
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth,
	    int reqHeight) {// 480*800
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(filePath, options);

	// Calculate inSampleSize
	 options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile(filePath, options);
    }
    
    /**
     * 计算图片的缩放值
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
	    int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {
	    // Calculate ratios of height and width to requested height and
	    // width
	    final int heightRatio = Math.round((float) height
		    / (float) reqHeight);
	    final int widthRatio = Math.round((float) width / (float) reqWidth);

	    // Choose the smallest ratio as inSampleSize value, this will
	    // guarantee
	    // a final image with both dimensions larger than or equal to the
	    // requested height and width.
	    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}
	return inSampleSize;
    }
}
