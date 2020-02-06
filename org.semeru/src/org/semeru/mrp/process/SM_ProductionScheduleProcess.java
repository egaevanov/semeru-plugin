package org.semeru.mrp.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.semeru.mrp.model.X_SM_MPS;

public class SM_ProductionScheduleProcess extends SvrProcess {

	
	private String p_DocStatus = "";
	private int p_SM_MPS_ID = 0;
	
	@Override
	protected void prepare() {
		
		
		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			
			else if(name.equals("p_SM_MPS_ID"))
				p_SM_MPS_ID = (int)para[i].getParameterAsInt();
			else if(name.equals("DocStatus"))
				p_DocStatus = (String)para[i].getParameterAsString();
		
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		
	if(p_SM_MPS_ID > 0) {
			
			X_SM_MPS prodSchedule = new X_SM_MPS(getCtx(), p_SM_MPS_ID, get_TrxName());
			prodSchedule.set_CustomColumn("DocStatus", p_DocStatus);
			prodSchedule.saveEx();
			
		}
		
		return "";
	}

}
