package com.example.upload.app;


import com.free.shishi.imageloader.ImageLoaderHelper;

import android.app.Application;

public class UploadApplication extends Application {
	private static UploadApplication application;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = this;
		ImageLoaderHelper.initImageLoader(this);//初始化imageloader
	}
	
	/**
	 * 获取application 对象
	 * 
	 * @return
	 */
	public static UploadApplication getApplication() {
		return application;
	}
}
