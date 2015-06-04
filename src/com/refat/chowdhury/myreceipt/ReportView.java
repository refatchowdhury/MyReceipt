package com.refat.chowdhury.myreceipt;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportView extends Activity  {
	
	ArrayList<DataSet> listDataObj=new ArrayList<DataSet>();
	TextView tvTotal;
	ListView dataList;
	String Fdate,Tdate,Total;
	Button btPdf;
	
	dataAdapter info=new dataAdapter(this);
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.new_report_view);
			
			//get data from caller
			Bundle extras = getIntent().getExtras();
			Fdate = extras.getString("Fdate");
			Tdate = extras.getString("Tdate");
			System.out.println("Now in reportview: "+Fdate+" to "+Tdate);
			
		    
			tvTotal=(TextView) findViewById(R.id.tvTotal);
			btPdf=(Button)findViewById(R.id.butPdf);
			dataList=(ListView)findViewById(R.id.lvReport);
			try{
			info.open();
			Total=info.getTotal(Fdate, Tdate);
			listDataObj=(ArrayList<DataSet>) info.getAllReportDataInObject(Fdate,Tdate);
			info.close();
			
			tvTotal.setText("Total Expenditure: "+Total);
			
			dataList.setAdapter(new CustomReportAdapter(this.getApplicationContext()));
			}catch(NullPointerException e){e.printStackTrace();}
			
			btPdf.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					PdfActivity obj=new PdfActivity(listDataObj,Total,Fdate,Tdate);
					obj.createPDF();
					Toast.makeText(ReportView.this,"receipt.pdf has been created in your sd card.",Toast.LENGTH_SHORT).show();
				}
			});
			
		}
		
		private class CustomReportAdapter extends BaseAdapter {
						
			//ArrayList<DataSet> listDataObj;
			Context context;
			CustomReportAdapter (Context c){
				context=c;
				
				//dataAdapter info=new dataAdapter (c);
				
				
				
				
				
			}
						
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return listDataObj.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return listDataObj.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
				
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				View row=inflater.inflate(R.layout.single_row_layout,parent,false);
				TextView dt=(TextView) row.findViewById(R.id.tvDt);
				TextView shop=(TextView) row.findViewById(R.id.tvShop);
				TextView amount=(TextView) row.findViewById(R.id.tvAmount);
				
				DataSet temp=listDataObj.get(position);
				dt.setTextColor(Color.BLACK);
				shop.setTextColor(Color.BLACK);
				amount.setTextColor(Color.BLACK);
				dt.setText(temp.date);
				shop.setText(temp.shop);
				amount.setText(temp.amount);
				
				return row;
			}
		}
	
		
	}



