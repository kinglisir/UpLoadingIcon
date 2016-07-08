package com.example.upload;
import java.io.File;

import com.example.upload.clip.PhotoClipperActivity;
import com.example.upload.imageloader.ImageLoaderHelper;
import com.example.upload.utils.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	PopupWindow mPop;
	/**����***/
	public final static int REQUEST_TAKE_PHOTO = 101;
	/**�����ѡ����Ƭ***/
	public final static int REQUEST_SELECT_PICTURE = 100;
	/**�ü�ҳ�� ��������**/
	public static final int REQUEST_CLIPPER = 1;
	protected static Uri mPhotoUri;//����ͼƬ·��
	private ImageView iv_icon;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.iv_icon).setOnClickListener(this);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_icon:
			View view = View.inflate(MainActivity.this, R.layout.selectpicture, null);
			
			mPop = new PopupWindow(view, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
			mPop.setOutsideTouchable(true);
			mPop.setFocusable(true);
			mPop.setBackgroundDrawable(new ColorDrawable(0));
			mPop.setAnimationStyle(R.style.anim_popupwindows);
			mPop.showAtLocation(view.findViewById(R.id.layout_root),Gravity.CENTER, 0, 0);
			
			view.findViewById(R.id.tv_take_photo).setOnClickListener(this);;
			view.findViewById(R.id.tv_select_photo).setOnClickListener(this);;
			view.findViewById(R.id.txt_cancel).setOnClickListener(this);;
			view.findViewById(R.id.outArea).setOnClickListener(this);
			break;

		case R.id.tv_take_photo:
			//������������
			mPhotoUri = Uri.fromFile(new File(FileUtils.createFile(this, FileUtils.FILE_TYPE_IMAGE)));
			// ��� ��ȡͼƬ  mPhotoUri : file:///storage/emulated/0/shishi/image/1460974445049.jpg
    		switchToCapturePhoto(mPhotoUri,REQUEST_TAKE_PHOTO);
			break;
		case R.id.tv_select_photo:
			switchToPickPhoto(REQUEST_SELECT_PICTURE);
			break;
		case R.id.txt_cancel:
			mPop.dismiss();
			break;
		case R.id.outArea:
			mPop.dismiss();
			break;
		}
		
	}
	
	/**
	 * �ֻ����ѡ��ͼƬ
	 * 
	 * @param activity
	 * @param requestCode
	 * @author zkx
	 */
	private  void switchToPickPhoto(int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		startActivityForResult(intent, requestCode);
	}

	/**
	 * �����ȡͼƬ
	 * @param activity
	 * @param uri
	 * @param requestCode
	 */
	private  void switchToCapturePhoto(Uri uri,int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 != RESULT_OK) {
			return;
		}
		
		
		//����requestcode�ж������� ���� ��  ����  �����ѡ�񷵻ص�
		switch (arg0) {
		case REQUEST_TAKE_PHOTO://���ջ�ȡͼƬ
			if (mPhotoUri != null) {
				String fileDir = mPhotoUri.getPath();
				Log.e("fileDir_photo", fileDir);
				switchToClip(fileDir);
			 }
			break;
			
		case REQUEST_SELECT_PICTURE://�����ѡ��ͼƬ
			 if (arg2 != null) { 
					Uri uri = FileUtils.getPickPhotoUri(this, arg2); // תuri��ʽ
					String fileDir = uri.getPath();// 
					Log.e("fileDir_picture", fileDir);
					switchToClip(fileDir);
				 }
			break;
			
		case REQUEST_CLIPPER://�ü�ҳ��
			 
			String filePath = arg2.getStringExtra("filePath");  ///��������������
			Toast.makeText(getApplicationContext(),filePath, 0).show();
			if (filePath != null && filePath.length() > 0) {
				ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
				ImageLoaderHelper.displayImage(iv_icon, filePath);
			}
			
			break;

		default:
			break;
		}
	}
	
	/**
	 * ��ת���ü�ҳ��
	 * @param filePath
	 */
	public void switchToClip(String filePath){ //  ������
		//��ת��ϵͳ�ü�����
		/*Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, REQUEST_CLIPPER);*/
		
		//��ת���Զ���Ĳü�����
		Intent intent=new Intent(this, PhotoClipperActivity.class);
		intent.putExtra("filePath", filePath);
		startActivityForResult(intent, REQUEST_CLIPPER);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mPop.dismiss();
	}
}
