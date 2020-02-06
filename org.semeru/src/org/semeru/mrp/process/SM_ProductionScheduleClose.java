package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_MPS;
import org.semeru.mrp.model.X_SM_MPSLine;

public class SM_ProductionScheduleClose extends SvrProcess {

	private int p_SM_MPS_ID = 0;

	
	@Override
	protected void prepare() {
		
		
		p_SM_MPS_ID = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {
		
		if(p_SM_MPS_ID > 0) {
			
			X_SM_MPS prodSchedule = new X_SM_MPS(getCtx(), p_SM_MPS_ID, get_TrxName());
			prodSchedule.setProcessed(true);
			prodSchedule.set_CustomColumn("DocStatus", "CL");
			prodSchedule.saveEx();
			
			
			StringBuilder getLineData = new StringBuilder();
			getLineData.append(" SELECT SM_MPSLine_ID ");
			getLineData.append(" FROM SM_MPSLine ");
			getLineData.append(" WHERE SM_MPS_ID ="+ prodSchedule.getSM_MPS_ID());
			
			PreparedStatement pstmt = null;
	     	ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(getLineData.toString(), null);
					rs = pstmt.executeQuery();
					while (rs.next()) {
						int SM_MPSLine_ID = rs.getInt(1);

						if(SM_MPSLine_ID > 0) {
							
							X_SM_MPSLine line = new X_SM_MPSLine(getCtx(), SM_MPSLine_ID, get_TrxName());
							line.set_CustomColumn("Processed", true);
							line.saveEx();
							
						}
				    	   			 				  
					}

				} catch (SQLException err) {
					log.log(Level.SEVERE, getLineData.toString(), err);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
			
		}
		return null;
	}

}
