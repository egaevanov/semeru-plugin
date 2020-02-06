package org.semeru.process;

import java.util.logging.Level;

import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MUser;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;

public class SMR_ProcessWeb extends SvrProcess{
	
	private String p_IsActive = "";
	private String p_Name = "";
	private String p_Description = "";
//	private String p_Password = "";
//	private String p_EMail ="";
//	private String p_NotificationType ="";
//	private String p_Phone ="";
	private Integer p_AD_Org_ID = 0;
//	private Integer p_Process_ID = 0;
	private boolean Active = false;

	
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("Process_ID"))
//				p_Process_ID = (int)para[i].getParameterAsInt();
//			else if(name.equals("IsActive"))
				p_IsActive = (String)para[i].getParameterAsString();
			else if(name.equals("Name"))
				p_Name = (String)para[i].getParameterAsString();
			else if(name.equals("Description"))
				p_Description = (String)para[i].getParameterAsString();
			else if(name.equals("Password"))
//				p_Password = (String)para[i].getParameterAsString();
//			else if(name.equals("EMail"))
//				p_EMail = (String)para[i].getParameterAsString();
//			else if(name.equals("NotificationType"))
//				p_NotificationType = (String)para[i].getParameterAsString();	
//			else if(name.equals("Phone"))
//				p_Phone = (String)para[i].getParameterAsString();
//			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
		if(p_IsActive.equals("Y")){
			Active  = true;
		}
		
	}

	@Override
	protected String doIt() throws Exception {
		
		String rs = "";
		MOrg org = null;
		
		try {
			
			org = new MOrg(getCtx(), p_AD_Org_ID, get_TrxName());
			
			boolean isValid = DataSetupValidation.IsValidDataMaster(getAD_Client_ID(),p_AD_Org_ID, MOrg.Table_Name, MOrg.COLUMNNAME_Name, p_Name);
			
			if (isValid){
				
				org.setName(p_Name);
				org.setValue(p_Name);
				org.setDescription(p_Description);
				org.setIsActive(Active);
				org.saveEx();
				
				
				if(org != null){
					
					MOrgInfo orgInfo = new MOrgInfo(org);
					orgInfo.setAD_Org_ID(org.getAD_Org_ID());
					orgInfo.setIsActive(Active);
					orgInfo.setTaxID("-");			
					orgInfo.saveEx();

					MUser user  = new MUser(getCtx(), 0, get_TrxName());
					//user.setClientOrg(Env.getAD_Client_ID(getCtx()), org.getAD_Org_ID());
					user.setAD_Org_ID(org.getAD_Org_ID());
					user.saveEx();
					
				}
				
			}
			
			
		} catch (Exception e) {
			rs = "ERROR";
		}
		
		if(rs.equals("ERROR")){
			rollback();
		}
		
		return rs;
	}
	
	

}
