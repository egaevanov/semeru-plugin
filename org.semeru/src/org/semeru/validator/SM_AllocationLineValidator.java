package org.semeru.validator;

import java.sql.Timestamp;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MAllocationLine;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;

public class SM_AllocationLineValidator {
	
public static CLogger log = CLogger.getCLogger(SM_AllocationLineValidator.class);
	
	public static String executeMAllocationLineEvent(Event event, PO po) {
		
		String msgAlloc = "";
		MAllocationLine Alloc = (MAllocationLine) po;
		if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW)) {
			
			msgAlloc = AllocAfterComplete(Alloc);
		}
		
	return msgAlloc;

	}
	
	
	private static String AllocAfterComplete(MAllocationLine Allocation) {
		String rs = "";
				
//		MAllocationHdr hdr = new MAllocationHdr(null, Allocation.getC_AllocationHdr_ID(),null);
		
		Timestamp now  = new Timestamp(System.currentTimeMillis());
		Allocation.setDateTrx(now);
		Allocation.saveEx();
		
		
		return rs;
	}

}
