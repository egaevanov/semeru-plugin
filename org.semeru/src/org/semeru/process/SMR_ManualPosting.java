package org.semeru.process;

import java.util.logging.Level;

import org.compiere.model.MTable;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SMR_ManualPosting extends SvrProcess{
	
	
	private int p_AD_Client_ID = 0;
//	private int p_AD_Org_ID = 0;
	private int p_AD_Table_ID = 0;
	private String p_Type = "";
	private int	m_countReset = 0;

	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null);
			
			else if(name.equals("AD_Client_ID")) {
				p_AD_Client_ID = (int)para[i].getParameterAsInt();
			}else if(name.equals("AD_Org_ID")) {
//				p_AD_Org_ID = (int)para[i].getParameterAsInt();
			}else if(name.equals("AD_Table_ID")) {
				p_AD_Table_ID = (int)para[i].getParameterAsInt();
			}else if(name.equals("Type")) {
				p_Type = (String)para[i].getParameterAsString();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}		
	}

	@Override
	protected String doIt() throws Exception {
		MTable table = new MTable(getCtx(), p_AD_Table_ID, get_TrxName());
		String rs = "";
		if(p_Type.equals("R")) {
			reset(table.getTableName());
			rs = "@Updated@ = " + m_countReset ;
		}else if(p_Type.equals("P")) {
//			StringBuilder 
			
		}
			
		return rs;
	}

	
	private void reset (String TableName)
	{
		String sql = "UPDATE " + TableName
			+ " SET Processing='N' WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Processing<>'N' OR Processing IS NULL)";
		int unlocked = DB.executeUpdate(sql, get_TrxName());
		//
		sql = "UPDATE " + TableName
			+ " SET Posted='N' WHERE AD_Client_ID=" + p_AD_Client_ID
			+ " AND (Posted NOT IN ('Y','N') OR Posted IS NULL) AND Processed='Y'";
		int invalid = DB.executeUpdate(sql, get_TrxName());
		//
		if (unlocked + invalid != 0)
			if (log.isLoggable(Level.FINE)) log.fine(TableName + " - Unlocked=" + unlocked + " - Invalid=" + invalid);
		m_countReset += unlocked + invalid; 
	}	//	reset
	
}
