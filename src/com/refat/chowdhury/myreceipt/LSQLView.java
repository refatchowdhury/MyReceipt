package com.refat.chowdhury.myreceipt;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LSQLView extends Activity{
	
	ArrayList<DataSet> listDataObj=new ArrayList<DataSet>();	
	dataAdapter info=new dataAdapter (this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lsql_view);
		
		ListView dataListView=(ListView)findViewById(R.id.reportListView);
			
		info.open();
		//set data for custom layout
		listDataObj=(ArrayList<DataSet>) info.getAllDataInObject();
		info.close();
        dataListView.setAdapter(new CustomAdapter(this)); 
		
		// register onClickListener to handle click events on each item
		  dataListView.setOnItemClickListener(new OnItemClickListener()
                    {
                             // argument position gives the index of item which is clicked
                            

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View v, int position, long arg3) {
								DataSet temp=listDataObj.get(position);
								Log.i("LSQLVIEW:id check from object","_id:"+temp.id+" receipt:"+temp.receipt);
								
								//receitLocation=temp.receipt;
																								 
								 if (temp.receipt!="No Image"){
									 Intent receiptShow=new Intent (getApplicationContext(),receitView.class);
									 receiptShow.putExtra("receiptName", temp.receipt);
										startActivity(receiptShow);
								 }
								 else
									 
									 Toast.makeText(getApplicationContext(), "No Image to Show",Toast.LENGTH_LONG).show();
								 
							}
                    });
	}
	
	
	

}
