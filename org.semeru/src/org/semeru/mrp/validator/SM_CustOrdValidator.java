package org.semeru.mrp.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MPeriod;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;
import org.semeru.mrp.model.X_SM_CustOrd_Upload;

public class SM_CustOrdValidator {
	
	
public static CLogger log = CLogger.getCLogger(SM_CustOrdValidator.class);
	
	public static String executeCustOrdEvent(Event event, PO po) {
		
		String msgCustOrd = "";
		X_SM_CustOrd_Upload custOrd = (X_SM_CustOrd_Upload) po;
		if (event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)||event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
		
			msgCustOrd = CustOrdBeforeSave(custOrd);
		}
		
	return msgCustOrd;

	}
	
	
	private static String CustOrdBeforeSave(X_SM_CustOrd_Upload custOrd) {
		String rs = "";
				
		MPeriod period = new MPeriod(custOrd.getCtx(), custOrd.getC_Period_ID(), custOrd.get_TrxName());
		
		StringBuilder SQLGetDtf = new StringBuilder();
		SQLGetDtf.append("SELECT IsDtf ");
		SQLGetDtf.append(" FROM C_Period ");
		SQLGetDtf.append(" WHERE C_Period_ID ="+period.getC_Period_ID());
		
		String dtf = DB.getSQLValueString(null, SQLGetDtf.toString());
		
		if(dtf.toUpperCase().equals("Y")) {
			
			rs = "Save Dokumen Gagal Karena Period Sudah dalam Frozen Zone";
		}
		
		return rs;
	}

}
