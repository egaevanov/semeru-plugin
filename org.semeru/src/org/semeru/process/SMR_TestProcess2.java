//package org.semeru.process;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.logging.Level;
//
//import org.compiere.process.ProcessInfoParameter;
//import org.compiere.process.SvrProcess;
//
//public class SMR_TestProcess2 extends SvrProcess{
//	
//	
//	private int p_reg_mth = 0;
//	private Timestamp p_start_date = null;
//	
//	@Override
//	protected void prepare() {
//
//		ProcessInfoParameter[] para = getParameter();
//		for (int i = 0; i < para.length; i++)
//		{
//			String name = para[i].getParameterName();
//			if (para[i].getParameter() == null);
//			
//			else if(name.equals("reg_mth"))
//				p_reg_mth = (int)para[i].getParameterAsInt();
//		
//			
//			else if(name.equals("start_date"))
//				p_start_date = (Timestamp)para[i].getParameterAsTimestamp();
//		
//			
//			else
//				log.log(Level.SEVERE, "Unknown Parameter: " + name);
//		}
//		
//	}
//	@Override
//	protected String doIt() throws Exception {
//		
//
//		Timestamp next_due_date = null;
//		Date tgl = new Date(); 
//		Date finalDate = new Date();
//		
//		Calendar c = Calendar.getInstance(); 
//		c.setTime(p_start_date); 
//		int contractDuration = p_reg_mth;
//		if(p_reg_mth >0) {
//			c.add(Calendar.MONTH, contractDuration);
//			
//			if(c.get(Calendar.DAY_OF_MONTH) >= 28) {
//				
//				c.set(Calendar.DAY_OF_MONTH, 1);
//				c.add(Calendar.MONTH, 1);
//			}
//			
//		}
//		finalDate = c.getTime();
//		
//		
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
//		String tglTostr = df.format(finalDate);
//
//		next_due_date = Timestamp.valueOf(tglTostr);
//		
//		
//		System.out.println("Start Date : " +p_start_date);
//		System.out.println("Duration : " +p_reg_mth);
//		System.out.println("Next Due Date : " +next_due_date);
//		
//		
//		return null;
//	}
//	
//	
//	
//
//}
