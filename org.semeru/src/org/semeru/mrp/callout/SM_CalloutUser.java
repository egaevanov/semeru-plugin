package org.semeru.mrp.callout;

import java.util.Properties;

import org.adempiere.base.IColumnCallout;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.model.MUser;
import org.compiere.util.DB;

public class SM_CalloutUser extends CalloutEngine implements IColumnCallout {

	@Override
	public String start(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, Object oldValue) {
		
		if(mField.getColumnName().equals(MUser.COLUMNNAME_Password)){
			return calloutUserPass(ctx, WindowNo, mTab, mField, value);
		}			
		return null;
	}

	
	
public String calloutUserPass (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		String newPassword = (String)mTab.getValue("Password");
		String email = (String) mTab.getValue("EMail");
		Integer C_BPartner_ID = (Integer) mTab.getValue("C_BPartner_ID");
			
		if(C_BPartner_ID <= 0) {
			return"";
		}
		
		MBPartner bp = new MBPartner(ctx, C_BPartner_ID, null);
		
		
		if(bp.isSalesRep()) {
		
			StringBuilder updatePass = new StringBuilder();
			updatePass.append("UPDATE semerulite.sales ");
			updatePass.append(" SET passtring = '"+newPassword+"'");
			updatePass.append(" WHERE email = '"+email+"'");
			
			DB.executeUpdate(updatePass.toString(), null);
			
		}
			
				
	return"";
	
	}
	
}
