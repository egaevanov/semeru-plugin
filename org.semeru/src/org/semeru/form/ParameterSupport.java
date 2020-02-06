package org.semeru.form;

import org.compiere.util.DB;

public class ParameterSupport {
	
	
	public static String getParam(int AD_Client_ID,String WhereClauseColumnName, String WhereClauseValue){
		
		String rs = "";
		
		StringBuilder SQLCheck = new StringBuilder();
		SQLCheck.append("SELECT Description ");
		SQLCheck.append(" FROM AD_Param ");
		SQLCheck.append(" WHERE AD_Client_ID = "+ AD_Client_ID );
		SQLCheck.append(" AND "+ WhereClauseColumnName + " ='"+WhereClauseValue+"'");

		rs = DB.getSQLValueStringEx(null, SQLCheck.toString());
			
		return rs;

	}
}