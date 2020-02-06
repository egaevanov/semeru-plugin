package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.mrp.model.X_SM_MPS;
import org.semeru.mrp.model.X_SM_MPSLine;

public class SM_MPSCalcLine extends SvrProcess  {

	private int SM_MPS_ID = 0;

	
	@Override
	protected void prepare() {

		
		SM_MPS_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
		
		X_SM_MPS mps = new X_SM_MPS(getCtx(), SM_MPS_ID, get_TrxName());

		
		StringBuilder getFromView = new StringBuilder();
		getFromView.append(" SELECT SM_MPS_ID,seq,week_plan,date_plan,fdm,cor,pab,atp,mps,c_period_id ");
		getFromView.append(" FROM sm_mpsline_draft_v ");
		getFromView.append(" WHERE SM_MPS_ID ="+SM_MPS_ID);

		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(getFromView.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					X_SM_MPSLine MPSLine = new X_SM_MPSLine(getCtx(), 0, get_TrxName());
				
					MPSLine.setAD_Org_ID(mps.getAD_Org_ID());
					MPSLine.setSM_MPS_ID(rs.getInt(1));
					MPSLine.setseq(rs.getInt(2));
					MPSLine.setweek(rs.getInt(3));
					MPSLine.setmpsdate(rs.getTimestamp(4));
					MPSLine.setfrs(rs.getBigDecimal(5));
					MPSLine.setcor(rs.getBigDecimal(6));
					MPSLine.setpab(rs.getBigDecimal(7));
					MPSLine.setatp(rs.getBigDecimal(8));
					MPSLine.setmps(rs.getBigDecimal(9));
					MPSLine.setC_Period_ID(rs.getInt(10));

					
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
