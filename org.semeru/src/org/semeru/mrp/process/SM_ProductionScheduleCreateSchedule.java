package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_MPSLine;

public class SM_ProductionScheduleCreateSchedule extends SvrProcess  {

//	private int SM_MPS_ID = 0;
	private String ScheduleMethod ="";

	
	@Override
	protected void prepare() {

		
		
		ProcessInfoParameter[] para = getParameter();

		for (int i = 0 ; i < para.length ;i++){
			
			String name = para[i].getParameterName();
			
			if(para[i].getParameter()==null)
				;
			
			else if(name.equals("ScheduleMethod"))
				ScheduleMethod = (String)para[i].getParameterAsString();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
			
		}
		
//		SM_MPS_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
		
		
		StringBuilder getFromView = new StringBuilder();
		getFromView.append(" SELECT SM_ProductionPlanLine_ID,SM_FactoryPlan_ID,MachineNo,LOTNumber,SM_OperatorTeam_ID,Week,StartDate,EndDate,Qty,FinishedQty ");
		getFromView.append(" FROM view sm_mpsline_draft_v ");
		getFromView.append(" WHERE ScheduleMethod = '"+ScheduleMethod+"'");

//		X_SM_MPS mps = new X_SM_MPS(getCtx(), 0, get_TrxName());
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(getFromView.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					X_SM_MPSLine MPSLine = new X_SM_MPSLine(getCtx(), 0, get_TrxName());
				
//					MPSLine.setAD_Org_ID(mps.getAD_Org_ID());
//					MPSLine.setSM_MPS_ID(mps.getSM_MPS_ID());
//					MPSLine.setSM_ProductionPlanLine_ID(rs.getInt(1));
//					MPSLine.setName("MPS Line ");
//					MPSLine.setDescription("MPS Line ");
//					MPSLine.setSM_FactoryPlan_ID(rs.getInt(2));
//					MPSLine.setMachineNo(rs.getBigDecimal(3));
//					MPSLine.setLOTNumber(rs.getBigDecimal(4));
//					MPSLine.setSM_OperatorTeam_ID(rs.getInt(5));
//					MPSLine.setWeek(rs.getBigDecimal(6));
//					MPSLine.setStartDate(rs.getTimestamp(7));
//					MPSLine.setEndDate(rs.getTimestamp(8));
//					MPSLine.setQty(rs.getBigDecimal(9));
//					MPSLine.setQtyFinished(rs.getBigDecimal(10));
					
					MPSLine.saveEx();

					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, getFromView.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		
		return "";
	}

}
