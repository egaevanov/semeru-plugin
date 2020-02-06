package org.semeru.mrp.process;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SM_ProdPlanAutoCreateLine extends SvrProcess{

	private int SM_Product_Plan = 0;
	
	@Override
	protected void prepare() {
		
		SM_Product_Plan = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLGenerateLine = new StringBuilder();
		SQLGenerateLine.append("SELECT f_product_planline("+SM_Product_Plan+")");
		
		
		DB.executeUpdate(SQLGenerateLine.toString(), get_TrxName());
		
		return null;
	}
	
	

}
