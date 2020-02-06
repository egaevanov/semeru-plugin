package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SMR_ProcessMobileReport extends SvrProcess{
	
	
	private Integer p_AD_Client_ID = 0;
	private Integer p_AD_Org_ID = 0;
	private Integer param_01 = 0;
	private Integer param_02 = 0;
	private String param_03 = "";
	private String param_04 = "";
	private Timestamp param_05 = null;
	private Timestamp param_06 = null;
	private Integer p_AD_User_ID = 0;
	private Integer p_Report = 0;

	//private final String ERROR = "ERROR";

	@Override
	protected void prepare() {
		
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Client_ID"))
				p_AD_Client_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("AD_Org_ID"))
				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("n_param01"))
				param_01 = (int)para[i].getParameterAsInt();
			
			else if(name.equals("n_param02"))
				param_02 = (int)para[i].getParameterAsInt();
			
			else if(name.equals("n_param03"))
				param_03 = (String)para[i].getParameterAsString();
			
			else if(name.equals("n_param04"))
				param_04 = (String)para[i].getParameterAsString();
			
			else if(name.equals("n_param05"))
				param_05 = (Timestamp)para[i].getParameterAsTimestamp();
			
			else if(name.equals("n_param06"))
				param_06 = (Timestamp)para[i].getParameterAsTimestamp();
			
			else if(name.equals("AD_User_ID"))
				p_AD_User_ID = (int)para[i].getParameterAsInt();
			
			else if(name.equals("Report"))
				p_Report = (int)para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}

	@Override
	protected String doIt() throws Exception {
		StringBuilder SQLExecuteFinReport = new StringBuilder();
		SQLExecuteFinReport.append("SELECT f_mobile_report(");
		SQLExecuteFinReport.append("?,");
		SQLExecuteFinReport.append("?,");
		
		if(param_01 == 0 || param_01 == null){
			SQLExecuteFinReport.append("null,");
		}else{
			SQLExecuteFinReport.append(param_01+",");
		}
		
		if(param_02 == 0 || param_02 == null){
			SQLExecuteFinReport.append("null,");
		}else{
			SQLExecuteFinReport.append(param_02+",");
		}
		
		if(param_03 == "" || param_03 == null){
			SQLExecuteFinReport.append("null,");
		}else{
			SQLExecuteFinReport.append(param_03+",");
		}
		
		
		if(param_04 == "" || param_04 == null){
			SQLExecuteFinReport.append("null,");
		}else{
			SQLExecuteFinReport.append(param_04+",");
		}
		
		SQLExecuteFinReport.append("?,?,?,?)");


		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLExecuteFinReport.toString(), null);
				pstmt.setInt(1, p_AD_Client_ID);	
				pstmt.setInt(2, p_AD_Org_ID);
				pstmt.setTimestamp(3, param_05);	
				pstmt.setTimestamp(4, param_06);	
				pstmt.setInt(5, p_AD_User_ID);	
				pstmt.setInt(6, p_Report);	

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
