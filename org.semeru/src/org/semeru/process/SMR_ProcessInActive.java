package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MUser;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SMR_ProcessInActive extends SvrProcess {
	
	private int p_AD_Client_ID = 0;

	@Override
	protected void prepare() {

	}

	@Override
	protected String doIt() throws Exception {

		String rslt = "";
		final String Error = "ERROR";
		
		StringBuilder SQLGetDueDate = new StringBuilder();
		SQLGetDueDate.append("SELECT user_id,next_due_date ");
		SQLGetDueDate.append(" FROM mob_trx_contract ");
		SQLGetDueDate.append(" WHERE AD_Client_ID = "+ p_AD_Client_ID);
		SQLGetDueDate.append(" AND next_due_date < now()");
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetDueDate.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					int AD_User_ID = rs.getInt(1); 
					
					MUser user = new MUser(getCtx(), AD_User_ID, get_TrxName());
					user.setIsActive(false);
					user.saveEx();
					
				}			

			} catch (SQLException err) {
				
				log.log(Level.SEVERE, SQLGetDueDate.toString(), err);
				rslt = Error;
				
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		

		
		
		
		return rslt;
	}

}
