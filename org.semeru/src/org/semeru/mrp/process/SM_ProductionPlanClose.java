package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_Product_Plan;
import org.semeru.mrp.model.X_SM_Product_PlanLine;

public class SM_ProductionPlanClose extends SvrProcess {

	private int p_SM_Product_Plan_ID = 0;

	
	@Override
	protected void prepare() {
	
		
		p_SM_Product_Plan_ID = getRecord_ID();

		
		
	}

	@Override
	protected String doIt() throws Exception {

		if(p_SM_Product_Plan_ID > 0) {
			
			X_SM_Product_Plan prodPlan = new X_SM_Product_Plan(getCtx(), p_SM_Product_Plan_ID, get_TrxName());
			prodPlan.set_CustomColumn("DocStatus", "CL");
			prodPlan.setProcessed(true);
			prodPlan.saveEx();
			
			
			StringBuilder getRef_ID = new StringBuilder();
			getRef_ID.append(" SELECT SM_Product_PlanLine_ID ");
			getRef_ID.append(" FROM SM_Product_PlanLine ");
			getRef_ID.append(" WHERE SM_Product_Plan_ID ="+prodPlan.getSM_Product_Plan_ID());
			
			PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(getRef_ID.toString(), null);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int SM_ProductionPlanLine_ID = rs.getInt(1);

						if(SM_ProductionPlanLine_ID > 0) {
							
							X_SM_Product_PlanLine line = new X_SM_Product_PlanLine(getCtx(), SM_ProductionPlanLine_ID, get_TrxName());
							line.setProcessed(true);
							line.saveEx();
							
						}
				    	   			 				  
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, getRef_ID.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
			
		}
		
		
		return "";
	}

}
