package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SMR_ProcessFinancialFunction extends SvrProcess{

	
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_C_Period_ID = 0;
	private int p_PA_ReportLineSet_ID = 0;
	private int p_Report = 0;

	//private final String ERROR = "ERROR";
	
	@Override
	protected void prepare() {
		//localprocess id = 1000118
		//uatprocess id = -

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Client_ID"))
				p_AD_Client_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("C_Period_ID"))
				p_C_Period_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("PA_ReportLineSet_ID"))
				p_PA_ReportLineSet_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("Report"))
				p_Report = (int)para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		
		
		StringBuilder SQLExecuteFinReport = new StringBuilder();
		SQLExecuteFinReport.append("SELECT f_financial_report(?,?,?,?,?)");

		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLExecuteFinReport.toString(), null);
				pstmt.setInt(1, p_AD_Client_ID);	
				pstmt.setInt(2, p_AD_Org_ID);	
				pstmt.setInt(3, p_C_Period_ID);	
				pstmt.setInt(4, p_PA_ReportLineSet_ID);	
				pstmt.setInt(5, p_Report);	

				rs = pstmt.executeQuery();
				while (rs.next()) {
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLExecuteFinReport.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		return "";
	}

}
