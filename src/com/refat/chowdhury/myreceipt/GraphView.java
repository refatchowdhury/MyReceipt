package com.refat.chowdhury.myreceipt;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;




import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class GraphView extends Activity implements OnClickListener,OnItemSelectedListener{
	private Spinner spinChoice, spinYearList;
	 Button btnSubmit;
	 ListView lvGraph;
	
	 double[] expense2=new double [12];
	 	 
	 private String[] mMonth = new String[] {
				"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
				"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
			};
	
	 dataAdapter info=new dataAdapter (this);
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_menu);
		//set up widgets
		spinChoice = (Spinner) findViewById(R.id.spinnerChoice);
		spinYearList= (Spinner) findViewById(R.id.spinnerYearList);
		btnSubmit=(Button)findViewById(R.id.butSubmit);
		//lvGraph=(ListView)findViewById(R.id.lvGraph);
				
		//set data for spinYearList
		
		info.open();
		//spinner1
		List<String> listOfReport=new ArrayList<String>();
		listOfReport.add("Monthly");
		listOfReport.add("Yearly");
		ArrayAdapter<String> choiceAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listOfReport);
		spinChoice.setAdapter(choiceAdapter);
		//spinner2 
		List<String> listOfYear = new ArrayList<String>();
		listOfYear=info.getListOfYear();
				
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, listOfYear);
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinYearList.setAdapter(dataAdapter);
				
		
		info.close();
		btnSubmit.setOnClickListener(this);
		spinChoice.setOnItemSelectedListener(this);
		spinYearList.setOnItemSelectedListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.butSubmit:
			//Log.i("SpinChoice data test:","SpinChoice selection ID:"+spinChoice.getSelectedItemPosition()+"SpinChoice selection:"+spinChoice.getSelectedItem().toString());
			int reportType=spinChoice.getSelectedItemPosition();
			if (reportType==0){
				int year=Integer.parseInt(spinYearList.getSelectedItem().toString());
				Toast.makeText(this,"Generating.",Toast.LENGTH_SHORT).show();
				openMonthlyChart(year);
			}
			else if (reportType==1){
				Toast.makeText(this,"Generating.",Toast.LENGTH_SHORT).show();
				//openYearlyChart();
				openYearlyBarGraph();
			}
				
			
		break;
		}
	}
	private void openMonthlyChart(int year){
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
    	Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
    	
    	// Start Activity
    	startActivity(intent);
    	return;
    }
	
	private void openYearlyBarGraph(){
		
		//String[] mYear = new String [10] ;
		//double [] yearExpense = new double [10];
		info.open();
		List<String> ReportOfYear = new ArrayList<String>();
		List<String> yearAmount=info.getYearAmount();
		ReportOfYear=info.getListOfYear();
		//Log.i("in Year Bar Graph","Report of Year size: "+ReportOfYear.size());
		if(ReportOfYear.size()<4){
			
			int lastYear=Integer.parseInt(ReportOfYear.get(ReportOfYear.size()-1));
			//Log.d("ReportOfYear check","LastYear:"+lastYear);
			for (int i=0;i<5;i++){
				lastYear++;				
				ReportOfYear.add(String.valueOf(lastYear));
				yearAmount.add("0");
			}
			
			/*for (int i=0;i<ReportOfYear.size();i++){
				Log.i("OpenYearlyBarGraph","ReportOfYear "+ReportOfYear.get(i));
				Log.i("OpenYearlyBarGraph","YearAmount "+yearAmount.get(i));
			}*/
		}
		
		info.close();
		//Log.i("OpenYearlyBarGraph","ReportOfYear size "+ReportOfYear.size());
		//Log.i("OpenYearlyBarGraph","YearAmount size "+yearAmount.size());
		double [] yearExpense = new double [ReportOfYear.size()];
		for( int i=0;i<yearAmount.size();i++){
			yearExpense[i]=Double.parseDouble(yearAmount.get(i));
			//Log.d("Year expense value check:","Yearexp="+yearExpense[i]);
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
    	Intent intent = ChartFactory.getBarChartIntent(getBaseContext(), dataset, multiRenderer, Type.DEFAULT);
    	
    	// Start Activity
    	startActivity(intent);
		
		return;
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
				Toast.makeText(this,"Please select a year.",Toast.LENGTH_SHORT).show();
				
			}
			else {
				spinYearList.setEnabled(false);
				spinYearList.setClickable(false);
			}
			break;
			
		case R.id.spinnerYearList:
			/*TextView selectYear=(TextView)arg1;
			String tempYear= (String) selectYear.getText();
			year=Integer.parseInt(tempYear);
			Toast.makeText(this,"Current selected year: "+year,Toast.LENGTH_SHORT).show();*/
			break;
		
		}
		
		
		
		
		
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}  
	  
}
