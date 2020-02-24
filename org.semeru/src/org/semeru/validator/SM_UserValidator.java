package org.semeru.validator;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.MBPartner;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.osgi.service.event.Event;

public class SM_UserValidator{

public static CLogger log = CLogger.getCLogger(SM_UserValidator.class);


public static String executeUserValidatorEvent(Event event, PO po) {
		
	String msgUser = "";
	MUser user = (MUser) po;
	if (event.getTopic().equals(IEventTopics.PO_AFTER_NEW) ||event.getTopic().equals(IEventTopics.PO_AFTER_CHANGE)) {
			
		msgUser = userBeforeSave(user);
	}
		
	return msgUser;

}


private static String userBeforeSave(MUser user) {
	
	String rs = "";
	
	if(user.getC_BPartner_ID() > 0) {
	
		MBPartner userBPInfo = new MBPartner(user.getCtx(), user.getC_BPartner_ID(), user.get_TrxName());
		if(userBPInfo.isSalesRep()) {
			StringBuilder updatePass = new StringBuilder();
			updatePass.append("UPDATE semerulite.sales ");
			updatePass.append(" SET passtring = '"+user.getPassword());
		
			System.out.println("asdasdas :"+user.getPassword());

		}
		
	}
	
	return rs;
}

}
