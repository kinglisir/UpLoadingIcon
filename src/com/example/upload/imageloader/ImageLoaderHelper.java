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
 * imageloader ����
 * <p>:����ͼƬ���ض�ͨ������࣬��������ͼƬ�ͱ���ͼƬ
 * @author oceangray
 *
 */
public class ImageLoaderHelper {
	//ͼƬ�����ַ
	private static String imageCacheDir=FileUtils.getRootDir(UploadApplication.getApplication())+"/image/";// �����Ŀ¼��ַ
	public static void initImageLoader(Context context){
		//��ʾͼƬ������Ŀ���������桢ͼƬΪ����ʾͼƬ������ʧ����ʾͼƬ��
		DisplayImageOptions options=new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisk(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(500)).build();
		
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, imageCacheDir);// ��ȡ�������Ŀ¼��ַ
		//��ʼ�������ļ�
		ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
		.defaultDisplayImageOptions(options)
		.threadPriority(Thread.NORM_PRIORITY)//�߳����ȼ�
		.denyCacheImageMultipleSizesInMemory()// ��ͬһ��Uri��ȡ��ͬ��С��ͼƬ�����浽�ڴ�ʱ��ֻ����һ����Ĭ�ϻỺ������ͬ�Ĵ�С����ͬͼƬ
		.diskCacheFileNameGenerator(new Md5FileNameGenerator())//�ļ�����Ϊ MD5ֵ
		.diskCacheSize(10 * 1024 * 1024)//100MB
		.memoryCacheSize(2 * 1024 * 1024)//2MB
		.memoryCache(new WeakMemoryCache())
		.diskCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
		.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))//����5�룬��ȡ30��
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.writeDebugLogs()//��ʼʱ���ã�����������Ҫȡ��
		.build();
		//��ʼ��ImageLoader
		ImageLoader.getInstance().init(config);
	}
	//��ͼƬ��ʾ���ˣ�Ĭ����ʾ�û�Ĭ��ͷ�� ,���� Options 
	static DisplayImageOptions defaultUserHeaderoptions=new DisplayImageOptions.Builder().
			cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	//��ͼƬ��ʾ���ˣ�Ĭ����ʾ�û�Ĭ��ͷ�� ,���� Options 
	static DisplayImageOptions defaultCompanyHeaderoptions=new DisplayImageOptions.Builder().
			cacheInMemory(true).cacheOnDisk(true).showImageOnFail(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			
			.bitmapConfig(Bitmap.Config.RGB_565).build();
	/**
	 * 
	 * ��������ͷ��ͼƬ
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultUserHeaderoptions);
	}
	/**
	 * 
	 * ������ҵ����ͷ��ͼƬ
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetCompanyHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultCompanyHeaderoptions);
	}
	/**
	 * 
	 * ����Ⱥ������ͷ��ͼƬ
	 * 
	 * @param imageView
	 * @param URL
	 */
	public static void displayNetGroupHeaderImage(ImageView imageView, String url) {
		displayNetImage(imageView, url, defaultCompanyHeaderoptions);
	}
	/**
	 * ��������ͼƬ
	 * 
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView);
	}
	/**
	 * ��������ͼƬ
	 * ���Զ�����ʾͼƬ����Ŀ��ͼƬΪ�յ�ʱ����ʾ��ͼƬ���ص�ʱ����ʾ
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options) {
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	/**
	 * ��������ͼƬ
	 * ���Զ�����ʾͼƬ����Ŀ��ͼƬΪ�յ�ʱ����ʾ��ͼƬ���ص�ʱ����ʾ,������ͼƬ���صĹ���
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options, 
			ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(url, imageView, options, listener);
	}
	/**
	 * ��������ͼƬ
	 * ���Զ�����ʾͼƬ����Ŀ��ͼƬΪ�յ�ʱ����ʾ��ͼƬ���ص�ʱ����ʾ,������ͼƬ���صĹ��̺�ͼƬ���صĽ���
	 * @param imageView
	 * @param URL
	 */
	private static void displayNetImage(ImageView imageView, String url,DisplayImageOptions options, 
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		ImageLoader.getInstance().displayImage(url, imageView, options, listener, progressListener);
	}
	/**
	 * ���ر���sd ������Ƭ(ԭͼ)
	 * @param imageView
	 * @param imagePath
	 */
	private static void displayDiskImage(ImageView imageView, String imagePath) {
		String imageUrl = Scheme.FILE.wrap(imagePath);
		ImageLoader.getInstance().displayImage(imageUrl, imageView);//·����Ҫ�Ǹ��ı��� ���Ǽ��ر��ص�����, ����������
	}
	/**
	 * ���ر���sd ������Ƭ(����ͼ)
	 * @param imageView
	 * @param imagePath
	 */
	private static void displayDiskImage200(ImageView imageView, String imagePath) {
		imageView.setImageBitmap(BitmapUtils.getSmallBitmap(imagePath, 100, 100));
	}
	/**
	 * ��ʾȫͼ
	 * @param imageView
	 * @param imagePath
	 */
	public static void displayImage(ImageView imageView, String imagePath){
		if(TextUtils.isEmpty(imagePath)){
			return ;
		}
		File file =new File(imagePath);
		//����ͼƬ
		if(file.exists()){
			displayDiskImage(imageView, imagePath);
		//����ͼƬ
		}else{
			displayNetImage(imageView, imagePath);
		}
	}
	/**
	 * ��ʾ����ͼ
	 * @param imageView
	 * @param imagePath
	 */
	public static void displayImage200(ImageView imageView, String imagePath){
		if(TextUtils.isEmpty(imagePath)){
			return ;
		}
		File file =new File(imagePath);
		//����ͼƬ
		if(file.exists()){
			displayDiskImage200(imageView, imagePath);
		//����ͼƬ
		}else{
           displayNetImage(imageView,imageUrlConvert200ImageUrl(imagePath));
		}
	}
	
	 /**
		 * �Ѵ�ͼURL��ַת��Ϊ200������ͼ��ַ���ͷ�����Լ���õģ�
		 * @param destUrl
		 * @return
		 */
		public static String imageUrlConvert200ImageUrl(String destUrl){
			int fileFormat = destUrl.lastIndexOf(".");
			String fileUrl =destUrl + "_200."+ destUrl.substring(fileFormat + 1, destUrl.length());
			return fileUrl;
		}
	/**
	 * �첽��ȡͼƬ
	 * @param uri
	 * @return
	 */
	public static Bitmap loadImageSync(String uri){
		return ImageLoader.getInstance().loadImageSync(uri);
	}
	/**
	 * �첽��ȡͼƬ
	 * @param uri
	 * @return
	 */
	public static Bitmap loadHeaderImageSync(String uri){
		return ImageLoader.getInstance().loadImageSync(uri,defaultUserHeaderoptions);
	}
	/**
	 * ����ͼƬ�洢
	 * @param uri
	 * @return  ,���û��bitmap , ����·��Ҳ����;
	 */
	public static void storeImageToCache(String serverImageUrl,String localFileUrl){
		try {
			ImageLoader.getInstance().getDiskCache().save(serverImageUrl, BitmapUtils.decodeFile(new File(localFileUrl)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ����ͼƬ�洢
	 * @param uri
	 * @return   ImageLoader�У�����ͨ��getDiskCache().get(url)��ȡ�����̻���  , save �洢 ����
	 */
	public static void storeImageToCache(String serverImageUrl,Bitmap bitmap){
		try {
			ImageLoader.getInstance().getDiskCache().save(serverImageUrl, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
}
}

