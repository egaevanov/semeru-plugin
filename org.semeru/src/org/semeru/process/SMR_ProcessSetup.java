package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MLocator;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.semeru.model.X_C_POS;
import org.semeru.model.X_I_Setup_Temp;
import org.semeru.ws.model.SMR_Model_POSSetup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SMR_ProcessSetup extends SvrProcess {

	
	private int p_AD_Client_ID = 0;
	private int p_AD_Org_ID = 0;
	private int p_Process_ID = 0;
	private final String Error = "ERROR";
	
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
		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,possetup,I_Setup_Temp_ID,setuptype ");
		SQLExtractJSon.append(" FROM  I_Setup_Temp ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND Insert_Setup = 'N' ");
		SQLExtractJSon.append(" AND Process_ID =  " +p_Process_ID);
		
		String JSonString = "";
		int AD_Org_ID= 0;
		int I_Setup_Temp_ID = 0;
		String rslt = "";
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					AD_Org_ID = rs.getInt(2);
					JSonString = rs.getString(3);
					I_Setup_Temp_ID = rs.getInt(4);
					
					Gson gson = new Gson();
					JsonObject json = new JsonObject();
					JsonParser parser = new JsonParser();
				    json = parser.parse(JSonString).getAsJsonObject();
					
			    	SMR_Model_POSSetup data = gson.fromJson(json.toString(), SMR_Model_POSSetup.class);
			    	
					X_C_POS setup = new X_C_POS(getCtx(), data.C_Pos_ID, get_TrxName());
					setup.setAD_Org_ID(AD_Org_ID);
										
					setup.setName("POS"+data.StoreName);
					setup.setStoreName(data.StoreName);
					setup.setStoreAddress(data.StoreAddress);
					setup.setStorePhone(data.StorePhone);
					setup.setStoreInvNote(data.StoreInvNote);
//					setup.setM_Warehouse_ID(data.M_Warehouse_ID);
					setup.set_CustomColumn("M_Sales_Locator_ID", data.M_Sales_Locator_ID);
					setup.setM_Locator_ID(data.M_Locator_ID);
					
					MLocator loc = new MLocator(getCtx(), data.M_Locator_ID, get_TrxName());
					setup.setM_Warehouse_ID(loc.getM_Warehouse_ID());
					
					setup.setC_BankAccount_ID(data.C_BankAccount_ID);
					setup.setM_PriceList_ID(data.M_PriceList_ID);
					setup.saveEx();
					
					if(setup != null) {

						if(data.C_Pos_ID > 0) {
							rslt = "Edit Setup Berhasil";	

						}else {
							rslt = "Sync Setup Berhasil";	
						}
						
					}
						   			 				  
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLExtractJSon.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
						
			if(rslt.equals(Error)){
				rollback();
				
				X_I_Setup_Temp master = new X_I_Setup_Temp(getCtx(), I_Setup_Temp_ID, get_TrxName());
	 			master.setinsert_setup(false);
	 			master.set_CustomColumn("Result", "Proses Gagal");
	 			master.saveEx();
				
			}else{
			    	
				X_I_Setup_Temp master = new X_I_Setup_Temp(getCtx(), I_Setup_Temp_ID, get_TrxName());
 			 	master.setinsert_setup(true);
 			 	master.set_CustomColumn("Result", rslt);
 			 	master.saveEx();	   			 					    
			 
			 }   
			
		return "";
	}
	

}
