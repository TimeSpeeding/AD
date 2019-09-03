package com.example.stationeryproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvUtil {
	
	public boolean exportCsv(File file,List<Disbursement> list)
	{
		boolean isSuccess=false;
	
		FileOutputStream out=null;
		OutputStreamWriter osw=null;
		BufferedWriter bw=null;
		
		try
		{
		out=new FileOutputStream(file);
		osw=new OutputStreamWriter(out,"GB2312");
		bw=new BufferedWriter(osw,1024);

		bw.write("Department,Description,Scheduled Qty,Actual Qty,Unit");
		bw.newLine();
		isSuccess=true;
		for(Disbursement disbursement :list)
		{
			bw.write(disbursement.getDep().toString()+",");
			bw.write(disbursement.getDescription().toString()+",");
			bw.write(disbursement.getScheduledQty()+",");
			bw.write(disbursement.getActualQty()+",");
			
		}
		bw.flush();
		

		}
		catch(Exception e)
		{
			
			//isSuccess=false;
		}
		finally {
			
			if(bw!=null)
			{
				try {
					bw.close();
					bw=null;
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(osw!=null)
			{
				try
				{
					osw.close();
					osw=null;
				}
				catch (IOException e) {
                    e.printStackTrace();
                } 
				
			}
			 if(out!=null){
	                try {
	                    out.close();
	                    out=null;
	                } catch (IOException e) {
	                    e.printStackTrace();
	                } 
	            }
		}
		return isSuccess;

	}

}


