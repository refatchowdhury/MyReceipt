package com.refat.chowdhury.myreceipt;


	

	
	import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.util.ArrayList;
	import android.graphics.Color;
	import android.os.Environment;
	import android.util.Log;
	import com.lowagie.text.Document;
	import com.lowagie.text.DocumentException;

	import com.lowagie.text.Font;
	import com.lowagie.text.HeaderFooter;
	import com.lowagie.text.PageSize;
	import com.lowagie.text.Paragraph;
	import com.lowagie.text.Phrase;

	import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

	
	public class PdfActivity {		
		ArrayList<DataSet> listDataObj=new ArrayList<DataSet>();
		String TotalAmount;
		String Fdate;
		String Tdate;
		
		PdfActivity (ArrayList<DataSet> listDataObj,String TotalAmount,String Fdate,String Tdate ){
			this.listDataObj=listDataObj;
			this.TotalAmount=TotalAmount;
			this.Fdate=Fdate;
			this.Tdate=Tdate;
		}
		
		public void createPDF()
	    {
	        Document doc = new Document(PageSize.A4);
	         
	         
	         try {
	                String path = Environment.getExternalStorageDirectory().getAbsolutePath() ;
	                  
	                File dir = new File(path);
	                    if(!dir.exists())
	                        dir.mkdirs();
	 
	                Log.d("PDFCreator", "PDF Path: " + path);
	                 
	                     
	                File file = new File(dir, "receipt.pdf");
	                FileOutputStream fOut = new FileOutputStream(file);
	      
	                PdfWriter.getInstance(doc, fOut);
	                  
	                //open the document
	                doc.open();
	                 
	                
	              
	                Paragraph p1 = new Paragraph("My Receipt");
	                Font paraFont= new Font(Font.COURIER,14.0f,Color.BLACK);
	                p1.setAlignment(Paragraph.ALIGN_CENTER);
	                p1.setFont(paraFont);
	                 
	                 //add paragraph to document   
	                 doc.add(p1);
	                 Paragraph p2 = new Paragraph("");
	                 doc.add(p2);
	                
	                 Paragraph p3 = new Paragraph(Fdate+"  To  "+Tdate);
		             Font p3Font= new Font(Font.COURIER,12.0f,Color.BLACK);
		             p3.setAlignment(Paragraph.ALIGN_CENTER);
		             p3.setFont(p3Font);
		             doc.add(p3);
	                
	                 
	               //setting table
	                 //Font bfBold12 = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
		             //Font bf11 = new Font(Font.TIMES_ROMAN, 11);
	                 PdfPTable table=new PdfPTable(3); 
	                 table.setSpacingBefore(20f);
	                 table.setSpacingAfter(20f);
	                 table.setWidthPercentage(100);
	                 //Font tableFont= new Font(Font.COURIER,12.0f,Color.GREEN);
	                 float[] columnWidths = {1f, 1f, 1f};
	                 table.setWidths(columnWidths);
	                 
	              // insert column headings
	             
	                 table.addCell("DATE");
	                 table.addCell("STORE");
	                 table.addCell("AMOUNT");
	                
	               //insert data
	                 for (int i=0;i<listDataObj.size();i++){
	                	 DataSet temp=listDataObj.get(i);
	                	 
	                	 table.addCell(temp.date);
		                 table.addCell(temp.shop);
		                 table.addCell(temp.amount);
	                	    	 
	                	 
	                 }
	                 
	                 //insert total
	                 table.addCell("");
	                 table.addCell("Total Expense :");
	                 table.addCell(TotalAmount);
	                 
	                 doc.add(table);
	                 
	                 //set footer
	                 
	                 Phrase footerText = new Phrase("This is a footer");
	                 HeaderFooter pdfFooter = new HeaderFooter(footerText, false);
	                 doc.setFooter(pdfFooter);
	                 
	                 //fOut.close();
	 
	                 
	         } catch (DocumentException de) {
	                 Log.e("PDFCreator", "DocumentException:" + de);
	         } catch (IOException e) {
	                 Log.e("PDFCreator", "ioException:" + e);
	         }
	         finally
	         {
	                 doc.close();
	                 
	                 
	         }
	        
	    }     
		
	}



