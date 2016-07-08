package com.example.upload.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;





import com.example.upload.app.UploadApplication;
import com.example.upload.constance.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 文件操作工具类
 * @author oceangray
 *
 */
@SuppressLint("NewApi")
public class FileUtils {
	/**根目录**/
	public static final String ROOT="/shishi";
	/**日志文件目录**/
	public static final String LOG="/log";
	/**图片文件目录**/
	public static final String IMAGE="/image";
	/**更新APK存放的目录**/
	public static final String UPDATE_APK="/apks";
	/**音频文件目录**/
	public static final String AUDIO="/audio";
	/**视频文件目录**/
	public static final String VIDEO="/video";
	/**临时文件目录**/
	public static final String TEMP="/temp";
	/**文档目录**/
	public static final String WORD="/word";
	/**日志格式**/
	public static final byte FILE_TYPE_LOG=0X00;
	/**图片格式**/
	public static final byte FILE_TYPE_IMAGE=0X01;
	/**音频格式**/
	public static final byte FILE_TYPE_AUDIO=0X02;
	/**视频格式**/
	public static final byte FILE_TYPE_VIDEO=0X09;
	/**临时文件**/
	public static final byte FILE_TYPE_TEMP=0X03;
	/**WORD文件**/
	public static final byte FILE_TYPE_WORD=0X04;
	/**TXT文件**/
	public static final byte FILE_TYPE_TXT=0X05;
	/**PDF文件**/
	public static final byte FILE_TYPE_PDF=0X06;
	/**EXCEL文件**/
	public static final byte FILE_TYPE_EXCEL=0X07;
	/**PPT文件**/
	public static final byte FILE_TYPE_PPT=0X08;
	/**文件压缩最大质量**/
	public static final int FILE_MAX_QUALITY=100;
	/**文件压缩最小质量**/
	public static final int FILE_MIN_QUALITY=0;
	/**
	 * 是否关注SD卡
	 * 
	 * @return true 是 ; false: 否
	 */
	public static boolean hasMountSDCard(){
		String state=Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state) || !Environment.isExternalStorageRemovable()){
			return true;
		}
		return false;
	}
	/**
	 * 获取文件存放的根目录
	 * @param context
	 * @return
	 */
	public static String getRootDir(Context context){
		if(hasMountSDCard()){
			return Environment.getExternalStorageDirectory().getAbsolutePath()+ROOT;
		} else {
			return context.getFilesDir().getAbsolutePath()+ROOT;
		}
	}
	/**
	 * 获取日志存放地址
	 * @param context
	 * @return
	 */
	public static String getLogDir(Context context){
		return getDir(context, LOG);
	}
	/**
	 * 获取图片地址
	 * @param context
	 * @return
	 */
	public static String getImageDir(Context context){
		return getDir(context, IMAGE);
	}
	/**
	 * apk 存放地址
	 * @param context
	 * @return
	 */
	public static String getApksDir(Context context){
		return getDir(context, UPDATE_APK);
	}
	/**
	 * 录音文件地址
	 * @param context
	 * @return
	 */
	public static String getAudioDir(Context context){
		return getDir(context, AUDIO);
	}
	/**
	 * 文档地址
	 * @param context
	 * @return
	 */
	public static String getWordDir(Context context){
		return getDir(context, WORD);
	}
	/**
	 * 临时文件地址
	 * @param context
	 * @return
	 */
	public static String getTempDir(Context context){
		return getDir(context, TEMP);
	}
	/**
	 * 获取指定文件类型的存放地址
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getDir(Context context ,String dir){
		String destDir=getRootDir(context)+dir+File.separator;
		createDirectory(destDir);
		return destDir;
	}
	/**
	 * 创建文件夹
	 * @param fileDir
	 * @return true :文件夹创建成功;false:文件夹创建失败
	 */
	public static boolean createDirectory(String fileDir){
		if(fileDir == null){
			return false;
		}
		File file=new File(fileDir);
		if(file.exists()){
			return true;
		}
		return file.mkdirs();
	}
	/**
	 * 创建文件
	 * @param context
	 * @param fileType 文件类型  {@code FILE_TYPE_AUDIO,FILE_TYPE_LOG,FILE_TYPE_IMAGE，FILE_TYPE_TEMP }
	 * @return  根据当前时间
	 */
	public static String createFile(Context context, int fileType){
		final String fileName;	
		switch(fileType){
			case FILE_TYPE_LOG:
				fileName=getLogDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_LOG;
				break;
			case FILE_TYPE_AUDIO:
				fileName=getAudioDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_AUDIO;
				break;
				//图片位置, + 当前时间+	 /** 图片类型 **/FILE_MIME_IMAGE = ".jpg";
			case FILE_TYPE_IMAGE: //getImageDir 进行了封装 , 创建文件夹, 指定存放位置;
				fileName=getImageDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_IMAGE;
				break;
			case FILE_TYPE_TEMP:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_TEMP;
				break;
			case FILE_TYPE_WORD:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_WORD;
				break;
			case FILE_TYPE_TXT:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_TXT;
				break;
			case FILE_TYPE_PDF:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_PDF;
				break;
			case FILE_TYPE_EXCEL:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_EXCEL;
				break;
			case FILE_TYPE_PPT:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_PPT;
				break;
			case FILE_TYPE_VIDEO:
				fileName=getTempDir(context)+System.currentTimeMillis()+FileTypes.FILE_MIME_MP4;
				break;
			default:
				throw new IllegalArgumentException("不合法的文件类型参数："+fileType);
		}
		return fileName;
	}
	/**
	 * 清理文件
	 * @param context
	 */
	public static void clear(Context context){
		deleteDirectory(getRootDir(context));
	}
	/**
	 * 删除文件
	 * @param fileDir
	 */
	public static boolean deleteDirectory(String fileDir){
		if(fileDir==null){
			return false;
		}
		File file=new File(fileDir);
		if(file ==null || !file.exists()){
			return false;
		}
		if(file.isDirectory()){
			File[] files=file.listFiles();
			for(int i=0; i<files.length;i++){
				if(files[i].isDirectory()){
					deleteDirectory(files[i].getAbsolutePath());
				}else{
					files[i].delete();
				}
			}
		}
		file.delete();
		return true;
	}
	/**
	 * 获取文件名称
	 * @param fileName
	 * @return
	 */
	public static String getSimpleName(String fileName){
		final int index=fileName.lastIndexOf("/");
		if(index ==-1){
			return fileName;
		}else{
			return fileName.substring(index+1);
		}
	}
	/**
	 * 把图片数据写入到磁盘，并返回文件路径
	 * @param bitmap
	 * @param context
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String writeBitmap(Bitmap bitmap,Context context) throws FileNotFoundException{
		String filePath=createFile(context, FILE_TYPE_IMAGE);
		FileOutputStream fos=null;
		fos=new FileOutputStream(new File(filePath));
		bitmap.compress(Bitmap.CompressFormat.JPEG, FILE_MAX_QUALITY, fos);
		return filePath;
	}
	 /**
     * 通过url获取文件的位置
     * 
     * @param context
     * @param url
     * @return
     */
    public static String getPathFromUrl(Context context, String url, int fileType) {
	String fileName;
	File file =new File(url);
	if (!file.exists()) {
	    switch (fileType) {
	    case FILE_TYPE_AUDIO: {
		fileName = getAudioDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_AUDIO;
		break;
	    }
	    case FILE_TYPE_LOG: {
		fileName = getLogDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_LOG;
		break;
	    }
	    case FILE_TYPE_IMAGE: {
		fileName = getImageDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_IMAGE;
		break;
	    }
	    case FILE_TYPE_TEMP: {
		fileName = getImageDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_TEMP;
		break;
	    }
	    case FILE_TYPE_WORD: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_WORD;
			break;
	    case FILE_TYPE_TXT: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_TXT;
			break;
	    case FILE_TYPE_PDF: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_PDF;
			break;
	    case FILE_TYPE_EXCEL: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_EXCEL;
			break;
	    case FILE_TYPE_PPT: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_PPT;
			break;
	    case FILE_TYPE_VIDEO: 
			fileName = getWordDir(context) + FileUtils.hashKeyForDisk(url)+ FileProperties.FILE_MIME_MP4;
			break;
	    default: {
		throw new IllegalArgumentException("Unsupported file type: "
			+ fileType);
	    }
	    }
	}else{
		fileName = url;
	}
	return fileName;
    }
    /**
     * 生成缓存key
     * 
     * @param key
     * @return
     */
    public static String hashKeyForDisk(String key) {
	String cacheKey;
	try {
	    final MessageDigest mDigest = MessageDigest.getInstance("MD5");
	    mDigest.update(key.getBytes());
	    cacheKey = bytesToHexString(mDigest.digest());
	} catch (NoSuchAlgorithmException e) {
	    cacheKey = String.valueOf(key.hashCode());
	}
	return cacheKey;
    }
    /**
     * 字节数组转换成字符串
     * 
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < bytes.length; i++) {
	    String hex = Integer.toHexString(0xFF & bytes[i]);
	    if (hex.length() == 1) {
		sb.append('0');
	    }
	    sb.append(hex);
	}
	return sb.toString();
    }
    /**
     * 字符数组转换成文件
     * 
     * @param bytes
     *            二进制数组
     * @param file
     *            文件
     * @return
     * @throws IOException
     */
    public static File bytesToFile(byte[] bytes, File file) throws IOException {
		String tempStr = FileUtils.createFile(UploadApplication.getApplication(), FileUtils.FILE_TYPE_TEMP);
		File tempFile = new File(tempStr);
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(bytes, 0, bytes.length);
		fos.flush();
		fos.close();
		tempFile.renameTo(file);
		return file;
    }
    /**
     * 是否为相同文件(只是比较另一个文件大小)
     * 
     * @param file
     *            文件名
     * @param size
     *            另一个文件大小
     * @return
     */
    public static boolean isSameFile(File file, long size) {
	boolean isSameFile = false;
	if (file.exists()) {
	    try {
		FileInputStream fis = new FileInputStream(file);
		long tmpSize = fis.available();// 读取文件长度
		if (tmpSize == size) {
		    isSameFile = true;
		} else {
		    file.delete();
		}
		fis.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return isSameFile;
    }
    /**
	 * 获取选择图片路径
	 * 
	 * @param m_activity
	 * @param data
	 * @return
	 */
	public static Uri getPickPhotoUri(Activity context, Intent data) {
		Uri uri = data.getData();
		String filePath = getPath(context, uri);
		//BitmapUtils.compressBitmap(filePath, context);
		return Uri.fromFile(new File(filePath)); //文件的Example: "file:///tmp/android.txt" uri
	}

	/**
	 * 获取图片路径
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		// Build.VERSION_CODES.KITKAT
		final boolean isKitKat = Build.VERSION.SDK_INT >= 19;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri)) {
				return uri.getLastPathSegment();
			}

			return getDataColumn(context, uri, null, null);
		}// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
	/**
	 * 是否为externalstorage.documents
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	/**
	 * 获取图片路径
	 * 
	 * @param context
	 * @param uri
	 * @param selection
	 *            查询参数
	 * @param selectionArgs
	 *            查询条件
	 * @return 图片路径
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = MediaStore.Images.Media.DATA;
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
	/**
	 * 是否为downloads.documents
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * 是否为MediaDocument
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * 是否为google photo
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	/**
	 * 获取文件类型
	 * @return
	 */
	public static String getFileType(File file){
		String result=null;
		String fileName=file.getName();
		if(fileName.lastIndexOf(".")!=-1){
			result=fileName.substring(fileName.lastIndexOf(".")+1);
			if(result==null){
				result=Constants.FileType.other;
				return result;
			}
			if("txt".equals(result.toLowerCase())){
				result=Constants.FileType.txt;
			}else if("doc".equals(result.toLowerCase())||"docx".equals(result.toLowerCase())){
				result=Constants.FileType.word;
			}else if("pdf".equals(result.toLowerCase())){
				result=Constants.FileType.pdf;
			}else if("xlsx".equals(result.toLowerCase())){
				result=Constants.FileType.excle;
			}else if("rar".equals(result.toLowerCase())||"zip".equals(result.toLowerCase())){
				result=Constants.FileType.rar_or_zip;
			}else if("xls".equals(result.toLowerCase())||"xlsx".equals(result.toLowerCase())){
				result=Constants.FileType.excel;
			}else if("ppt".equals(result.toLowerCase())){
				result=Constants.FileType.ppt;
			}else if("pdf".equals(result.toLowerCase())){
				result=Constants.FileType.pdf;
			}else if("png".equals(result.toLowerCase())||"gif".equals(result.toLowerCase())||"jpg".equals(result.toLowerCase())){
				result=Constants.FileType.image;
			}else {
				result=Constants.FileType.other;
			}
		}else{
			result=Constants.FileType.other;
		}
		return result;
	}
	/**
	 * 获取文件名称
	 * @return
	 */
	public static String getFileName(String filePath){
		if(TextUtils.isEmpty(filePath)){
			return null;
		}
		File file=new File(filePath);
		return file.getName();
	}
	/**
	 * 获取文件类型
	 * @return
	 */
	public static String getFileType(String fileName){
		String result=null;
		if(TextUtils.isEmpty(fileName)){
			return null;
		}
		if(fileName.lastIndexOf(".")!=-1){
			result=fileName.substring(fileName.lastIndexOf(".")+1);
			if(result==null){
				result=Constants.FileType.other;
				return result;
			}
			if("txt".equals(result.toLowerCase())){
				result=Constants.FileType.txt;
			}else if("doc".equals(result.toLowerCase())||"docx".equals(result.toLowerCase())){
				result=Constants.FileType.word;
			}else if("pdf".equals(result.toLowerCase())){
				result=Constants.FileType.pdf;
			}else if("xlsx".equals(result.toLowerCase())){
				result=Constants.FileType.excle;
			}else if("rar".equals(result.toLowerCase())||"zip".equals(result.toLowerCase())){
				result=Constants.FileType.rar_or_zip;
			}else if("xls".equals(result.toLowerCase())||"xlsx".equals(result.toLowerCase())){
				result=Constants.FileType.excel;
			}else if("ppt".equals(result.toLowerCase())){
				result=Constants.FileType.ppt;
			}else if("pdf".equals(result.toLowerCase())){
				result=Constants.FileType.pdf;
			}else if("png".equals(result.toLowerCase())||"gif".equals(result.toLowerCase())||"jpg".equals(result.toLowerCase())){
				result=Constants.FileType.image;
			}else {
				result=Constants.FileType.other;
			}
		}else{
			result=Constants.FileType.other;
		}
		return result;
	}
	/**
	 * 获取文件大小
	 * @param file
	 * @return
	 */
	public static  String getFileSize(File file){
		String result="";
		if(file.exists()&&file.isFile()){
			double length=file.length();
			double klength=length/1024d;
			DecimalFormat    df   = new DecimalFormat("######0.00");   
			if(klength<1024){
				result=df.format(klength)+"k";
			}else{
				double mlength=klength/1024d;
				if(mlength<1024){
					result=df.format(mlength)+"M";
				}else{
					double glength=mlength/1024d;
					result=df.format(glength)+"G";
				}
			}
		}
		return  result;
	}
	/**
	 * 获取视频第一贞的图片
	 * @param videoPath
	 * @param context
	 * @return
	 */
	public static String getVideoImageOfFirstFrame(String videoPath,Context context) {
		MediaMetadataRetriever media = new MediaMetadataRetriever();
		media.setDataSource(videoPath);
		try {
			return writeBitmap(media.getFrameAtTime(), context);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}




















