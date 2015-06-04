package com.refat.chowdhury.myreceipt;



import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class deleteRecord extends Activity{
	
	
	ArrayList<DataSet> listDataObj=new ArrayList<DataSet>();	
	dataAdapter info=new dataAdapter (this);
	ListView dataListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_listview);
		
		dataListView=(ListView)findViewById(R.id.listViewDelete);
		
		setObjAdapter();
		
		// register onClickListener to handle click events on each item
		  dataListView.setOnItemClickListener(new OnItemClickListener()
                    {
                             // argument position gives the index of item which is clicked
                            

							@Override
							public void onItemClick(AdapterView<?> arg0,View v, final int position, long arg03) {
								// TODO Auto-generated method stub
								final DataSet temp=listDataObj.get(position);
								Log.i("deleteRecord.java","_id:"+temp.id+" receipt:"+temp.receipt);
								info.open();
								
														
									AlertDialog.Builder builder = new AlertDialog.Builder(deleteRecord.this);
									builder
									.setTitle("Delete Record")
									.setMessage("Please confirm before you delete the record.")
									//.setIcon(android.R.drawable.dialog_frame)
									.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {			      	
											//Yes button clicked.
											try{
												 info.deleteRecord(Integer.parseInt(temp.id));//deleting record
																											
												//deleting image
											
												if (temp.receipt!="No Image"){
													String sdcard_path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/Recipt_image";
													//deleteFile(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Expense_image"+File.separator+imageLocation);

													File cacheFile =new File (sdcard_path,temp.receipt);
													if(cacheFile.exists()){
													boolean deleted=cacheFile.delete();
													//boolean deleted=deleteFile(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Expense_image"+File.separator+imageLocation);
													Log.i("Deletion check","deleted:"+deleted);
													sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
													
													
													}
													else
														Toast.makeText(getApplicationContext(), " No Image to delete",Toast.LENGTH_SHORT).show();

														
													
												}
												else
													Toast.makeText(getApplicationContext(), " No Image to delete",   Toast.LENGTH_SHORT).show();
											
											}catch (Exception e){e.printStackTrace();}
											finally{ 
												Toast.makeText(getApplicationContext(), "Record deleted successfully",   Toast.LENGTH_SHORT).show();
											    
												
												info.close();
											   // arrayAdapter.remove(arrayAdapter.getItem(position));
												//arrayAdapter.notifyDataSetChanged();
												
												
												setObjAdapter();
								            }
	    	    	
									}
									}
									)
									.setNegativeButton("No", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {			      	
											//NO button clicked, do something
											Toast.makeText(deleteRecord.this, "Record was not deleted!",Toast.LENGTH_SHORT).show();
	    	    	
										}
									})	
									.show(); 
									
									
																 
							}
                    });
		 
	}
	
	private void setObjAdapter() {
		// TODO Auto-generated method stub
		info.open();
		listDataObj=(ArrayList<DataSet>) info.getAllDataInObject();
		
		info.close();
		dataListView.setAdapter(new CustomAdapter(deleteRecord.this)); 
	}
	
	
	
	
	
}
