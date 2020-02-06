package org.semeru.mrp.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SM_ProdPlanLineAutoCreatePrdQty extends SvrProcess {

private int SM_Product_Plan_Line = 0;
	
	@Override
	protected void prepare() {
		
		SM_Product_Plan_Line = getRecord_ID();
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLGenerateLine = new StringBuilder();
		
		SQLGenerateLine.append("SELECT f_production_qty("+SM_Product_Plan_Line+")");
		
		
		PreparedStatement pstmt = null;
		ResultSet rslt = null;
		try
		{
			pstmt = DB.prepareStatement(SQLGenerateLine.toString(), null);
			rslt = pstmt.executeQuery();
			while (rslt.next()){
			} 
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, SQLGenerateLine.toString(), e);
			throw new Exception(e.getLocalizedMessage());
		}
		finally
		{
			DB.close(rslt, pstmt);
			pstmt = null;
		}
		
		
		return null;
	}
	
	
	
}
