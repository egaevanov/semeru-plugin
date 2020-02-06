package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SM_MPSUpdate extends SvrProcess {

	
	private int p_SM_MPS_ID = 0;
	
	@Override
	protected void prepare() {

		
		p_SM_MPS_ID = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {
		
		
		StringBuilder SQLUpdateLine = new StringBuilder();
		SQLUpdateLine.append("SELECT update_mpsline(a.sm_mps_id, a.lotsizemethod, b.seq) ");
		SQLUpdateLine.append(" FROM sm_mps a, sm_mpsline b ");
		SQLUpdateLine.append(" WHERE a.sm_mps_id = b.sm_mps_id");
		SQLUpdateLine.append(" AND a.sm_mps_id = "+p_SM_MPS_ID);
		SQLUpdateLine.append("  ORDER BY b.seq");
		
		PreparedStatement pstmtLine = null;
     	ResultSet rsLine = null;
			try {
				pstmtLine = DB.prepareStatement(SQLUpdateLine.toString(), null);
				rsLine = pstmtLine.executeQuery();
				while (rsLine.next()) {
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLUpdateLine.toString(), err);
			} finally {
				DB.close(rsLine, pstmtLine);
				rsLine = null;
				pstmtLine = null;
			}
		

		
		StringBuilder SQLUpdateLineAtp = new StringBuilder();
		SQLUpdateLineAtp.append("SELECT update_mpsline_atp(a.sm_mps_id, a.lotsizemethod, b.seq) ");
		SQLUpdateLineAtp.append(" FROM sm_mps a, sm_mpsline b");
		SQLUpdateLineAtp.append("  WHERE a.sm_mps_id = b.sm_mps_id");
		SQLUpdateLineAtp.append(" AND a.sm_mps_id = "+p_SM_MPS_ID);
		SQLUpdateLineAtp.append("  ORDER BY b.seq");

		
		PreparedStatement pstmtLineAtp = null;
     	ResultSet rsLineAtp = null;
			try {
				pstmtLineAtp = DB.prepareStatement(SQLUpdateLineAtp.toString(), null);
				rsLineAtp = pstmtLineAtp.executeQuery();
				while (rsLineAtp.next()) {
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLUpdateLineAtp.toString(), err);
			} finally {
				DB.close(rsLineAtp, pstmtLineAtp);
				rsLineAtp = null;
				pstmtLineAtp = null;
			}
		
		
		return "";
	}

}
