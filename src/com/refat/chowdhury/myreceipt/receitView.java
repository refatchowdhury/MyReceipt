package com.refat.chowdhury.myreceipt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class receitView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receit_view);
		String receiptName=getIntent().getExtras().getString("receiptName");
		//Log.i("receipt name check:","receitLocation:"+LSQLView.receitLocation+"receiptName"+receiptName);
		
		ImageView receitImView=(ImageView)findViewById(R.id.receitView);
		
		String sdcard_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Recipt_image";
		File cacheFile =new File (sdcard_path,receiptName);
		
		try{
				InputStream fileInputStream = new FileInputStream(cacheFile);					
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
					//bitmapOptions.inSampleSize = scale;
				bitmapOptions.inJustDecodeBounds = false;
				Bitmap receitBitmap = BitmapFactory.decodeStream(fileInputStream, null, bitmapOptions);
				receitImView.setImageBitmap(receitBitmap);
			}catch(FileNotFoundException e){ Toast.makeText(this, "Sorry for the inconvenience.", Toast.LENGTH_LONG).show();}
	   
	}

	
}
