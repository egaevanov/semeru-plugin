package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.model.I_M_Brand;
import org.semeru.model.X_I_Brand_Upload;
import org.semeru.model.X_M_Brand;
import org.semeru.ws.model.SMR_Model_Brand;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class SMR_ProcessUploadBrand extends SvrProcess{

	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Process_ID = 0;
	
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
			else if(name.equals("process_id"))
				p_Process_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		
	}
		
	

	@Override
	protected String doIt() throws Exception {

		String rslt = "";
		
		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,File_Name,I_Brand_Upload_ID");
		SQLExtractJSon.append(" FROM  I_Brand_Upload ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND isupload = 'N' ");
		SQLExtractJSon.append(" AND Process_ID =  " +p_Process_ID);
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
		
		try {
			
			pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
			rs = pstmt.executeQuery();
			while (rs.next()) {
			
				String JSonString = rs.getString(3);
							
				Gson gson = new Gson();
				JsonArray json = new JsonArray();
				JsonParser parser = new JsonParser();
			    json = parser.parse(JSonString).getAsJsonArray();
			    
		    	SMR_Model_Brand[] DataMaps = gson.fromJson(json.toString(), SMR_Model_Brand[].class);
		    	int count = 0;
		    	
		    	
		    	for (SMR_Model_Brand DataMap : DataMaps) {
		    	
				X_M_Brand brand = new X_M_Brand(getCtx(), 0, get_TrxName());
				boolean isValid = DataSetupValidation.IsValidDataMaster(getAD_Client_ID(),p_AD_Org_ID, I_M_Brand.Table_Name, X_M_Brand.COLUMNNAME_Value, DataMap.Name);
				
				if(isValid) {
					brand.setAD_Org_ID(p_AD_Org_ID);
					
					if(DataMap.IsActive.equals("Y")) {
						brand.setIsActive(true);
					}else {
						brand.setIsActive(false);
	
					}
					brand.setName(DataMap.Name);
					brand.setValue(DataMap.Name);
					brand.saveEx();		
					count++;
		    	
			    	}
		    	}
		    	
		    	if(count > 0) {
		    		X_I_Brand_Upload itable = new X_I_Brand_Upload(getCtx(), rs.getInt(4), get_TrxName());
		    		itable.setisupload(true);
		    		itable.saveEx();
		    			
		    	}
	    	}
		} catch (Exception e) {
			rslt = "ERROR";
			rollback();
		}
		 finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}

		
		return rslt;
	}

}
