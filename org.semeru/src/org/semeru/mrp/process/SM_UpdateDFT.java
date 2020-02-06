package org.semeru.mrp.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SM_UpdateDFT extends SvrProcess{
	
	
	private int p_AD_Client_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_SM_MPS_ID = 0;


	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			else if(name.equals("AD_Client_ID"))
				p_AD_Client_ID = (int)para[i].getParameterAsInt();

			else if(name.equals("C_Period_ID"))
				p_C_Period_ID = (int)para[i].getParameterAsInt();
	
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}

		p_SM_MPS_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
			
//		StringBuilder SQLUpdateMPS = new StringBuilder();
//		SQLUpdateMPS.append("UPDATE SM_MPS ");
//		SQLUpdateMPS.append("SET isdtf = 'Y' ");
//		SQLUpdateMPS.append("WHERE SM_MPS_ID = "+p_SM_MPS_ID);
//		DB.executeUpdate(SQLUpdateMPS.toString(), null);	
		
		StringBuilder SQLUpdateMPSLine = new StringBuilder();
		SQLUpdateMPSLine.append("UPDATE SM_MPSLine ");
		SQLUpdateMPSLine.append(" SET isdtf = 'Y' ");
		SQLUpdateMPSLine.append(" WHERE SM_MPS_ID = "+p_SM_MPS_ID);
		SQLUpdateMPSLine.append(" AND C_Period_ID = "+p_C_Period_ID);
		SQLUpdateMPSLine.append(" AND AD_Client_ID = "+p_AD_Client_ID);
		DB.executeUpdate(SQLUpdateMPSLine.toString(), null);
		
		StringBuilder SQLUpdateColine = new StringBuilder();
		SQLUpdateColine.append("UPDATE SM_Coline ");
		SQLUpdateColine.append("SET isdtf = 'Y' ");
		SQLUpdateColine.append("WHERE SM_Coline_ID IN (SELECT  col.SM_Coline_ID ");
		SQLUpdateColine.append(" FROM sm_mps mps, sm_product_planline mpl,sm_coline col ");
		SQLUpdateColine.append(" WHERE  mps.sm_product_planline_id = mpl.sm_product_planline_id   ");
		SQLUpdateColine.append(" AND  mpl.sm_product_planline_id = col.sm_product_planline_id  ");
		SQLUpdateColine.append(" AND  col.C_period_ID ="+p_C_Period_ID );
		SQLUpdateColine.append(" AND  mps.sm_mps_id ="+ p_SM_MPS_ID+")");
		SQLUpdateMPSLine.append(" AND C_Period_ID = "+p_C_Period_ID);

		DB.executeUpdate(SQLUpdateColine.toString(), null);
	
		StringBuilder SQLUpdatePeriod = new StringBuilder();
		SQLUpdatePeriod.append("UPDATE C_Period ");
		SQLUpdatePeriod.append(" SET isdtf = 'Y' ");
		SQLUpdatePeriod.append(" WHERE C_Period_ID = "+p_C_Period_ID);
		SQLUpdateMPSLine.append(" AND AD_Client_ID = "+p_AD_Client_ID);
		DB.executeUpdate(SQLUpdatePeriod.toString(), null);
		
		StringBuilder SQLUpdateCustomerOrd= new StringBuilder();
		SQLUpdateCustomerOrd.append("UPDATE SM_CustOrd_Upload ");
		SQLUpdateCustomerOrd.append(" SET isdtf = 'Y' ");
		SQLUpdateCustomerOrd.append(" WHERE C_Period_ID = "+p_C_Period_ID);
		SQLUpdateCustomerOrd.append(" AND AD_Client_ID = "+p_AD_Client_ID);
		DB.executeUpdate(SQLUpdateCustomerOrd.toString(), null);
		
		
		return "";
	}

}
