package com.refat.chowdhury.myreceipt;


import com.parse.Parse;
import com.parse.ParseAnalytics;


import com.parse.ParseObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
//import android.widget.ImageButton;
import android.widget.ImageView;
//import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	/*DisplayMetrics metrics = this.getResources().getDisplayMetrics();
	public int width = metrics.widthPixels;
	public int height = metrics.heightPixels;*/
	private static String uniqueID = null;
	private ImageButton ibCalender,ibCamera,ibUpdate,ibGraph,ibDelete,ibView,ibReport;
	EditText etDate,etSpent,etAmount;
	ImageView iv;
	TextView tvDFdate,tvDTdate;
	String Fdate;
	String Tdate;
	int calCall=0;
	
	private Calendar cal;
	private int day;
	private int month;
	private int year;
	DatePickerDialog dpd;
	
	private static 	 String CamFileName="";
	private static   String pdate="";
	private static   String shop_name="";
	private static   String amount="";
	
	
	
	private static boolean  cameraFlag=false;
	private static Bitmap thePic;
	
	LocationManager mLocationManager;
	LocationListener listener;
	Location location;
	double pLong,pLat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            uniqueID=uniqueID.replace("-", "_");
            uniqueID="A"+uniqueID;}
		Log.i("Unique ID:",""+uniqueID);
		//Parse.initialize(this, "HhyX8awMDDrkhxuVfUNC4h6G5Lf5LtxnnZCGIP3a", "I8HwbKPPjxQtutEdo5l2tvX5tQAXAx1u30eInV6f");
		Parse.initialize(this, "Dw2DuQCloTM6pPa7MM4vvnQkzfV1aSX7JKRE3CvV", "nVBftz0dKXDLdIWUUyAnrDKhfl3UwDk3vqxHBhGl");
		
	   
		
			//set widgets
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		dpd = new DatePickerDialog(this,new datePickerListener(), year, month, day);
		
		etDate = 	(EditText) findViewById(R.id.etDate);
		etSpent=	(EditText) findViewById(R.id.etSpentWhere);
		etAmount=	(EditText) findViewById(R.id.etAmount);
		etAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
		
		//setAmountFilter();
		ibCalender=	(ImageButton) findViewById(R.id.ibCalender);
		ibCamera=	(ImageButton) findViewById(R.id.ibCamera);
		ibUpdate=	(ImageButton) findViewById(R.id.ibUpdate);
		ibGraph=	(ImageButton) findViewById(R.id.ibGraph);
		ibDelete=	(ImageButton) findViewById(R.id.ibDelete);
		ibView=		(ImageButton) findViewById(R.id.ibView);
		ibReport=	(ImageButton) findViewById(R.id.ibReport);
		iv=(ImageView) findViewById(R.id.iv);
		//String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
		//String currentDateTimeString=getDate();
		// textView is the TextView view that should display it
		etDate.setText(getDate());
		etDate.setKeyListener(null);//making the date not editable
		etDate.setOnClickListener(this);
		ibCalender.setOnClickListener(this);
		ibCamera.setOnClickListener(this);
		ibUpdate.setOnClickListener(this);
		ibGraph.setOnClickListener(this);
		ibDelete.setOnClickListener(this);
		ibView.setOnClickListener(this);
		ibReport.setOnClickListener(this);
		iv.setImageResource(R.drawable.receipt_big);
		iv.setOnClickListener(this);
		
	}

	/* 
	 * private void setAmountFilter() {
		// TODO Auto-generated method stub
		 etAmount.setFilters(new InputFilter[] {
				 new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
				 int beforeDecimal = 5, afterDecimal = 2;
				  
				 @Override
				 public CharSequence filter(CharSequence source, int start, int end,
				 Spanned dest, int dstart, int dend) {
				 String temp = et.getText() + source.toString();
				  
				 if (temp.equals(".")) {
				 return "0.";
				 }
				 else if (temp.toString().indexOf(".") == -1) {
				 // no decimal point placed yet
				 if (temp.length() > beforeDecimal) {
				 return "";
				 }
				 } else {
				 temp = temp.substring(temp.indexOf(".") + 1);
				 if (temp.length() > afterDecimal) {
				 return "";
				 }
				 }
				  
				 return super.filter(source, start, end, dest, dstart, dend);
				 }
				 }
				 });
		
	}*/

	private String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
		
	}
	private String getDateAndTime() {
		//Calendar c = Calendar.getInstance();
		SimpleDateFormat dtFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HH-MM-ss", Locale.getDefault());
        Date date = new Date();
        return dtFormat.format(date);
		
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
		case R.id.ibUpdate:
			//check for size of the sd card for enough space
			//TO DO
			
			//get all the data
			//Read date
			 pdate =etDate.getText().toString();
			 shop_name =etSpent.getText().toString().toUpperCase(Locale.getDefault());
				Log.d("Variable check","shop name:"+shop_name);
			amount =etAmount.getText().toString();
			
			//Read shop name
			if (shop_name.length()==0){
				Toast.makeText(this, "Please enter where you spent the money", Toast.LENGTH_SHORT).show();
				etSpent.setError("Shop name is required!");
				break;
			}
			
			//Read Amount
			
			if (amount.length()==0){
				Toast.makeText(this, "Please enter how much you spent", Toast.LENGTH_SHORT).show();
				etAmount.setError("Amount is required!");
				break;
			}
			
			//check if the photo has already been taken;if taken assign the filename to receit.
			if (cameraFlag){
			 //receit =CamFileName;
			 saveImage(thePic);
			 //Toast.makeText(this, "CamFileName:"+CamFileName, Toast.LENGTH_SHORT).show();
			 saveData(pdate,shop_name,amount,CamFileName);
			 
			 cameraFlag=false;
			}
			//if (CamFileName.length()==0){
			else {
				//Put up the Yes/No message box
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder
					.setTitle("Capture your receit")
					.setMessage("Would you like to capture your receit?")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {			      	
							//Yes button clicked, do something
							Toast.makeText(MainActivity.this, "Please press camera button.",Toast.LENGTH_SHORT).show();
							
	    	    	
						}
					})
	    			.setNegativeButton("No", new DialogInterface.OnClickListener() {
	    					public void onClick(DialogInterface dialog, int which) {			      	
	    	    	//Yes button clicked, do something
	    	    	Toast.makeText(MainActivity.this, "No receit for this purchase!",Toast.LENGTH_SHORT).show();
	    	    	//cameraFlag=true;
	    	    	saveData(pdate,shop_name,amount,"No Image");
	    					}
	    			})	
	    			.show();
			}
			
			
			break;
		case R.id.ibView:
			
			/*Normally when we launch new activity, it’s previous activities will be kept in a queue
			 *  like a stack of activities. So if you want to kill all the previous activities, just 
			 *  follow these methods.
			 * Intent i = new Intent(OldActivity.this, NewActivity.class);
				// set the new task and clear flags
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
					startActivity(i); 
			 */
			//Intent i=new Intent ("com.refat.myexpense.SQLVIEW");
			//startActivity(i);
			Toast.makeText(this, "Press on a record to see the receipt.", Toast.LENGTH_LONG).show();
			Intent l=new Intent (getApplicationContext(),LSQLView.class);
			//l.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(l);
			break;
		case R.id.ibCalender:
			//didDateChanged=true;
			calCall=0;
			//showDialog(0);
			
			dpd.show();
			break;
		case R.id.etDate:
			
			calCall=0;
			
			dpd.show();
			break;
		case R.id.ibCamera:
			cameraFlag=true;
			try{
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
						
			/*create instance of File with name img.jpg*/
			//tempCamFileName=getDateAndTime()+".jpg";
			File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
			
			//CamFileName=Environment.getExternalStorageDirectory()+File.separator + tempCamFileName;
			
			/*put uri as extra in intent object*/
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			/*start activity for result pass intent as argument and request code */
			startActivityForResult(intent, 1);
			}catch(ActivityNotFoundException anfe){
				//display an error message
			    String errorMessage = "Whoops - your device doesn't support capturing images!";
			    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			    toast.show();
			}
			break;
		case R.id.iv:
			/*iv.setClickable(true);
			iv.playSoundEffect(SoundEffectConstants.CLICK);
			Toast.makeText(this, "No gallery at the moment", Toast.LENGTH_SHORT).show();
			Intent j=new Intent ("com.refat.myexpense.SQLVIEW");
			startActivity(j);*/
			break;
		case R.id.ibReport:
			pickDateDialog();
			//Toast.makeText(this, "Please enter dates to see the report.", Toast.LENGTH_LONG).show();
			
			break;
		case R.id.ibDelete:
			Toast.makeText(getApplicationContext(), "Please press on a record to delete it.", Toast.LENGTH_LONG).show();
			Intent del=new Intent (getApplicationContext(),deleteRecord.class);
			startActivity(del);
			break;
			
		case R.id.ibGraph:
			Intent graph=new Intent (getApplicationContext(),GraphView1.class);
			startActivity(graph);
			
			
			
			break;
			
		
		}
		
	}
		
	private void saveData(String pdate,String shop_name,String amount,String receit){
		boolean didItWork=true;
		//if (shop_name.length()>0 && amount.length()>0&&cameraFlag){
			
			try{
						
				dataAdapter entry=new dataAdapter(MainActivity.this);
				entry.open();
				
				/*if(!didDateChanged){
					entry.createEntry(getDate(),shop_name,amount,receit);
					}
				else*/
					entry.createEntry(pdate,shop_name,amount,receit);
					ParseObject testObject = new ParseObject(uniqueID);
					 testObject.put("Date",pdate);
					 testObject.put("Store", shop_name);
					 testObject.put("Amount", amount);
					 testObject.saveInBackground();
		
				entry.close();
				
				etDate.setText(getDate());
				etSpent.setText("");
				etAmount.setText("");
				iv.setImageResource(R.drawable.receipt_big);
			}catch(Exception e){
				
				didItWork=false;
				String error=e.toString();
				Dialog d=new Dialog(this);
				d.setTitle("ERROR");
				TextView tv=new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			}finally{
				if (didItWork){
					
					Toast.makeText(getApplicationContext(), "Record was saved successfully",Toast.LENGTH_LONG).show();
				
				}
			}
		//}
			
			//save data in the cloud
		/*try{
			ParseObject Object = new ParseObject( uniqueID);
			
				Object.put("Date",pdate);
				Object.put("Store",shop_name);
				Object.put("Amount",amount);
				Object.put("Latitude",Double.toString(pLat));
				Object.put("Longitude", Double.toString(pLong));
				Object.saveInBackground();
		}catch(Exception e){e.printStackTrace();}*/
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==RESULT_OK){
		//if request code is same we pass as argument in startActivityForResult
		if(requestCode==1){
			//create instance of File with same name we created before to get image from storage
			File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
			
			//Crop the captured image using an other intent
			try {
				/*the user's device may not support cropping*/
				cropCapturedImage(Uri.fromFile(file));
			}
			catch(ActivityNotFoundException aNFE){
				//display an error message if user device doesn't support
				String errorMessage = "Sorry - your device doesn't support the crop action!";
				Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		if(requestCode==2){
			//Create an instance of bundle and get the returned data
			Bundle extras = data.getExtras();
			//get the cropped bitmap from extras
			thePic = extras.getParcelable("data");
			//thePic = Bitmap.createScaledBitmap(thePic,100, 100, false);
			 //save the image in a folder
			//saveImage(thePic);
			//set image bitmap to image view
		    Toast.makeText(this, "Press the add button to save the image", Toast.LENGTH_LONG).show();
			iv.setImageBitmap(thePic);
		}
		}
	}
	public void saveImage(Bitmap thePic) {
		// TODO Auto-generated method stub
		String file_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Recipt_image";
		File dir=new File(file_path);
		if (!dir.exists()){
			dir.mkdirs();
		}
		CamFileName=getDateAndTime()+".jpg";
		File file =new File (dir,CamFileName);
		try{
			FileOutputStream fout=new FileOutputStream(file);
			//thePic = Bitmap.createScaledBitmap(thePic, 100, 100, false);
			thePic.compress(Bitmap.CompressFormat.JPEG, 85, fout);
			fout.flush();
			fout.close();
			verifyFileCreation(file);
			//Toast.makeText(this, "Image saved successfully.", Toast.LENGTH_SHORT).show();
		}catch(FileNotFoundException e){ Toast.makeText(this, "FNFE:Sorry...Unable to save", Toast.LENGTH_LONG).show();}
		 catch(IOException e){Toast.makeText(this, "FNFE:Sorry...Unable to save", Toast.LENGTH_SHORT).show();
		}
	}

	private void verifyFileCreation(File file) {
		// TODO Auto-generated method stub
		MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,new MediaScannerConnection.OnScanCompletedListener() {
			
			@Override
			public void onScanCompleted(String path, Uri uri) {
				
				Log.e("External Storage","Scanned"+path+":");
				Log.e("External Storage","->uri-"+uri);
			}
		}); 
				
			
		}
	

	//create helping method cropCapturedImage(Uri picUri)
	public void cropCapturedImage(Uri picUri){
		//call the standard crop action intent 
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		//indicate image type and Uri of image
		cropIntent.setDataAndType(picUri, "image/*");
		//set crop properties
		cropIntent.putExtra("crop", "true");
		//indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		//indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		//retrieve data on return
		cropIntent.putExtra("return-data", true);
		//start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, 2);
	}
	
	
	
	private void pickDateDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setTitle("Please press to enter dates");

		// set the custom dialog components - text and button
		  tvDFdate = (TextView) dialog.findViewById(R.id.tvFdate);
		  tvDTdate = (TextView) dialog.findViewById(R.id.tvTdate);
		 Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		 
		 tvDFdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					calCall=1;
					dpd.show();
				}
			});
		 tvDTdate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					calCall=2;
					dpd.show();
				}
			});
		// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Fdate=tvDFdate.getText().toString();
					Tdate=tvDTdate.getText().toString();
					//validation goes here
					if (Fdate.equalsIgnoreCase("From Date")||Tdate.equalsIgnoreCase("To Date")){
						//dialog.dismiss();
						Toast.makeText(MainActivity.this, "Try again...you did not select valid dates.",Toast.LENGTH_SHORT).show();
						
					}
					else{	
					Log.i("dates check:","You've Chosed "+Fdate+" to "+Tdate);
					System.out.println("You've Chosen "+Fdate+" to "+Tdate);
					dialog.dismiss();
					Intent report=new Intent (getApplicationContext(),ReportView.class);
					Bundle extras = new Bundle();
					extras.putString("Fdate",Fdate);
					extras.putString("Tdate",Tdate);
					report.putExtras(extras);
					startActivity(report);
					}
				}
			});
 
			dialog.show();
	}
	
	private class datePickerListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth,
				int selectedDay) {
			// TODO Auto-generated method stub
			String strSelectedMonth,strSelectedDay;
			selectedMonth=selectedMonth + 1;
			if (selectedMonth<10)
				strSelectedMonth="0"+selectedMonth;
			else strSelectedMonth=""+selectedMonth;
			if (selectedDay<10)
				strSelectedDay="0"+selectedDay;
			else strSelectedDay=""+selectedDay;
				String selectedDate=selectedYear + "-" + strSelectedMonth + "-"	+ strSelectedDay;
			if(calCall==0)	etDate.setText(selectedDate);
			else if(calCall==1){
				tvDFdate.setText(selectedDate);
				calCall=0;}
			else if (calCall==2){
				tvDTdate.setText(selectedDate);
				calCall=0;
			
			
		}
		
		
	}
	
	
}
	
	private class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
			
		    mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

		        Matcher matcher=mPattern.matcher(dest);       
		        if(!matcher.matches())
		            return "";
		        return null;
		    }

		

		}
}
