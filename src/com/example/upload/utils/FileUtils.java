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
 * �ļ�����������
 * @author oceangray
 *
 */
@SuppressLint("NewApi")
public class FileUtils {
	/**��Ŀ¼**/
	public static final String ROOT="/shishi";
	/**��־�ļ�Ŀ¼**/
	public static final String LOG="/log";
	/**ͼƬ�ļ�Ŀ¼**/
	public static final String IMAGE="/image";
	/**����APK��ŵ�Ŀ¼**/
	public static final String UPDATE_APK="/apks";
	/**��Ƶ�ļ�Ŀ¼**/
	public static final String AUDIO="/audio";
	/**��Ƶ�ļ�Ŀ¼**/
	public static final String VIDEO="/video";
	/**��ʱ�ļ�Ŀ¼**/
	public static final String TEMP="/temp";
	/**�ĵ�Ŀ¼**/
	public static final String WORD="/word";
	/**��־��ʽ**/
	public static final byte FILE_TYPE_LOG=0X00;
	/**ͼƬ��ʽ**/
	public static final byte FILE_TYPE_IMAGE=0X01;
	/**��Ƶ��ʽ**/
	public static final byte FILE_TYPE_AUDIO=0X02;
	/**��Ƶ��ʽ**/
	public static final byte FILE_TYPE_VIDEO=0X09;
	/**��ʱ�ļ�**/
	public static final byte FILE_TYPE_TEMP=0X03;
	/**WORD�ļ�**/
	public static final byte FILE_TYPE_WORD=0X04;
	/**TXT�ļ�**/
	public static final byte FILE_TYPE_TXT=0X05;
	/**PDF�ļ�**/
	public static final byte FILE_TYPE_PDF=0X06;
	/**EXCEL�ļ�**/
	public static final byte FILE_TYPE_EXCEL=0X07;
	/**PPT�ļ�**/
	public static final byte FILE_TYPE_PPT=0X08;
	/**�ļ�ѹ���������**/
	public static final int FILE_MAX_QUALITY=100;
	/**�ļ�ѹ����С����**/
	public static final int FILE_MIN_QUALITY=0;
	/**
	 * �Ƿ��עSD��
	 * 
	 * @return true �� ; false: ��
	 */
	public static boolean hasMountSDCard(){
		String state=Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state) || !Environment.isExternalStorageRemovable()){
			return true;
		}
		return false;
	}
	/**
	 * ��ȡ�ļ���ŵĸ�Ŀ¼
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
	 * ��ȡ��־��ŵ�ַ
	 * @param context
	 * @return
	 */
	public static String getLogDir(Context context){
		return getDir(context, LOG);
	}
	/**
	 * ��ȡͼƬ��ַ
	 * @param context
	 * @return
	 */
	public static String getImageDir(Context context){
		return getDir(context, IMAGE);
	}
	/**
	 * apk ��ŵ�ַ
	 * @param context
	 * @return
	 */
	public static String getApksDir(Context context){
		return getDir(context, UPDATE_APK);
	}
	/**
	 * ¼���ļ���ַ
	 * @param context
	 * @return
	 */
	public static String getAudioDir(Context context){
		return getDir(context, AUDIO);
	}
	/**
	 * �ĵ���ַ
	 * @param context
	 * @return
	 */
	public static String getWordDir(Context context){
		return getDir(context, WORD);
	}
	/**
	 * ��ʱ�ļ���ַ
	 * @param context
	 * @return
	 */
	public static String getTempDir(Context context){
		return getDir(context, TEMP);
	}
	/**
	 * ��ȡָ���ļ����͵Ĵ�ŵ�ַ
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
	 * �����ļ���
	 * @param fileDir
	 * @return true :�ļ��д����ɹ�;false:�ļ��д���ʧ��
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
	 * �����ļ�
	 * @param context
	 * @param fileType �ļ�����  {@code FILE_TYPE_AUDIO,FILE_TYPE_LOG,FILE_TYPE_IMAGE��FILE_TYPE_TEMP }
	 * @return  ���ݵ�ǰʱ��
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
				//ͼƬλ��, + ��ǰʱ��+	 /** ͼƬ���� **/FILE_MIME_IMAGE = ".jpg";
			case FILE_TYPE_IMAGE: //getImageDir �����˷�װ , �����ļ���, ָ�����λ��;
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
				throw new IllegalArgumentException("���Ϸ����ļ����Ͳ�����"+fileType);
		}
		return fileName;
	}
	/**
	 * �����ļ�
	 * @param context
	 */
	public static void clear(Context context){
		deleteDirectory(getRootDir(context));
	}
	/**
	 * ɾ���ļ�
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
	 * ��ȡ�ļ�����
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
	 * ��ͼƬ����д�뵽���̣��������ļ�·��
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
     * ͨ��url��ȡ�ļ���λ��
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
     * ���ɻ���key
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
     * �ֽ�����ת�����ַ���
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
     * �ַ�����ת�����ļ�
     * 
     * @param bytes
     *            ����������
     * @param file
     *            �ļ�
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
     * �Ƿ�Ϊ��ͬ�ļ�(ֻ�ǱȽ���һ���ļ���С)
     * 
     * @param file
     *            �ļ���
     * @param size
     *            ��һ���ļ���С
     * @return
     */
    public static boolean isSameFile(File file, long size) {
	boolean isSameFile = false;
	if (file.exists()) {
	    try {
		FileInputStream fis = new FileInputStream(file);
		long tmpSize = fis.available();// ��ȡ�ļ�����
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
	 * ��ȡѡ��ͼƬ·��
	 * 
	 * @param m_activity
	 * @param data
	 * @return
	 */
	public static Uri getPickPhotoUri(Activity context, Intent data) {
		Uri uri = data.getData();
		String filePath = getPath(context, uri);
		//BitmapUtils.compressBitmap(filePath, context);
		return Uri.fromFile(new File(filePath)); //�ļ���Example: "file:///tmp/android.txt" uri
	}

	/**
	 * ��ȡͼƬ·��
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
	 * �Ƿ�Ϊexternalstorage.documents
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}
	/**
	 * ��ȡͼƬ·��
	 * 
	 * @param context
	 * @param uri
	 * @param selection
	 *            ��ѯ����
	 * @param selectionArgs
	 *            ��ѯ����
	 * @return ͼƬ·��
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
	 * �Ƿ�Ϊdownloads.documents
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * �Ƿ�ΪMediaDocument
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * �Ƿ�Ϊgoogle photo
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
	/**
	 * ��ȡ�ļ�����
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
	 * ��ȡ�ļ�����
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
	 * ��ȡ�ļ�����
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
	 * ��ȡ�ļ���С
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
	 * ��ȡ��Ƶ��һ���ͼƬ
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




















