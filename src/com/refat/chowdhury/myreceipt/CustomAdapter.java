package com.refat.chowdhury.myreceipt;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	
	
	ArrayList<DataSet> listDataObj;
	Context context;
	CustomAdapter (Context c){
		context=c;
		listDataObj=new ArrayList<DataSet>();
		dataAdapter info=new dataAdapter (c);
		info.open();
		
		listDataObj=(ArrayList<DataSet>) info.getAllDataInObject();
		
		
		info.close();
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
		
		dt.setText(temp.date);
		shop.setText(temp.shop);
		amount.setText(temp.amount);
		
		return row;
	}
}
