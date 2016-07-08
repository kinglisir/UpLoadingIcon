package com.example.upload.imageloader;

import java.io.File;
import java.io.IOException;

import com.example.upload.R;
import com.example.upload.app.UploadApplication;
import com.example.upload.utils.BitmapUtils;
import com.example.upload.utils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * 
 * imageloader 配置
 * <p>:所有图片加载都通过这个类，包括网络图片和本地图片
 * @author oceangray
 *
 */
public class ImageLoaderHelper {
	//图片缓存地址
	private static String imageCacheDir=FileUtils.getRootDir(UploadApplication.getApplication())+"/image/";// 缓存的目录地址
	public static void initImageLoader(Context context){
		//显示图片配置项目，包括缓存、图片为空显示图片、加载失败显示图片等
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(500)).build();
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, imageCacheDir);// 获取到缓存的目录地址
		//初始化配置文件
		ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
		.defaultDisplayImageOptions(options)
		.threadPriority(Thread.NORM_PRIORITY)//线程优先级
		.denyCacheImageMultipleSizesInMemory()// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())//文件名字为 MD5值
		.diskCacheSize(10 * 1024 * 1024)//100MB
		.memoryCacheSize(2 * 1024 * 1024)//2MB
		.memoryCache(new WeakMemoryCache())
		.diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
		.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))//连接5秒，读取30秒
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()//开始时候用，生产环境需要取消
		.build();
		//初始化ImageLoader
		ImageLoader.getInstance().init(config);
	}
	//当图片显示不了，默认显示用户默认头像 ,配置 Options 
	static DisplayImageOptions defaultUserHeaderoptions=new DisplayImageOptions.Builder().
			cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	//当图片显示不了，默认显示用户默认头像 ,配置 Options 
	static DisplayImageOptions defaultCompanyHeaderoptions=new DisplayImageOptions.Builder().
			cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			
			.bitmapConfig(Bitmap.Config.RGB_565).build();
	/**
	 * 
	 * 加载网络头像图片
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultUserHeaderoptions);
	}
	/**
	 * 
	 * 加载企业网络头像图片
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetCompanyHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultCompanyHeaderoptions);
	}
	/**
	 * 
	 * 加载群组网络头像图片
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetGroupHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultCompanyHeaderoptions);
	}
	/**
	 * 加载网络图片
	 * 
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView);
	}
	/**
	 * 加载网络图片
	 * 并自定义显示图片的项目：图片为空的时候显示、图片加载的时候显示
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	/**
	 * 加载网络图片
	 * 并自定义显示图片的项目：图片为空的时候显示、图片加载的时候显示,并监听图片加载的过程
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options, 
			ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(url, imageView, options, listener);
	}
	/**
	 * 加载网络图片
	 * 并自定义显示图片的项目：图片为空的时候显示、图片加载的时候显示,并监听图片加载的过程和图片加载的进度
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options, 
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		ImageLoader.getInstance().displayImage(url, imageView, options, listener, progressListener);
	}
	/**
	 * 加载本地sd 卡的照片(原图)
	 * @param imageView
	 * @param imagePath
	 */
	private static void displayDiskImage(ImageView imageView, String imagePath) {
		String imageUrl = Scheme.FILE.wrap(imagePath);
		ImageLoader.getInstance().displayImage(imageUrl, imageView);//路径你要是给的本地 就是加载本地的数据, 不请求网络
	}
	/**
	 * 加载本地sd 卡的照片(缩略图)
	 * @param imageView
	 * @param imagePath
	 */
	private static void displayDiskImage200(ImageView imageView, String imagePath) {
		imageView.setImageBitmap(BitmapUtils.getSmallBitmap(imagePath, 100, 100));
	}
	/**
	 * 显示全图
	 * @param imageView
	 * @param imagePath
	 */
	public static void displayImage(ImageView imageView, String imagePath){
		if(TextUtils.isEmpty(imagePath)){
			return ;
		}
		File file =new File(imagePath);
		//本地图片
		if(file.exists()){
			displayDiskImage(imageView, imagePath);
		//网络图片
		}else{
			displayNetImage(imageView, imagePath);
		}
	}
	/**
	 * 显示缩略图
	 * @param imageView
	 * @param imagePath
	 */
	public static void displayImage200(ImageView imageView, String imagePath){
		if(TextUtils.isEmpty(imagePath)){
			return ;
		}
		File file =new File(imagePath);
		//本地图片
		if(file.exists()){
			displayDiskImage200(imageView, imagePath);
		//网络图片
		}else{
           displayNetImage(imageView,imageUrlConvert200ImageUrl(imagePath));
		}
	}
	
	 /**
		 * 把大图URL地址转换为200的缩略图地址（和服务器约定好的）
		 * @param destUrl
		 * @return
		 */
		public static String imageUrlConvert200ImageUrl(String destUrl){
			int fileFormat = destUrl.lastIndexOf(".");
			String fileUrl =destUrl + "_200."+ destUrl.substring(fileFormat + 1, destUrl.length());
			return fileUrl;
		}
	/**
	 * 异步获取图片
	 * @param uri
	 * @return
	 */
	public static Bitmap loadImageSync(String uri){
		return ImageLoader.getInstance().loadImageSync(uri);
	}
	/**
	 * 异步获取图片
	 * @param uri
	 * @return
	 */
	public static Bitmap loadHeaderImageSync(String uri){
		return ImageLoader.getInstance().loadImageSync(uri,defaultUserHeaderoptions);
	}
	/**
	 * 缓存图片存储
	 * @param uri
	 * @return  ,如果没有bitmap , 传入路径也可以;
	 */
	public static void storeImageToCache(String serverImageUrl,String localFileUrl){
		try {
			ImageLoader.getInstance().getDiskCache().save(serverImageUrl, BitmapUtils.decodeFile(new File(localFileUrl)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 缓存图片存储
	 * @param uri
	 * @return   ImageLoader中，可以通过getDiskCache().get(url)获取到磁盘缓存  , save 存储 缓存
	 */
	public static void storeImageToCache(String serverImageUrl,Bitmap bitmap){
		try {
			ImageLoader.getInstance().getDiskCache().save(serverImageUrl, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}

