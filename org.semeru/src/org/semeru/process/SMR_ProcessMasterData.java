package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import org.compiere.model.I_C_UOM;
import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MAttributeSet;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBank;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCity;
import org.compiere.model.MClient;
import org.compiere.model.MCost;
import org.compiere.model.MCostElement;
import org.compiere.model.MElementValue;
import org.compiere.model.MLocation;
import org.compiere.model.MLocator;
import org.compiere.model.MMailText;
import org.compiere.model.MProduct;
import org.compiere.model.MProductPrice;
import org.compiere.model.MUOM;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.model.MWarehouse;
import org.compiere.model.X_C_UOM;
import org.compiere.model.X_M_ProductPrice;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.semeru.model.I_M_Brand;
import org.semeru.model.X_C_Pos_Setup;
import org.semeru.model.X_I_Master_Temp;
import org.semeru.model.X_M_Brand;
import org.semeru.model.X_M_Sales_Locator;
import org.semeru.ws.model.SMR_Model_BPartner;
import org.semeru.ws.model.SMR_Model_Bank;
import org.semeru.ws.model.SMR_Model_Brand;
import org.semeru.ws.model.SMR_Model_Locator;
import org.semeru.ws.model.SMR_Model_POSSetup;
import org.semeru.ws.model.SMR_Model_ProductPricing;
import org.semeru.ws.model.SMR_Model_SalesLocator;
import org.semeru.ws.model.SMR_Model_UOM;
import org.semeru.ws.model.SMR_Model_User;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SMR_ProcessMasterData extends SvrProcess{

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
			
			else if(name.equals("Process_ID"))
				p_Process_ID = (int)para[i].getParameterAsInt();
			
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
	}

	@Override
	protected String doIt() throws Exception {

		StringBuilder SQLExtractJSon = new StringBuilder();
		SQLExtractJSon.append("SELECT AD_Client_ID,AD_Org_ID,Master,I_Master_Temp_ID,Pos ");
		SQLExtractJSon.append(" FROM  I_Master_Temp ");
		SQLExtractJSon.append(" WHERE AD_Client_ID = " + p_AD_Client_ID );
		SQLExtractJSon.append(" AND AD_Org_ID = " + p_AD_Org_ID);
		SQLExtractJSon.append(" AND Insert_Master = 'N' ");
		SQLExtractJSon.append(" AND Process_ID =  " +p_Process_ID);
		
		String JSonString = "";
		int AD_Client_ID = 0;
		int AD_Org_ID= 0;
		int I_Master_Temp_ID = 0;
		String rslt = "";
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLExtractJSon.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					AD_Client_ID = rs.getInt(1);
					AD_Org_ID = rs.getInt(2);
					JSonString = rs.getString(3);
					I_Master_Temp_ID = rs.getInt(4);
					String posType= rs.getString(5);		
					
					Gson gson = new Gson();
					JsonObject json = new JsonObject();
					JsonParser parser = new JsonParser();
				    json = parser.parse(JSonString).getAsJsonObject();
					
				    if(posType.equals("M")){
				    	SMR_Model_Brand data = gson.fromJson(json.toString(), SMR_Model_Brand.class);
						rslt = createBrand(AD_Client_ID, AD_Org_ID, data);
					
				    }else if(posType.equals("U")){
				    	SMR_Model_UOM data = gson.fromJson(json.toString(), SMR_Model_UOM.class);
						rslt = createUOM(AD_Client_ID, AD_Org_ID, data);
	
				    }else if(posType.equals("B")){
				    	SMR_Model_Bank data = gson.fromJson(json.toString(), SMR_Model_Bank.class);
						rslt = createBankAccount(AD_Client_ID, AD_Org_ID, data);

				    }else if(posType.equals("A")){
				    	//Done UT
				    	SMR_Model_BPartner data = gson.fromJson(json.toString(), SMR_Model_BPartner.class);
						rslt = createBPartner(AD_Client_ID, AD_Org_ID, data);

				    }else if(posType.equals("L")){
				    	SMR_Model_Locator data = gson.fromJson(json.toString(), SMR_Model_Locator.class);
						rslt = createLocator(AD_Client_ID, AD_Org_ID, data);

				    }else if(posType.equals("S")){

				    	SMR_Model_SalesLocator data = gson.fromJson(json.toString(), SMR_Model_SalesLocator.class);
						rslt = createSalesLocator(AD_Client_ID, AD_Org_ID, data);

				    }else if(posType.equals("P")){

				    	SMR_Model_ProductPricing data = gson.fromJson(json.toString(), SMR_Model_ProductPricing.class);
				    	rslt = createProductPricing(AD_Client_ID, AD_Org_ID, data);

				    }else if(posType.equals("T")){
				    	SMR_Model_POSSetup data = gson.fromJson(json.toString(), SMR_Model_POSSetup.class);

				    	rslt = CreatePOSSetup(AD_Client_ID, AD_Org_ID, data);
				    }else if(posType.equals("C")){
				    	SMR_Model_User data = gson.fromJson(json.toString(), SMR_Model_User.class);
				    	
				    	rslt = ChangePassword(AD_Client_ID, AD_Org_ID, data);
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
				
				X_I_Master_Temp master = new X_I_Master_Temp(getCtx(), I_Master_Temp_ID, get_TrxName());
	 			master.setinsert_master(false);
	 			master.set_CustomColumn("Result", "Proses Gagal");
	 			master.saveEx();
				
			}else{
			    	
			    X_I_Master_Temp master = new X_I_Master_Temp(getCtx(), I_Master_Temp_ID, get_TrxName());
 			 	master.setinsert_master(true);
 			 	master.set_CustomColumn("Result", rslt);
 			 	master.saveEx();	   			 					    
			 
			 }   
			
		return "";
	}

	
	//Create Brand Document
	private String createBrand(int AD_Client_ID,int AD_Org_ID,SMR_Model_Brand data){
		
		String rs = "";
		
		try {
			
			boolean isValid = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, I_M_Brand.Table_Name, X_M_Brand.COLUMNNAME_Value, data.Name);
					
			boolean isValidEdit = true;
			if(isValid || data.M_Brand_ID > 0 ){
				
				X_M_Brand brand = new X_M_Brand(getCtx(), data.M_Brand_ID, get_TrxName());
				String p_brandName = data.Name;
//				String brandName = brand.getName();


				
				if(data.M_Brand_ID > 0) {
				
					isValidEdit = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, I_M_Brand.Table_Name, X_M_Brand.COLUMNNAME_Name, data.Name);
					
					if(!isValidEdit) {
						
						rs = "Produk "+data.Name+" Sudah Terdaftar";
						return rs;
					}
				}
				
				//brand.setClientOrg(AD_Client_ID, AD_Org_ID);
				brand.setAD_Org_ID(AD_Org_ID);	
				if(data.IsActive.equals("Y")){
			   	 	brand.setIsActive(true);
				}else if(data.IsActive.equals("N")){
			   	 	brand.setIsActive(false);
				}
				
				
				if(isValidEdit && p_brandName != null) {
					brand.setName(data.Name);
					brand.setValue(data.Name);
				}
			 	brand.saveEx();
				 	
				if(brand!=null && data.M_Brand_ID == 0){
					rs = "Penambahan Brand Berhasil";			
				}else if(brand!=null && data.M_Brand_ID > 0) {
					rs = "Edit Brand Berhasil";			

				}	
			}else{			
				rs = "Brand Yang Anda Tambahkan Sudah Terdaftar, Silahkan Refresh Aplikasi";		
			}
		
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;
		
	}
	
	private String createUOM(int AD_Client_ID,int AD_Org_ID,SMR_Model_UOM data){
		
		String rs = "";
		
		try {
			
			boolean isValid = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, I_C_UOM.Table_Name, X_C_UOM.COLUMNNAME_Name, data.Name);
	
			if(isValid || data.C_UOM_ID > 0){
				
				MUOM UOM = new MUOM(getCtx(), data.C_UOM_ID, get_TrxName());
				
				//UOM.setClientOrg(AD_Client_ID, AD_Org_ID);
				UOM.setAD_Org_ID(AD_Org_ID);	
				if(data.IsActive.equals("Y")){
					UOM.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					UOM.setIsActive(false);
				}
				
				UOM.setName(data.Name);
				UOM.setDescription(data.Name);
				UOM.setStdPrecision(2);
				UOM.setCostingPrecision(2);
				UOM.setX12DE355(data.Name);
				UOM.setUOMSymbol(data.Name);
				UOM.saveEx();
				 	
				if(UOM!=null && data.C_UOM_ID == 0){		
					rs = "Penambahan UOM Berhasil";		
				} else if(UOM!=null && data.C_UOM_ID > 0) {
					rs = "Edit UOM Berhasil";			

				}
			}else{
				rs = "UOM Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data UOM";
			}
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;
		
	}
	
	private String createBankAccount(int AD_Client_ID,int AD_Org_ID,SMR_Model_Bank data){
		String rs = "";
		
		boolean IsNew = true; 


		try {

			if(data.C_Bank_ID > 0){
				IsNew = false;
			}
			
			boolean IsValidBank = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, MBank.Table_Name, MBank.COLUMNNAME_Name, data.Name);
			boolean IsValidBankAcct = DataSetupValidation.IsValidDataMaster(AD_Client_ID, AD_Org_ID,MBankAccount.Table_Name, MBankAccount.COLUMNNAME_Value, data.Name);
	
			if((IsValidBank && IsValidBankAcct) || (data.C_Bank_ID > 0 && data.C_BankAccount_ID > 0)){
				
				MBank bank = new MBank(getCtx(), data.C_Bank_ID, get_TrxName());
				MBankAccount bankAcct = new MBankAccount(getCtx(), data.C_BankAccount_ID, get_TrxName());
				
				if(data.IsActive.equals("Y")){
					bank.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					bank.setIsActive(false);
				}
				
				//bank.setClientOrg(AD_Client_ID, AD_Org_ID);
				bank.setAD_Org_ID(AD_Org_ID);
				bank.setName(data.Name);
				bank.setDescription(data.Name);
				bank.setIsOwnBank(true);
				bank.setRoutingNo("1");
				bank.saveEx();
				
				if(bank!= null){
					
					//bankAcct.setClientOrg(AD_Client_ID, AD_Org_ID);
					bankAcct.setAD_Org_ID(AD_Org_ID);
					if(data.IsActive.equals("Y")){
						bankAcct.setIsActive(true);
					}else if(data.IsActive.equals("N")){
						bankAcct.setIsActive(false);
					}
					bankAcct.setC_Bank_ID(bank.getC_Bank_ID());
					bankAcct.setValue(data.Name);
					bankAcct.setName(data.Name);
					bankAcct.setAccountNo(data.AccountNo);
					bankAcct.setBankAccountType(data.BankAccountType);
					bankAcct.setC_Currency_ID(data.C_Currency_ID);
					bankAcct.setCurrentBalance(Env.ZERO);
					bankAcct.saveEx();
				}
				
				
				if(bankAcct != null && IsNew){
					
					Integer C_AcctSchema_ID = DataSetupValidation.getID(AD_Client_ID, MAcctSchema.Table_Name, "AD_Org_ID", 0, null);
					
					MElementValue ElementValue = new MElementValue(getCtx(), 0, get_TrxName());
					
					String SetElementValue = DataSetupValidation.getValueElement(AD_Client_ID);
					
					StringBuilder SQLGetElement = new StringBuilder();
					SQLGetElement.append("SELECT C_Element_ID");
					SQLGetElement.append(" FROM C_Element ");
					SQLGetElement.append(" WHERE AD_Client_ID = "+ AD_Client_ID );
					
					Integer Element_ID = DB.getSQLValueEx(null, SQLGetElement.toString());
					
					
					ElementValue.setValue(SetElementValue);
					ElementValue.setAD_Org_ID(0);
					ElementValue.setName(data.Name);
					ElementValue.setAccountType(MElementValue.ACCOUNTTYPE_Asset);
					ElementValue.setAccountSign(MElementValue.ACCOUNTSIGN_Natural);
					ElementValue.setPostActual(true);
					ElementValue.setPostBudget(true);
					ElementValue.setPostStatistical(true);
					ElementValue.setC_Element_ID(Element_ID);
					ElementValue.saveEx();		
					
					MAccount newAccount = new MAccount (getCtx(), 0, get_TrxName());
					//newAccount.setClientOrg(AD_Client_ID, 0);
					newAccount.setAD_Org_ID(AD_Org_ID);
					newAccount.setC_AcctSchema_ID(C_AcctSchema_ID);
					newAccount.setAccount_ID(ElementValue.getC_ElementValue_ID());	
					newAccount.saveEx();
					
					StringBuilder SQLUpdateBankAcct = new StringBuilder();
					SQLUpdateBankAcct.append("UPDATE C_BankAccount_Acct SET B_Asset_Acct = "+newAccount.getC_ValidCombination_ID());
					SQLUpdateBankAcct.append(" , B_Intransit_Acct = "+newAccount.getC_ValidCombination_ID());
					SQLUpdateBankAcct.append(" WHERE AD_Client_ID = "+ newAccount.getAD_Client_ID());
					SQLUpdateBankAcct.append(" AND C_BankAccount_ID = "+ bankAcct.getC_BankAccount_ID());
					SQLUpdateBankAcct.append(" AND C_AcctSchema_ID = "+ C_AcctSchema_ID);
					DB.executeUpdate(SQLUpdateBankAcct.toString(), get_TrxName());
					
					StringBuilder SQLGetParent = new StringBuilder();
					SQLGetParent.append("SELECT C_ElementValue_ID ");
					SQLGetParent.append(" FROM C_ElementValue ");
					SQLGetParent.append(" WHERE Name = 'Bank' ");
					SQLGetParent.append(" AND AD_Client_ID = "+ AD_Client_ID);
					Integer parent_ID = DB.getSQLValueEx(get_TrxName(), SQLGetParent.toString());	
					     
					StringBuilder SQLUpdateTreeNode = new StringBuilder();
					SQLUpdateTreeNode.append("UPDATE AD_TreeNode");
					SQLUpdateTreeNode.append(" SET Parent_ID = " + parent_ID);
					SQLUpdateTreeNode.append(" WHERE Node_ID = " + ElementValue.getC_ElementValue_ID());
					DB.executeUpdate(SQLUpdateTreeNode.toString(), get_TrxName());
	
				}
				
				if(bankAcct!= null && data.C_Bank_ID == 0 && data.C_BankAccount_ID==0 ){			
					rs = "Penambahan Bank Berhasil";	
				}else if(bankAcct!= null && data.C_Bank_ID > 0 && data.C_BankAccount_ID > 0 ){			
					rs = "Edit Bank Berhasil";	
				}
			}else{
				rs = "Bank Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data Bank";			
			}
		
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;

	}
	
	
	private String createBPartner(int AD_Client_ID,int AD_Org_ID,SMR_Model_BPartner data){
		String rs = "";

		try {
			
			boolean isValid = DataSetupValidation.IsValidDataMaster(AD_Client_ID, AD_Org_ID,MBPartner.Table_Name, MBPartner.COLUMNNAME_Value, data.Name);
	
			if(isValid || data.C_BPartner_ID > 0){
				MBPartner bp = new MBPartner(getCtx(), data.C_BPartner_ID, get_TrxName());
				
				bp.setClientOrg(AD_Client_ID, AD_Org_ID);
				if(data.IsVendor.equals("Y")){
					bp.setIsVendor(true);
				}else if(data.IsVendor.equals("N")){
					bp.setIsVendor(false);
				}
				
				if(data.IsCustomer.equals("Y")){
					bp.setIsCustomer(true);
				}else if(data.IsCustomer.equals("N")){
					bp.setIsCustomer(false);
				}
				
				
				if(data.IsActive.equals("Y")){
					bp.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					bp.setIsActive(false);
				}
				
				bp.setValue(data.Name);
				bp.setName(data.Name);
				System.out.println(data.CreditLimit);
				bp.setSO_CreditLimit(data.CreditLimit);
				bp.setC_PaymentTerm_ID(DataSetupValidation.getDefaultPaymentTerm(AD_Client_ID));
				bp.saveEx();
				
				if(bp!= null){
					
					MLocation location = new MLocation(getCtx(), data.C_Location_ID, get_TrxName());
					//location.setClientOrg(AD_Client_ID, AD_Org_ID);
					location.setAD_Org_ID(AD_Org_ID);
					if(data.IsActive.equals("Y")){
						location.setIsActive(true);
					}else if(data.IsActive.equals("N")){
						location.setIsActive(false);
					}
					location.setAddress1(data.Address1);
					location.setC_Country_ID(209);
					System.out.println(data.Postal);
					location.setPostal(data.Postal);
					location.setC_City_ID(data.C_City_ID);
					location.saveEx();
					
					
					if(location!= null){
						MBPartnerLocation BpLoc = new MBPartnerLocation(getCtx(), data.C_BPartner_Location_ID, get_TrxName());
						if(data.IsActive.equals("Y")){
							BpLoc.setIsActive(true);
						}else if(data.IsActive.equals("N")){
							BpLoc.setIsActive(false);
						}
						
						BpLoc.setC_BPartner_ID(bp.getC_BPartner_ID());
						BpLoc.setC_Location_ID(location.getC_Location_ID());
						System.out.println(data.Phone);
						BpLoc.setPhone(data.Phone);
						System.out.println(data.Phone2);
						BpLoc.setPhone2(data.Phone2);
						BpLoc.setIsShipTo(true);
						BpLoc.setIsPayFrom(true);
						//BpLoc.set_CustomColumnReturningBoolean("IsTax", true);
						BpLoc.setIsBillTo(true);
						BpLoc.setIsRemitTo(true);
						BpLoc.saveEx();			
					
					}
				}
				
				if(bp!= null && data.C_BPartner_ID == 0){
					rs = "Penambahan Bussines Partner Berhasil";	
				}else if(bp!= null && data.C_BPartner_ID > 0){
					rs = "Edit Bussines Partner Berhasil";	
				}
			}else{
				rs = "Mitra Bisnis Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data Mitra Bisnis";		
			}
		
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;

	}
	
	private String createLocator(int AD_Client_ID,int AD_Org_ID,SMR_Model_Locator data){
		
		String rs = "";
		
		try {
			
			boolean isValidLoc = DataSetupValidation.IsValidDataMaster(AD_Client_ID, AD_Org_ID,MLocator.Table_Name, MLocator.COLUMNNAME_Value, data.Name);
			boolean isValidWH = DataSetupValidation.IsValidDataMaster(AD_Client_ID, AD_Org_ID,MWarehouse.Table_Name, MWarehouse.COLUMNNAME_Value, data.Name);
			
			if((isValidLoc && isValidWH) || (data.M_Warehouse_ID > 0 && data.C_Location_ID > 0) ){
				
				MLocation location = new MLocation(getCtx(), data.C_Location_ID, get_TrxName());
				
				//location.setClientOrg(AD_Client_ID, AD_Org_ID);
				location.setAD_Org_ID(AD_Org_ID);
				if(data.IsActive.equals("Y")){
					location.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					location.setIsActive(false);
				}
				
				MCity city = new MCity(getCtx(), data.C_City_ID, get_TrxName());
				
				location.setAddress1(city.getName());
				location.setC_Country_ID(209);
				location.setPostal(city.getPostal());
				location.setC_City_ID(data.C_City_ID);
				location.saveEx();
				
				if(location != null){
				
					MWarehouse wh = new MWarehouse(getCtx(), data.M_Warehouse_ID, get_TrxName());
					
					//wh.setClientOrg(AD_Client_ID, AD_Org_ID);
						wh.setAD_Org_ID(AD_Org_ID);				
					if(data.M_Warehouse_ID <= 0){
					
						wh.setName(data.Name);
						wh.setValue(data.Name);
						wh.setDescription(data.Name);
					
					}
					
					wh.setC_Location_ID(location.getC_Location_ID());
					if(data.IsDisallowNegativeInv.equals("Y")){
						wh.setIsDisallowNegativeInv(true);
					}else if(data.IsDisallowNegativeInv.equals("N")){
						wh.setIsDisallowNegativeInv(false);
					}
					wh.setSeparator("*");
					wh.saveEx();
					 	
					if(wh != null){
						MLocator locator = new MLocator(getCtx(), data.M_Locator_ID, get_TrxName());
						//locator.setClientOrg(AD_Client_ID, AD_Org_ID);
					locator.setAD_Org_ID(AD_Org_ID);
						if(data.IsActive.equals("Y")){
							locator.setIsActive(true);
						}else if(data.IsActive.equals("N")){
							locator.setIsActive(false);
						}
						
						locator.setM_Warehouse_ID(wh.getM_Warehouse_ID());
						locator.setValue(data.Name);
						locator.setIsDefault(true);
						locator.setPriorityNo(10);
						
						String sec = DataSetupValidation.getXYZLocator(AD_Client_ID, data.M_Warehouse_ID);
						System.out.println(sec);
						locator.setX(sec);
						locator.setY(sec);
						locator.setZ(sec);
						locator.saveEx();
		
						if(locator != null&&data.M_Warehouse_ID == 0 && data.C_Location_ID == 0){
							rs = "Penambahan Lokasi Berhasil";	
						}else if(locator != null&&data.M_Warehouse_ID > 0 && data.C_Location_ID > 0){
							rs = "Edit Lokasi Berhasil";	
						}
									
					}
					
				}
				
			}else {			
				rs = "Lokasi Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data Lokasi";
			}
			
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;
		
	}
	
	
	
	private String createSalesLocator(int AD_Client_ID,int AD_Org_ID,SMR_Model_SalesLocator data){
		
		String rs = "";
		try {
				
			boolean IsValid = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, X_M_Sales_Locator.Table_Name, X_M_Sales_Locator.COLUMNNAME_Name, data.Name);
	
			if(IsValid || data.M_Sales_Locator_ID > 0){
			
				X_M_Sales_Locator SalesLocator = new X_M_Sales_Locator(getCtx(), data.M_Sales_Locator_ID, get_TrxName());
				
				//SalesLocator.setClientOrg(AD_Client_ID, AD_Org_ID);
				SalesLocator.setAD_Org_ID(AD_Org_ID);	
				if(data.IsActive.equals("Y")){
					SalesLocator.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					SalesLocator.setIsActive(false);
				}
				
				SalesLocator.setName(data.Name);
				SalesLocator.setAddress1(data.Address1);
				SalesLocator.setAddress2(data.Address2);
				SalesLocator.setDescription(data.Description);
				SalesLocator.setValue(data.Name);
				SalesLocator.saveEx();
					
				if(SalesLocator != null && data.M_Sales_Locator_ID == 0){			
					rs = "Penambahan Lokasi Penjualan Berhasil";	
				}else if(SalesLocator != null && data.M_Sales_Locator_ID > 0){			
					rs = "Edit Lokasi Penjualan Berhasil";	
				}
			
			} else{
				rs = "Lokasi Penjualan Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data Lokasi Penjualan";
			}
	 	
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;
		
	}
	
	private String createProductPricing(int AD_Client_ID,int AD_Org_ID,SMR_Model_ProductPricing data){
		boolean IsNew = true; 
		String rs = "";
		boolean isValid = true;
		
		try {
			
			if(data.M_Product_ID > 0){
				IsNew = false;
			}
			
			
			if(IsNew){
				isValid = DataSetupValidation.IsValidDataMaster(AD_Client_ID,AD_Org_ID, MProduct.Table_Name, MProduct.COLUMNNAME_Value, data.Value);
			}
				
			if(isValid || data.M_Product_ID > 0){
				
				MProduct product = new MProduct(getCtx(), data.M_Product_ID, get_TrxName());
				
				//product.setClientOrg(AD_Client_ID, AD_Org_ID);
				product.setAD_Org_ID(AD_Org_ID);	
				if(data.IsActive.equals("Y")){
					product.setIsActive(true);
				}else if(data.IsActive.equals("N")){
					product.setIsActive(false);
				}
				
				product.setValue(data.Value);
				product.setName(data.Name);
				product.setDescription(data.Name);
				product.set_CustomColumn("M_Brand_ID", data.M_Brand_ID);
				product.setIsStocked(true);
				product.setM_Product_Category_ID(data.M_Product_Category_ID);
				product.setC_TaxCategory_ID(data.C_TaxCategory_ID);
				product.setC_UOM_ID(data.C_UOM_ID);
				product.setProductType(data.ProductType);
				product.setIsPurchased(true);
				product.setIsSold(true);
				product.setImageURL(data.ImageUrl);
				if(data.IsAttributeSet.equals("Y")){
					
					MAttributeSet AttributeSet = new MAttributeSet(getCtx(), data.M_AttributeSet_ID, get_TrxName());
					//AttributeSet.setClientOrg(AD_Client_ID, AD_Org_ID);
					AttributeSet.setAD_Org_ID(AD_Org_ID);
					if(data.IsActive.equals("Y")){
						AttributeSet.setIsActive(true);
					}else if(data.IsActive.equals("N")){
						AttributeSet.setIsActive(false);
					}
					
					AttributeSet.setName(data.Name);
					AttributeSet.setMandatoryType("S");
					AttributeSet.setIsInstanceAttribute(true);
					AttributeSet.setIsSerNo(true);
					AttributeSet.set_CustomColumnReturningBoolean("IsUniqueAttributeSet", true);
					AttributeSet.saveEx();
					if(AttributeSet !=  null){
						product.setM_AttributeSet_ID(AttributeSet.getM_AttributeSet_ID());
					}
					
				}
				
				product.saveEx();
							
				if(IsNew){
					
					Integer elementid = DataSetupValidation.getID(getAD_Client_ID(), MCostElement.Table_Name, MCostElement.COLUMNNAME_CostingMethod, 0, MCostElement.COSTINGMETHOD_AveragePO);
					Integer asid = DataSetupValidation.getID(getAD_Client_ID(), MAcctSchema.Table_Name, MAcctSchema.COLUMNNAME_CostingMethod, 0, MAcctSchema.COSTINGMETHOD_AveragePO);
					
				
					
					MProductPrice pricingBuy = new MProductPrice(getCtx(), 0 , get_TrxName());		
					MProductPrice pricingRtl = new MProductPrice(getCtx(), 0 , get_TrxName());			
					MProductPrice pricingRgs = new MProductPrice(getCtx(), 0 , get_TrxName());
					
					pricingBuy.setM_PriceList_Version_ID(data.M_PriceList_Version_Buy_ID);
					pricingBuy.setM_Product_ID(product.getM_Product_ID());
					pricingBuy.setPriceList(data.PriceList_Buy);
					pricingBuy.setPriceStd(data.PriceList_Buy);
					pricingBuy.setPriceLimit(data.PriceList_Buy);
					pricingBuy.saveEx();
					
					MAcctSchema as = new MAcctSchema(getCtx(),asid , get_TrxName());					
					MCost cost = new MCost(product, 0, as, 0, elementid);
					cost.setCurrentCostPrice(data.PriceList_Buy);
					cost.saveEx();
					
					pricingRtl.setM_PriceList_Version_ID(data.M_PriceList_Version_Rtl_ID);
					pricingRtl.setM_Product_ID(product.getM_Product_ID());
					pricingRtl.setPriceList(data.PriceList_Rtl);
					pricingRtl.setPriceStd(data.PriceList_Rtl);
					pricingRtl.setPriceLimit(data.PriceLimit_Rtl);
					pricingRtl.saveEx();
					
					pricingRgs.setM_PriceList_Version_ID(data.M_PriceList_Version_Rgs_ID);
					pricingRgs.setM_Product_ID(product.getM_Product_ID());
					pricingRgs.setPriceList(data.PriceList_Rgs);
					pricingRgs.setPriceStd(data.PriceList_Rgs);
					pricingRgs.setPriceLimit(data.PriceLimit_Rgs);
					pricingRgs.saveEx();		
			
				}else if(!IsNew){
					
					int M_ProductPriceBuy = getPricing_ID(AD_Client_ID,AD_Org_ID, data.M_PriceList_Version_Buy_ID, data.M_Product_ID);
					int M_ProductPriceRtl = getPricing_ID(AD_Client_ID,AD_Org_ID, data.M_PriceList_Version_Rtl_ID, data.M_Product_ID);
					int M_ProductPriceRgs = getPricing_ID(AD_Client_ID,AD_Org_ID, data.M_PriceList_Version_Rgs_ID, data.M_Product_ID);
	
					X_M_ProductPrice pricingBuyEdit = null;
					X_M_ProductPrice pricingRtlEdit = null;
					X_M_ProductPrice pricingRgsEdit = null;
				
					if(M_ProductPriceBuy > 0){									
						pricingBuyEdit = new X_M_ProductPrice(getCtx(), M_ProductPriceBuy, get_TrxName());
						
						
						HashMap<String, BigDecimal> IsChanged = IsChagedPrice(pricingBuyEdit.getPriceList(), pricingBuyEdit.getPriceStd(), pricingBuyEdit.getPriceLimit(),
								data.PriceList_Buy, data.PriceList_Buy, data.PriceList_Buy);
						
						if(IsChanged.size() > 0){
							
							for(String key:IsChanged.keySet()){
								
								if(key.equals("PRICELIST")){
									pricingBuyEdit.setPriceList(IsChanged.get(key));
								}else if(key.equals("PRICESTD")){
									pricingBuyEdit.setPriceStd(IsChanged.get(key));
								}else if(key.equals("PRICELIMIT")){
									pricingBuyEdit.setPriceLimit(IsChanged.get(key));
								}
								
							}
						
							pricingBuyEdit.saveEx();
						}
					}
					
					if(M_ProductPriceRtl > 0){
						
						pricingRtlEdit = new X_M_ProductPrice(getCtx(), M_ProductPriceRtl, get_TrxName());
	
						HashMap<String, BigDecimal> IsChanged = IsChagedPrice(pricingRtlEdit.getPriceList(), pricingRtlEdit.getPriceStd(), pricingRtlEdit.getPriceLimit(),
								data.PriceList_Rtl, data.PriceList_Rtl, data.PriceLimit_Rtl);
						
						if(IsChanged.size()>0){
							
							for(String key:IsChanged.keySet()){
								
								if(key.equals("PRICELIST")){
									pricingRtlEdit.setPriceList(IsChanged.get(key));
								}else if(key.equals("PRICESTD")){
									pricingRtlEdit.setPriceStd(IsChanged.get(key));
								}else if(key.equals("PRICELIMIT")){
									pricingRtlEdit.setPriceLimit(IsChanged.get(key));
								}
								
							}
												
							pricingRtlEdit.saveEx();
						}
					}
					
					if(M_ProductPriceRgs > 0){
						
						pricingRgsEdit = new X_M_ProductPrice(getCtx(), M_ProductPriceRgs, get_TrxName());
						
						HashMap<String, BigDecimal> IsChanged = IsChagedPrice(pricingRgsEdit.getPriceList(), pricingRgsEdit.getPriceStd(), pricingRgsEdit.getPriceLimit(),
								data.PriceList_Rgs, data.PriceList_Rgs, data.PriceLimit_Rgs);
						
						if(IsChanged.size() > 0){
							
							for(String key:IsChanged.keySet()){
								
								if(key.equals("PRICELIST")){
									pricingRgsEdit.setPriceList(IsChanged.get(key));
								}else if(key.equals("PRICESTD")){
									pricingRgsEdit.setPriceStd(IsChanged.get(key));
								}else if(key.equals("PRICELIMIT")){
									pricingRgsEdit.setPriceLimit(IsChanged.get(key));
								}
								
							}
							
							pricingRgsEdit.saveEx();
						}
					}
				}
						
				if(product != null &  data.M_Product_ID == 0){
					rs = "Penambahan Produk Berhasil";	
				}else if(product != null &  data.M_Product_ID > 0){
					rs = "Edit Produk Berhasil";	
				}
			}else{		
				rs = "Produk Yang Anda Tambahkan Sudah Terdaftar, Silahkan Sync Ulang Data Produk";
			}
		} catch (Exception e) {
			rs = Error;
		}
		
		return rs;
		
	}
	
	
	private int getPricing_ID (int AD_Client_ID, int AD_Org_ID, int M_PriceList_Version_ID, int M_Product_ID){
		
		Integer rs = 0 ;
		
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT M_ProductPrice_ID");
		SQL.append(" FROM M_ProductPrice");
		SQL.append(" WHERE AD_Client_ID = " + AD_Client_ID);
		SQL.append(" AND AD_Org_ID = " + AD_Org_ID);
		SQL.append(" AND M_PriceList_Version_ID = " + M_PriceList_Version_ID);					
		SQL.append(" AND M_Product_ID = " + M_Product_ID);
		
		rs = DB.getSQLValueEx(get_TrxName(), SQL.toString());
		
		return rs;
	}
	
	private HashMap<String, BigDecimal> IsChagedPrice (BigDecimal OldPriceList, BigDecimal OldPriceStd,BigDecimal OldPriceLimit,BigDecimal newPriceList,BigDecimal newPriceStd,BigDecimal newPriceLimit ){
		
		HashMap<String, BigDecimal> Changed = new HashMap<String, BigDecimal>();
		
		if(OldPriceList.compareTo(newPriceList) != 0){		
			Changed.put("PRICELIST", newPriceList);
		}
		
		if(OldPriceStd.compareTo(newPriceStd) != 0){	
			Changed.put("PRICESTD", newPriceStd);
		}
		
		if(OldPriceLimit.compareTo(newPriceLimit) != 0){
			Changed.put("PRICELIMIT", newPriceLimit);
		}		

		return Changed;
		
	}
	
	
	private String CreatePOSSetup(int AD_Client_ID,int AD_Org_ID,SMR_Model_POSSetup data){
		
		String rs = "";
	
		try{
		
			X_C_Pos_Setup setup = new X_C_Pos_Setup(getCtx(), data.C_Pos_Setup_ID, get_TrxName());
	
			
			if(data.IsActive.toUpperCase().equals("Y")) {
				setup.setIsActive(true);
			}else if(data.IsActive.toUpperCase().equals("N")) {
				setup.setIsActive(false);
			}		
			setup.setName(data.Name);
			setup.setAddress(data.Address);
			setup.setstore_name(data.store_name);
			setup.setfooter(data.Footer);
			setup.set_ValueNoCheck("Phone", data.Phone);
			setup.setM_PriceList_ID(data.M_PriceList_ID);
			setup.setM_Sales_Locator_ID(data.M_Sales_Locator_ID);
			setup.setM_Locator_ID(data.M_Locator_ID);
			setup.setC_BankAccount_ID(data.C_BankAccount_ID);
			setup.saveEx();
			
			if(setup != null && data.C_Pos_Setup_ID == 0) {
				rs = "Penambahan Setup Pos Berhasil";
				
			}else if(setup != null && data.C_Pos_Setup_ID > 0){
				rs = "Perubahan Setup Pos Berhasil";

			}
			
			
		}catch (Exception e) {
			rs = Error;
		}	
		
		return rs;
		
	}
	
	
	private String ChangePassword(int AD_Client_ID,int AD_Org_ID,SMR_Model_User data){
		
		String rs = "";
	
		try{

			MUser user = new MUser(getCtx(), data.AD_User_ID, get_TrxName());
			
			if(user != null){
				user.setPassword(data.Password);
				user.saveEx();	
			}
			
			
			if(user != null){
				
				
				
				boolean OK = sendIndividualMail(user.getAD_User_ID(), data.Password);
				
				if(OK) {
					rs = "Perubahan Password Berhasil";
				}else {
					rs = Error;
				}
			}
			
		}catch (Exception e) {
			rs = Error;
		}	
		
		return rs;
		
	}
	
	private Boolean sendIndividualMail (int AD_User_ID, String newPass) throws Exception{
		
		
	 	int	m_R_MailText_ID = 1000001;
	 	MMailText m_MailText = null;
	 	MClient m_client = null;
	 	MUser m_from = null;
	
		
		m_MailText = new MMailText (getCtx(), m_R_MailText_ID, get_TrxName());
		if (m_MailText.getR_MailText_ID() == 0)
			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
		
		//	Client Info
		m_client = MClient.get (getCtx());
		if (m_client.getAD_Client_ID() == 0)
			throw new Exception ("Not found @AD_Client_ID@");
		
		// Check SMTP Host
		if (m_client.getSMTPHost() == null || m_client.getSMTPHost().length() == 0)
			throw new Exception ("No SMTP Host found");
		
	
		MUser to = new MUser (getCtx(), AD_User_ID, null);
		m_MailText.setUser(AD_User_ID);		//	parse context
		StringBuilder message = new StringBuilder(m_MailText.getMailText(true));
		
		//m_from = new MUser(getCtx(), getAD_User_ID(), get_TrxName());
		

		message.append(" "+to.getName());
		message.append("\n");
		message.append("\n");
		message.append("Perubahan Password Anda Berhasil Diupdate");
		message.append("\n");
		message.append("\n");
		message.append("Password Baru Anda :"+ newPass);		
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("\n");
		message.append("Salam Hangat");
		message.append("\n");
		message.append("\n");
		message.append("Team Semeru");
		
		EMail email = m_client.createEMail(m_from, to, m_MailText.getMailHeader(), message.toString());
		
		if (m_MailText.isHtml())
			email.setMessageHTML(m_MailText.getMailHeader(), message.toString());
		else
		{
			email.setSubject (m_MailText.getMailHeader());
			email.setMessageText (message.toString());
		}
		if (!email.isValid() && !email.isValid(true))
		{
			log.warning("NOT VALID - " + email);
			to.setIsActive(false);
			to.addDescription("Invalid EMail");
			to.saveEx();
			return Boolean.FALSE;
		}
		boolean OK = EMail.SENT_OK.equals(email.send());
		MUserMail uMail = new MUserMail(m_MailText, AD_User_ID, email);
		uMail.saveEx();
		//
		if (OK) {
			//if (log.isLoggable(Level.FINE)) log.fine(to.getEMail());
			uMail.setIsDelivered("Y");
			uMail.saveEx();

		} else {
			log.warning("FAILURE - " + to.getEMail());
		}
		StringBuilder msglog = new StringBuilder((OK ? "@OK@" : "@ERROR@")).append(" - ").append(to.getEMail());
		addLog(0, null, null, msglog.toString());
		return new Boolean(OK);
	}	//	sendIndividualMail

}
