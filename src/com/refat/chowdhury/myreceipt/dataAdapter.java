package com.refat.chowdhury.myreceipt;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class dataAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "shop_name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "purchase_date";
    public static final String KEY_RECEIT = "purchase_receit";
//just checking git branch change nothing to do with this file
//another check
    public static final String DATABASE_NAME = "myReceipt";
    public static final String DATABASE_TABLE = "recordTable";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TAG = "dataAdapter";


    private DbHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;
    String[] columns = new String[]{KEY_ROWID, KEY_DATE, KEY_NAME, KEY_AMOUNT, KEY_RECEIT};
    public static int iRow, iName, iDate, iAmount, iReceit;

    private static class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + DATABASE_TABLE + "(" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_DATE + " DATETIME NOT NULL, " +
                            KEY_NAME + " TEXT NOT NULL, " +
                            KEY_AMOUNT + " INTEGER NOT NULL, " +
                            KEY_RECEIT + " TEXT);"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    }

    public dataAdapter(Context c) {
        ourContext = c;
    }

    public dataAdapter open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long createEntry(String pdate, String shop_name, String amount, String receit) {

        double DbAmount = Double.parseDouble(amount);
        Log.i("createEntry:", "DbAmount:" + DbAmount);
        double tempDbAmount = DbAmount * 100;
        Log.i("createEntry:", "tempIntAmount:" + tempDbAmount);
        int IntAmount = (int) Math.round(tempDbAmount);
        Log.i("createEntry:", "IntAmount:" + IntAmount);
        ContentValues cv = new ContentValues();
        cv.put(KEY_DATE, pdate);
        cv.put(KEY_NAME, shop_name);
        cv.put(KEY_AMOUNT, IntAmount);
        cv.put(KEY_RECEIT, receit);

        return ourDatabase.insert(DATABASE_TABLE, null, cv);

    }


    public Cursor easyQuery(String yourQuery) {
        Cursor c = ourDatabase.rawQuery(yourQuery, null);

        return c;
    }

    public String getReceitLocation(int position) {

        Log.e("getReceitLocation check..", "Scanned for receit view:" + position);
        String location;
        String selectQuery = "SELECT " + KEY_RECEIT + " FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + "=" + position;

        Cursor c = ourDatabase.rawQuery(selectQuery, null);
        iReceit = c.getColumnIndex(KEY_RECEIT);
        c.moveToFirst();
        location = c.getString(iReceit);
        Log.e("Image Location", "Scanned for receit view" + location);

        return location;

    }

    public void deleteRecord(long id) {
        //String string=String.valueOf(id);
        String where = KEY_ROWID + "=" + id;
        //String selectQuery = "DELETE FROM " + DATABASE_TABLE +" WHERE "+KEY_ROWID+"="+position;
        //Cursor c = ourDatabase.rawQuery(selectQuery, null);
        //ourDatabase.execSQL(selectQuery);
        ourDatabase.delete(DATABASE_TABLE, where, null);

        //String query="DELETE FROM"+ DATABASE_TABLE +"WHERE _id = '" + string + "'";
        //ourDatabase.execSQL(query);
    }

    public List<String> getId() {
        List<String> allId = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE;

        //SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        iRow = c.getColumnIndex(KEY_ROWID);

        // looping through all rows and adding to list
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

            allId.add(c.getString(iRow));
        }

        // return all data
        return allId;

    }


    public String getTotal(String Fdate, String Tdate) {

        String total = null;
        String selectQuery = "SELECT " + "SUM(" + KEY_AMOUNT + ") AS Total" +
                " FROM " + DATABASE_TABLE + " WHERE " + KEY_DATE + " BETWEEN '" + Fdate + "' AND '" + Tdate + "'";
        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0) {

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {


                DecimalFormat format = new DecimalFormat("0.00");
                double tempTotal = Integer.parseInt(c.getString(0)) * 0.01;

                total = format.format(tempTotal);

            }
        } else {
            total = "0.00";
        }

        return total;
    }


    public double[] getAmountInDouble(int year) {
        //int year=2014;
        double[] tempAmount = new double[12];
        double tempDoubleAmount;

        // Select All Query
        String selectQuery = " SELECT SUM(amount),strftime('%m',purchase_date) FROM recordTable  WHERE strftime('%Y',purchase_date)='" + year + "' GROUP BY strftime('%m',purchase_date)";

        Cursor c = ourDatabase.rawQuery(selectQuery, null);
        DecimalFormat format = new DecimalFormat("0.00");

        // looping through all rows and adding to list
        if (c != null && c.getCount() > 0) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                tempDoubleAmount = Integer.parseInt(c.getString(0)) * 0.01;

                String formattedAmount = format.format(tempDoubleAmount);

                tempAmount[Integer.parseInt(c.getString(1)) - 1] = Double.parseDouble(formattedAmount);
            }
        }
        for (int i = 0; i < tempAmount.length; i++) {
            //allAmount.add(String.valueOf(tempAmount[i]));
            Log.i("getAmountInDouble() Check:", "tempAmount:" + tempAmount[i]);
            //tempAmount[i]=0;
        }
        // return all data
        return tempAmount;

    }

    public double[] getAmountInDouble1(int year) {
        int i = 0;
        double[] tempAmount = new double[12];
        // Select All Query
        String selectQuery = " SELECT SUM(amount),strftime('%m',purchase_date) FROM  recordTable  WHERE strftime('%Y',purchase_date)='" + year + "' GROUP BY strftime('%m',purchase_date)";
        //DecimalFormat format = new DecimalFormat("0.00");		      
        Cursor c = ourDatabase.rawQuery(selectQuery, null);
        double tempDoubleAmount;
        // looping through all rows and adding to list
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //String formattedAmount=format.format(c.getDouble(0));
            //tempAmount[c.getInt(1)-1]=Double.parseDouble(formattedAmount);

            tempDoubleAmount = Integer.parseInt(c.getString(0)) * 0.01;

            //String formattedAmount=format.format(tempDoubleAmount);
            tempAmount[Integer.parseInt(c.getString(1)) - 1] = Math.round(tempDoubleAmount);

            Log.i("getAmountInDouble() Check:", "c.getInt(1)-1:" + (Integer.parseInt(c.getString(1)) - 1) + "tempAmount:" + tempAmount[i++]);
        }
       /*  for(int i=0;i<tempAmount.length;i++){
             //allAmount.add(String.valueOf(tempAmount[i]));
        	 Log.i("getAmountInDouble() Check:","tempAmount:"+tempAmount[i]);
         }*/
        // return all data
        return tempAmount;

    }

    public List<String> getListOfYear() {
        // TODO Auto-generated method stub
        List<String> allYears = new ArrayList<String>();

        // Select All Query
        // String selectQuery =" select strftime('%Y',purchase_date) from recordTable GROUP BY strftime('%Y',purchase_date) ORDER BY strftime('%Y',purchase_date) DESC" ;
        String selectQuery = " select strftime('%Y',purchase_date) from recordTable GROUP BY strftime('%Y',purchase_date)";
        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0) {

            // looping through all rows and adding to list
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                //result=c.getString(0).trim();
                allYears.add(c.getString(0).trim());
            }

        }
        //Log.i("in allyear check:","allyear size"+allYears.size());
        else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            allYears.add(String.valueOf(year));
        }
        Log.i("in allyear check:", "allyear size" + allYears.size());
        // return all data
        return allYears;
    }

    public List<String> getYearAmount() {
        // TODO Auto-generated method stub

        List<String> allAmount = new ArrayList<String>();
        String result = "";
        // Select All Query
        String selectQuery = "SELECT SUM(amount),strftime('%Y',purchase_date)  FROM  recordTable  GROUP BY strftime('%Y',purchase_date)";

        Cursor c = ourDatabase.rawQuery(selectQuery, null);
        if (c != null && c.getCount() > 0) {
            // looping through all rows and adding to list
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                //format the amount
                DecimalFormat format = new DecimalFormat("0.00");
                double tempTotal = Integer.parseInt(c.getString(0)) * 0.01;
                result = format.format(tempTotal);
                allAmount.add(result);
            }
        } else {
            allAmount.add("0");
        }

        // return all data
        return allAmount;


    }

    public List<String> getYearAmount1() {
        // TODO Auto-generated method stub

        List<String> allAmount = new ArrayList<String>();
        String result = "";
        // Select All Query
        String selectQuery = "SELECT SUM(amount),strftime('%Y',purchase_date)  FROM  recordTable  GROUP BY strftime('%Y',purchase_date)";

        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            //DecimalFormat format=new DecimalFormat("0.00");
            double tempTotal = Integer.parseInt(c.getString(0)) * 0.01;
            tempTotal = Math.round(tempTotal);
            result = String.valueOf(tempTotal);
            //result=c.getString(0);
            allAmount.add(result);
        }

        // return all data
        return allAmount;


    }

    public List<DataSet> getAllDataInObject() {

        List<DataSet> allDataObject = new ArrayList<DataSet>();
        String selectQuery;
		
		/*if (args.length>0){
			Log.i("varargs check:","args:"+args[0]);
			selectQuery = "SELECT *  FROM " + DATABASE_TABLE +" WHERE "+KEY_DATE+" BETWEEN '"+args[0]+"' AND '"+args[1]+"'"+" ORDER BY "+KEY_DATE+" DESC;" ;
		}
		else{*/
        selectQuery = "SELECT * FROM " + DATABASE_TABLE + " ORDER BY " + KEY_DATE + " DESC;";
        //}
        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        iRow = c.getColumnIndex(KEY_ROWID);
        iDate = c.getColumnIndex(KEY_DATE);
        iName = c.getColumnIndex(KEY_NAME);
        iAmount = c.getColumnIndex(KEY_AMOUNT);
        iReceit = c.getColumnIndex(KEY_RECEIT);
        DecimalFormat format = new DecimalFormat("0.00");
        String formattedAmount;

        // looping through all rows and adding to list
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            Log.i("getAllDataInObject", "c.getInt(iAmount:" + c.getInt(iAmount));
            double tempTotal = c.getInt(iAmount) * 0.01;
            Log.i("getAllDataInObject", "tempTotal: " + tempTotal);
            formattedAmount = format.format(tempTotal);
            allDataObject.add(new DataSet(c.getString(iRow), c.getString(iDate), c.getString(iName), formattedAmount, c.getString(iReceit)));
        }

        return allDataObject;

    }

    public List<DataSet> getAllReportDataInObject(String fdate, String tdate) {

        List<DataSet> allDataObject = new ArrayList<DataSet>();
        String selectQuery;
        Date max, min;
        //Check fdate and tdate

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = (Date) format.parse(fdate);
            Date date2 = (Date) format.parse(tdate);
            if (date1.compareTo(date2) < 0) {

                max = date2;
                min = date1;
                //Log.i("Date Compare Check:","max:"+max+" min:"+min);
            } else {
                max = date1;
                min = date2;

            }

            //From Date to String..
            fdate = format.format(min);
            tdate = format.format(max);
            Log.i("Date Compare Check in block:", "max:" + tdate + " min:" + fdate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("Fdate and Tdate Check out block:", "fdate:" + fdate + " tdate" + tdate);
        selectQuery = "SELECT *  FROM " + DATABASE_TABLE + " WHERE " + KEY_DATE + " BETWEEN '" + fdate + "' AND '" + tdate + "'" + " ORDER BY " + KEY_DATE + " DESC;";
        Cursor c = ourDatabase.rawQuery(selectQuery, null);

        iRow = c.getColumnIndex(KEY_ROWID);
        iDate = c.getColumnIndex(KEY_DATE);
        iName = c.getColumnIndex(KEY_NAME);
        iAmount = c.getColumnIndex(KEY_AMOUNT);
        iReceit = c.getColumnIndex(KEY_RECEIT);


        //  DecimalFormat format = new DecimalFormat("0.00");
        String formattedAmount;

        // looping through all rows and adding to list
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            double tempTotal = Integer.parseInt(c.getString(iAmount)) * 0.01;
            //tempTotal=Math.round(tempTotal);
            formattedAmount = Double.toString(tempTotal);
            allDataObject.add(new DataSet(c.getString(iRow), c.getString(iDate), c.getString(iName), formattedAmount, c.getString(iReceit)));
        }

        return allDataObject;

    }

}
