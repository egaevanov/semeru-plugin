package org.semeru.mrp.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.semeru.mrp.model.X_SM_Product_Plan;

public class SM_ProductionPlanProcess extends SvrProcess{
	
	
	private String p_DocStatus = "";
	private int p_SM_Product_Plan_ID = 0;

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			
			else if(name.equals("SM_Product_Plan_ID"))
				p_SM_Product_Plan_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("DocStatus"))
				p_DocStatus = (String)para[i].getParameterAsString();
		
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		// TODO Auto-generated method stub
		
		if(p_SM_Product_Plan_ID > 0) {
			
			X_SM_Product_Plan prodPlan = new X_SM_Product_Plan(getCtx(), p_SM_Product_Plan_ID, get_TrxName());
			prodPlan.set_CustomColumn("DocStatus", p_DocStatus);
			prodPlan.saveEx();
			
		}
		
		return "";
	}

}
