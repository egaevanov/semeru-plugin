package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

public class SMR_TestProcess extends SvrProcess{
	//org.semeru.process.SMR_ProcessMasterData

	private int p_AD_Process_ID = 0;
	private String p_Function_Name = "";
	private int p_ParameterCount = 0;


	private ProcessInfoParameter[] para = null;
	private HashMap<String, String> ParaMap= new HashMap<>();


	
	
	@Override
	protected void prepare() {
		
		para = getParameter();
		p_AD_Process_ID = getProcessInfo().getAD_Process_ID();

		
		StringBuilder SQLGetPara = new StringBuilder();
		
		SQLGetPara.append("SELECT ColumnName,AD_Reference_ID ");
		SQLGetPara.append(" FROM AD_Process_Para");
		SQLGetPara.append(" WHERE AD_Process_ID = ?");
		SQLGetPara.append(" Order By SeqNo ASC");

			
		PreparedStatement pstmtPara = null;
     	ResultSet rsPara = null;
			try {
				pstmtPara = DB.prepareStatement(SQLGetPara.toString(), null);
				pstmtPara.setInt(1, p_AD_Process_ID);	
			
				rsPara = pstmtPara.executeQuery();
				while (rsPara.next()) {
					
					String ColumnName = rsPara.getString(1);
					Integer AD_Reference_ID = rsPara.getInt(2);
					String DataType = "";
					
					if(AD_Reference_ID == 19 || AD_Reference_ID == 11) {
						DataType = "Integer";
					}else if(AD_Reference_ID == 12) {
						DataType = "BigDecimal";
					}else if(AD_Reference_ID == 10) {
						DataType = "String";
					}
					
					
					ParaMap.put(ColumnName, DataType);
					
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetPara.toString(), err);
			} finally {
				DB.close(rsPara, pstmtPara);
				rsPara = null;
				pstmtPara = null;
			}
		
		
		for (int i = 0; i < para.length; i++){
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null) {
			;	
			}else if(name.equals("FunctionName")) {
				p_Function_Name = (String)para[i].getParameterAsString();
			}
//			else {
//				
//				String DataType = ParaMap.get(name);
//				if(DataType == "String") {
//					object[i-1] = para[i].getParameterAsString();
//				}else if(DataType == "Integer") {
//					object[i-1] = para[i].getParameterAsInt();
//				}else if(DataType == "BigDecimal") {
//					object[i-1] = para[i].getParameterAsBigDecimal();
//				}
//				System.out.println(object[i-1]);
//
//			}
	
		}
		
		
		p_ParameterCount = para.length -1;
		
		
	}
	@Override
	protected String doIt() throws Exception {
			
		
		StringBuilder SQLParamFunctionClause = new StringBuilder();
		StringBuilder ParamMark = new StringBuilder();

		String PreFix = "(";
		String SufFix = ")";
		
		if(p_ParameterCount == 0) {
			SQLParamFunctionClause.append(PreFix+SufFix);
		}else if(p_ParameterCount > 0) {
			for (int i = 1 ; i <= p_ParameterCount ; i++) {
				
				if(i != p_ParameterCount) {
					ParamMark.append("?,");
				}else if(i == p_ParameterCount){
					ParamMark.append("?");
				}		
			}
			
			SQLParamFunctionClause.append(PreFix+ParamMark+SufFix);

		}
		
		
		System.out.println(SQLParamFunctionClause.toString());
		
		StringBuilder SQLExecuteFunction = new StringBuilder();
		SQLExecuteFunction.append("SELECT "+p_Function_Name+SQLParamFunctionClause);
		
		System.out.println(SQLExecuteFunction.toString());

		
		for(int n = 0; n < para.length ; n++) {
			String name = para[n].getParameterName();

			String DataType = ParaMap.get(name);
			if(DataType == "String") {
				System.out.println("Param "+n+" : "+ para[n].getParameterAsString());
			}else if(DataType == "Integer") {	
				System.out.println("Param "+n +" : "+ para[n].getParameterAsInt());
			}else if(DataType == "BigDecimal") {
				System.out.println("Param "+n +" : "+ para[n].getParameterAsBigDecimal());

			}
		}
//		PreparedStatement pstmt = null;
//     	ResultSet rs = null;
//			try {
//				pstmt = DB.prepareStatement(SQLExecuteFunction.toString(), null);
//				for(int n = 1; n < para.length ; n++) {
//					String name = para[n].getParameterName();
//
//					String DataType = ParaMap.get(name);
//					if(DataType == "String") {
//						pstmt.setString(n,para[n].getParameterAsString());
//						System.out.println("Param : "+n+1 +" "+ para[n].getParameterAsString());
//					}else if(DataType == "Integer") {
//						pstmt.setInt(n,para[n].getParameterAsInt());
//						
//						System.out.println("Param : "+n+1 +" "+ para[n].getParameterAsInt());
//
//					}else if(DataType == "BigDecimal") {
//						pstmt.setBigDecimal(n,para[n].getParameterAsBigDecimal());
//						System.out.println("Param : "+n+1 +" "+ para[n].getParameterAsBigDecimal());
//
//					}
//					
//				}
//			
//				rs = pstmt.executeQuery();
//				while (rs.next()) {
//					
//				}
//
//			} catch (SQLException err) {
//				log.log(Level.SEVERE, SQLExecuteFunction.toString(), err);
//			} finally {
//				DB.close(rs, pstmt);
//				rs = null;
//				pstmt = null;
//			}
		
		return "";
				
	}
	

}
