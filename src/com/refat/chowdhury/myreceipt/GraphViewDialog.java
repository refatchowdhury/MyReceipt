package com.refat.chowdhury.myreceipt;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class GraphViewDialog implements OnItemSelectedListener {
	
	int year;
	int reportType=0;
	Context context;
	List<String> listOfReport;
	List<String> listOfYear;
	Spinner spinChoice ;
	Spinner spinYearList;
	Button btnSubmit;
	dataAdapter info;
	
     Dialog dialog;
	GraphViewDialog(Context c){
		this.context=c;
		dataAdapter info=new dataAdapter (c);
		info.open();
		//spinner1
		listOfReport=new ArrayList<String>();
		listOfReport.add("Monthly");
		listOfReport.add("Yearly");
		
		//spinner2 
		listOfYear = new ArrayList<String>();
		listOfYear=info.getListOfYear();
			
		info.close();
		
	}
	
	public void showDialog(){
		
	    dialog = new Dialog(context);
		dialog.setContentView(R.layout.graph_menu);
		dialog.setTitle("Please choose your report");

		// set the custom dialog components - text and button
		spinChoice = (Spinner) dialog.findViewById(R.id.spinnerChoice);
		spinYearList= (Spinner) dialog.findViewById(R.id.spinnerYearList);
		Button btnSubmit=(Button) dialog.findViewById(R.id.butSubmit);
		
		ArrayAdapter<String> choiceAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,listOfReport);
		spinChoice.setAdapter(choiceAdapter);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, listOfYear);
		spinYearList.setAdapter(dataAdapter);
		
		dialog.show();
		
		
		spinChoice.setOnItemSelectedListener(this);
		spinYearList.setOnItemSelectedListener(this);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 reportType=spinChoice.getSelectedItemPosition();
				Log.i("SpinChoice data test:","SpinChoice selection ID:"+spinChoice.getSelectedItemPosition()+"SpinChoice selection:"+spinChoice.getSelectedItem().toString());
				if (reportType==0){
					int year=Integer.parseInt(spinYearList.getSelectedItem().toString());
					Toast.makeText(context,"Generating.",Toast.LENGTH_SHORT).show();
					openMonthlyChart(year);
					dialog.dismiss();
					
				}
				else if (reportType==1){
					Toast.makeText(context,"Generating.",Toast.LENGTH_SHORT).show();
					//openYearlyChart();
					openYearlyBarGraph();
					dialog.dismiss();
				}
									
			}
		});
		
		
	  return ;
	}
	
	

	

	

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.spinnerChoice:
			//TextView selectReport=(TextView)arg1;
			//reportType=position;
			//Toast.makeText(this,"reportType:"+position,Toast.LENGTH_LONG).show();
			if (position==0){
				spinYearList.setEnabled(true);
				spinYearList.setClickable(true);
				Toast.makeText(context,"Please select a year.",Toast.LENGTH_SHORT).show();
			}
			else {
				spinYearList.setEnabled(false);
				spinYearList.setClickable(false);
			}
			break;
		}
	}

	
	
	
	
	
	
	
	private void openMonthlyChart(int year){
		
	 	 
		  String[] mMonth = new String[] {
					"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
					"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
				};
		 // double[] expense2=new double [12];
		  double []expense=new double [12];	
			info.open();
			//List<String> amount=info.getAmount(year);
			
			expense=info.getAmountInDouble(year);
			info.close();
	    	// Creating an  XYSeries for expense
	    	XYSeries expenseSeries = new XYSeries("Expense");
	    	
	    	// Adding data to Expense Series
	    	for(int i=0;i<expense.length;i++){    		
	    		//incomeSeries.add(i,income[i]);
	    		expenseSeries.add(i,expense[i]);
	    	}
	    	    	
	    	// Creating a dataset to hold each series
	    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    	
	    	// Adding Expense Series to dataset
	    	dataset.addSeries(expenseSeries);    	
	        	
	    	// Creating XYSeriesRenderer to customize expenseSeries
	    	XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
	    	//expenseRenderer.setColor(Color.rgb(220, 80, 80));
	    	expenseRenderer.setColor(Color.BLUE);
	    	expenseRenderer.setFillPoints(true);
	    	expenseRenderer.setLineWidth(2);
	    	expenseRenderer.setDisplayChartValues(true); 
	    	expenseRenderer.setChartValuesTextAlign(Align.CENTER);
	    	expenseRenderer.setChartValuesTextSize(16f);
	    	
	    	//expenseRenderer.getStroke();
	    	
	    	// Creating a XYMultipleSeriesRenderer to customize the whole chart
	    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
	    	multiRenderer.setXLabels(0);
	    	multiRenderer.setChartTitle("Monthly Expense Chart");
	    	multiRenderer.setXTitle("Year"+year);
	    	multiRenderer.setYTitle("Amount");
	    	multiRenderer.setZoomButtonsVisible(true);
	    	multiRenderer.setApplyBackgroundColor(true);
	    	multiRenderer.setBackgroundColor(Color.BLACK);
	    	multiRenderer.setBarSpacing(2);
	        multiRenderer.setBarWidth(50);
	        //multiRenderer.setMarginsColor(Color.CYAN);
	    	for(int i=0; i< mMonth.length;i++){
	    		multiRenderer.addXTextLabel(i, mMonth[i]);    		
	    	}    	
	    	
	    	
	    	// Adding incomeRenderer and expenseRenderer to multipleRenderer
	    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
	    	// should be same
	    	multiRenderer.addSeriesRenderer(expenseRenderer);
	    	
	    	// Creating an intent to plot bar chart using dataset and multipleRenderer    	
	    	Intent intent = ChartFactory.getBarChartIntent(context, dataset, multiRenderer, Type.DEFAULT);
	    	
	    	// Start Activity
	    	context.startActivity(intent);
	    	
	    	
		return;
		
	}
	
	
	public void openYearlyBarGraph(){
		//String[] mYear = new String [10] ;
				//double [] yearExpense = new double [10];
				info.open();
				List<String> ReportOfYear = new ArrayList<String>();
				List<String> yearAmount=info.getYearAmount();
				ReportOfYear=info.getListOfYear();
				Log.i("in Year Bar Graph","Reoport of Year size: "+ReportOfYear.size());
				if(ReportOfYear.size()<4){
					
					int lastYear=Integer.parseInt(ReportOfYear.get(ReportOfYear.size()-1));
					Log.d("ReportOfYear check","LastYear:"+lastYear);
					for (int i=0;i<5;i++){
						lastYear++;				
						ReportOfYear.add(String.valueOf(lastYear));
						yearAmount.add("0");
					}
					
					for (int i=0;i<ReportOfYear.size();i++){
						Log.i("OpenYearlyBarGraph","ReportOfYear "+ReportOfYear.get(i));
						Log.i("OpenYearlyBarGraph","YearAmount "+yearAmount.get(i));
					}
				}
				
				info.close();
				Log.i("OpenYearlyBarGraph","ReportOfYear size "+ReportOfYear.size());
				Log.i("OpenYearlyBarGraph","YearAmount size "+yearAmount.size());
				double [] yearExpense = new double [ReportOfYear.size()];
				for( int i=0;i<yearAmount.size();i++){
					yearExpense[i]=Double.parseDouble(yearAmount.get(i));
					Log.d("Year expense value check:","Yearexp="+yearExpense[i]);
				}
				
				// Creating an  XYSeries for expense
		    	XYSeries expenseSeries = new XYSeries("Expense");
		    	
		    	// Adding data to Expense Series
		    	for(int i=0;i<yearExpense.length;i++){    		
		    		
		    		expenseSeries.add(i,yearExpense[i]);
		    	}
		    	    	
		    	// Creating a dataset to hold each series
		    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    	
		    	// Adding Expense Series to dataset
		    	dataset.addSeries(expenseSeries);    	
		        	
		    	// Creating XYSeriesRenderer to customize expenseSeries
		    	XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		    	//expenseRenderer.setColor(Color.rgb(220, 80, 80));
		    	expenseRenderer.setColor(Color.BLUE);
		    	expenseRenderer.setFillPoints(true);
		    	//expenseRenderer.setLineWidth(2);
		    	expenseRenderer.setChartValuesTextSize(20);
		    	expenseRenderer.setDisplayChartValues(true); 
		    	expenseRenderer.setChartValuesTextAlign(Paint.Align.CENTER);
		    	//expenseRenderer.setXAxisMin(0.5);
		    	//expenseRenderer.setXAxisMax(10.5);
		    	//expenseRenderer.setYAxisMin(0);
		    	//expenseRenderer.setYAxisMax(210);
		    	//expenseRenderer.getStroke();
		    	
		    	// Creating a XYMultipleSeriesRenderer to customize the whole chart
		    	XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		    	multiRenderer.setXLabels(0);
		    	multiRenderer.setChartTitle("Yearly Expense Chart");
		    	multiRenderer.setXTitle("Years");
		    	multiRenderer.setYTitle("Amount");
		    	multiRenderer.setZoomButtonsVisible(true);
		    	multiRenderer.setApplyBackgroundColor(true);
		    	multiRenderer.setBackgroundColor(Color.BLACK);
		    	multiRenderer.setBarSpacing(1);
		        multiRenderer.setBarWidth(50);
		        //multiRenderer.setMarginsColor(Color.CYAN);
		    	for(int i=0; i< ReportOfYear.size();i++){
		    		multiRenderer.addXTextLabel(i, ReportOfYear.get(i));    		
		    	}    	
		    	    	
		    	// Adding incomeRenderer and expenseRenderer to multipleRenderer
		    	// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		    	// should be same
		    	multiRenderer.addSeriesRenderer(expenseRenderer);
		    	
		    	// Creating an intent to plot bar chart using dataset and multipleRenderer    	
		    	Intent intent = ChartFactory.getBarChartIntent(context, dataset, multiRenderer, Type.DEFAULT);
		    	
		    	// Start Activity
		    	context.startActivity(intent);
		
		return;
	
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
