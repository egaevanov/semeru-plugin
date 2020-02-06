package org.semeru.mrp.process;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SM_ProdPlanLineAutoCreateMPS  extends SvrProcess {
	
private int SM_Product_Plan_Line = 0;
	
	@Override
	protected void prepare() {
		
		SM_Product_Plan_Line = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLGenerateLine = new StringBuilder();
		SQLGenerateLine.append("SELECT f_mps("+SM_Product_Plan_Line+")");
		
		
		DB.executeUpdate(SQLGenerateLine.toString(), get_TrxName());
		
		return null;
	}
	
	

}
