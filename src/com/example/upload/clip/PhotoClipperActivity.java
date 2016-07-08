package com.example.upload.clip;

import java.io.FileNotFoundException;

import com.example.upload.R;
import com.example.upload.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;



/**
 * @Description —°‘Ò’’∆¨≤¢≤√ºÙ
 */
public class PhotoClipperActivity extends Activity implements OnClickListener{
	private ClipImageLayout  clip_image_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_clipper);
		findViewById(R.id.bt_save).setOnClickListener(this);
		findViewById(R.id.bt_cancle).setOnClickListener(this);
		clip_image_layout=(ClipImageLayout) findViewById(R.id.clip_image_layout);
		Log.e("filePath", getIntent().getStringExtra("filePath"));
		clip_image_layout.setImagePath(getIntent().getStringExtra("filePath"));
	}
	/**
	 * Õº∆¨≤√ºÙ
	 */
	public void clipPhoto(){
		Bitmap bitmap = clip_image_layout.clip();
		String filePath=null;
		try {
			filePath = FileUtils.writeBitmap(bitmap, this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.putExtra("filePath", filePath);
		setResult(RESULT_OK, intent);
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_save:
			clipPhoto();
			break;
			
      case R.id.bt_cancle:
    	  finish();
			break;

		default:
			break;
		}
		
	}
}
