package org.semeru.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MBank;
import org.compiere.model.MBankAccount;
import org.compiere.model.MElementValue;
import org.compiere.model.MLocation;
import org.compiere.model.MLocator;
import org.compiere.model.MOrg;
import org.compiere.model.MOrgInfo;
import org.compiere.model.MUser;
import org.compiere.model.MUserRoles;
import org.compiere.model.MWarehouse;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CacheMgt;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.semeru.model.X_mob_mst_package;
import org.semeru.model.X_mob_trx_contract;
import org.semeru.model.X_mob_trx_hist_payment;
import org.semeru.model.X_mob_trx_registration;

/**
 * 
 * @author Tegar N
 *
 */
public class SMR_ProcessInitialOrg extends SvrProcess{
	
	private final String Error = "ERROR";
	
	private int p_mob_trx_registration_id;
	private int p_ad_client_id;
	private int p_ad_org_id;
//	private String p_mob_trx_registration_no;
	private String p_org_value;
	private String p_org_name;
	private String p_user_name;
	private String p_user_email;
	private String p_user_pass;
	private String p_user_phone;
	private String p_user_phone2;
	private Timestamp p_user_birthday;
	private int p_package_id;
//	private Timestamp p_reg_date;
	private Timestamp p_reg_paid_date;
	private Timestamp p_reg_date_process;	
	private BigDecimal p_reg_amt_paid;
	private int p_contract_no;
	private String p_user_address;
	private int p_user_city_ID;
	private int p_user_country_id;
	private int p_reg_mth;
	private String p_latitude;
	private String p_longitude;

	
	private boolean p_is_extend = false;
	private boolean p_is_mutation = false;
	private boolean p_is_group = false;
	private boolean p_isactive = false;
	MOrg InitialOrg = null;
	MUser user = null;
	
	boolean isNewCust = false;
	boolean isPerpanjangan = false;
	boolean isUpgrade = false;
	boolean isGroup = false;
	
//	private MMailText	m_MailText = null;
//	private MClient m_client = null;
//	private int	m_R_MailText_ID = 1000000;
//	private MUser m_from = null;
	
	
	@Override
	protected void prepare() {

		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{

		}
		
				
	}

	@Override
	protected String doIt() throws Exception {

		
		StringBuilder SQLGetRegisData = new StringBuilder();
		SQLGetRegisData.append(" SELECT mob_trx_registration_id,");
		SQLGetRegisData.append(" ad_client_id,");
		SQLGetRegisData.append(" ad_org_id,");
		SQLGetRegisData.append(" mob_trx_registration_no,");
		SQLGetRegisData.append(" org_value,");
		SQLGetRegisData.append(" org_name,");
		SQLGetRegisData.append(" is_extend,");
		SQLGetRegisData.append(" is_mutation,");
		SQLGetRegisData.append(" is_group,");
		SQLGetRegisData.append(" isactive,");
		SQLGetRegisData.append(" user_name,");
		SQLGetRegisData.append(" user_email,");
		SQLGetRegisData.append(" user_pass,");
		SQLGetRegisData.append(" user_phone,");
		SQLGetRegisData.append(" user_phone2,");
		SQLGetRegisData.append(" user_birthday,");
		SQLGetRegisData.append(" package_id,");
		SQLGetRegisData.append(" reg_date,");
		SQLGetRegisData.append(" reg_paid_date,");
		SQLGetRegisData.append(" reg_date_process,");
		SQLGetRegisData.append(" contract_no,");
		SQLGetRegisData.append(" user_address,");
		SQLGetRegisData.append(" user_city_id,");
		SQLGetRegisData.append(" user_country_id,");
		SQLGetRegisData.append(" reg_mth, ");
		SQLGetRegisData.append(" latitude, ");
		SQLGetRegisData.append(" longitude ");
		SQLGetRegisData.append(" FROM  mob_trx_registration ");
		SQLGetRegisData.append(" WHERE IsCreated = 'N' ");


		String rslt = "";
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetRegisData.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					p_mob_trx_registration_id = rs.getInt(1);
					p_ad_client_id = rs.getInt(2);
					p_ad_org_id = rs.getInt(3);
//					p_mob_trx_registration_no = rs.getString(4);
					p_org_value = rs.getString(5);
					p_org_name = rs.getString(6);
					if(rs.getString(7).equals("Y")){
						p_is_extend = true;
					}
					if(rs.getString(8).equals("Y")){
						p_is_mutation = true;
					}
					if(rs.getString(9).equals("Y")){
						p_is_group = true;
					}
					if(rs.getString(10).equals("Y")){
						p_isactive = true;
					}
					
					p_user_name = rs.getString(11);
					p_user_email = rs.getString(12);
					p_user_pass = rs.getString(13);
					p_user_phone = rs.getString(14);	
					p_user_phone2 = rs.getString(15);
					p_user_birthday = rs.getTimestamp(16);
					p_package_id = rs.getInt(17);
//					p_reg_date = rs.getTimestamp(18);
					p_reg_paid_date = rs.getTimestamp(19);
					p_reg_date_process = rs.getTimestamp(20);
					p_contract_no = rs.getInt(21);	
					p_user_address =rs.getString(22);
					p_user_city_ID =rs.getInt(23);
					p_user_country_id =rs.getInt(24);
					p_reg_mth = rs.getInt(25);
					p_latitude = rs.getString(26);
					p_longitude = rs.getString(27);
				
					
					if(!p_is_extend && !p_is_mutation && !p_is_group &&p_reg_date_process == null && p_contract_no == 0 && p_isactive){
						
						isNewCust = true;
						
					}else if(p_is_extend && !p_is_mutation && !p_is_group && p_isactive){
						
						isPerpanjangan = true;
						
					}else if(p_is_mutation && !p_is_group && p_isactive &&  p_reg_date_process == null && p_contract_no == 0){
						
						isUpgrade = true;
						
					}else if(!p_is_extend && !p_is_mutation && p_is_group && p_isactive && p_reg_date_process == null && p_contract_no == 0 ){
						
						isGroup = true;
						
					}
					
					
					
					rslt = generateInitialData(p_ad_client_id, p_ad_org_id, p_org_value, p_org_name, "", p_user_phone, p_user_phone2, p_user_email, p_user_address, p_user_city_ID, p_user_country_id,
							p_user_pass, isNewCust, isPerpanjangan, isUpgrade,isGroup);
					
					
					if(rslt.equals(Error)){
						rollback();
						
						X_mob_trx_registration registrationUpdate = new X_mob_trx_registration(getCtx(), p_mob_trx_registration_id, get_TrxName());
						registrationUpdate.setIsCreated("N");
						registrationUpdate.saveEx();
						
					}else{
					    	
						X_mob_trx_registration registrationUpdate = new X_mob_trx_registration(getCtx(), p_mob_trx_registration_id, get_TrxName());
						registrationUpdate.setIsCreated("Y");
						registrationUpdate.saveEx();   			 					    
					 
					 }   
					
					
				}
				
				

			} catch (SQLException err) {
				
				log.log(Level.SEVERE, SQLGetRegisData.toString(), err);
				rslt = Error;
				
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
						
		
			
		return rslt;
	}	
	
private String generateInitialData(int AD_Client_ID,int AD_Org_ID, String value, String name, String description, String phone, String phone2,
		String email, String address, int C_City_ID, int C_Country_ID, String password, Boolean IsNewCust, Boolean IsPerpanjangan, Boolean IsUpgrade,Boolean IsGroup){
		
		String rslt = "";
		
		try {
			
			
			if(IsNewCust){
			
				MWarehouse whPilihLokasi= null;
				MWarehouse wh= null;
				

				
				//Create Organization
				InitialOrg = new MOrg(getCtx(), 0, get_TrxName());
				InitialOrg.setName(name);
				InitialOrg.setValue(value);
				InitialOrg.setDescription("");
				InitialOrg.setIsActive(true);
				InitialOrg.saveEx();
				
			
					
				Random rand  = new Random();
				Integer rand_pass  = rand.nextInt(100000);
				password = String.valueOf(rand_pass);
				
				//Create User
				user = new MUser(getCtx(), 0, get_TrxName());
				user.setValue(value);
				user.setName(email);
				user.setEMail(email);
				user.setBirthday(p_user_birthday);
				//user.setPassword(password);
				user.setAD_Org_ID(InitialOrg.getAD_Org_ID());
				user.setIsActive(true);
				user.saveEx();
				
				
				if(InitialOrg != null){
					
					//cache reset
					Env.reset(false);	
					CacheMgt.get().reset();
					
				}
				
				//Create User Location
				MLocation userLoc = new MLocation(getCtx(), 0, get_TrxName());
				userLoc.setAD_Org_ID(0);
				userLoc.setAddress1(address);
				userLoc.setC_City_ID(C_City_ID);
				userLoc.setC_Country_ID(C_Country_ID);
				userLoc.setIsActive(true);
				userLoc.saveEx();
						
				//orgLoc
								
				MOrgInfo orgInfo = InitialOrg.getInfo();
				orgInfo.setC_Location_ID(userLoc.getC_Location_ID());
				orgInfo.setPhone(p_user_phone);;
				orgInfo.saveEx();

				
				//Create Bank Acct
				
				MBank bank = new MBank(getCtx(), 0, get_TrxName());
				MBankAccount bankAcct = new MBankAccount(getCtx(), 0, get_TrxName());
				
				bank.setIsActive(true);
				
				//bank.setClientOrg(AD_Client_ID, InitialOrg.getAD_Org_ID());
				bank.setAD_Org_ID(InitialOrg.getAD_Org_ID());
				bank.setName("Bank "+ p_org_name);
				bank.setDescription("Deafault Bank "+ p_org_name);
				bank.setIsOwnBank(true);
				bank.setRoutingNo("1");
				bank.saveEx();
				
				if(bank != null){
					
					//bankAcct.setClientOrg(AD_Client_ID, InitialOrg.getAD_Org_ID());
					bankAcct.setAD_Org_ID(InitialOrg.getAD_Org_ID());
					bankAcct.setIsActive(true);
					bankAcct.setC_Bank_ID(bank.getC_Bank_ID());
					bankAcct.setValue(bank.getName());
					bankAcct.setName(bank.getName());
					bankAcct.setAccountNo("123");
					bankAcct.setBankAccountType("B");
					bankAcct.setC_Currency_ID(303);
					bankAcct.setCurrentBalance(Env.ZERO);
					bankAcct.saveEx();	
					
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
					ElementValue.setName(bank.getName());
					ElementValue.setAccountType(MElementValue.ACCOUNTTYPE_Asset);
					ElementValue.setAccountSign(MElementValue.ACCOUNTSIGN_Natural);
					ElementValue.setPostActual(true);
					ElementValue.setPostBudget(true);
					ElementValue.setPostStatistical(true);
					ElementValue.setC_Element_ID(Element_ID);
					ElementValue.saveEx();			
					
					MAccount newAccount = new MAccount (getCtx(), 0, get_TrxName());
					//newAccount.setClientOrg(AD_Client_ID, 0);
					newAccount.setAD_Org_ID(0);
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
				
				
								
				//Create Bussiness Partner
				MBPartner bp = new MBPartner(getCtx(), 0, get_TrxName());
				bp.setAD_Org_ID( InitialOrg.getAD_Org_ID());
				bp.setValue(value);
				bp.setName(name);
				bp.setName2(name);
				bp.setSO_CreditLimit(Env.ZERO);
				bp.set_CustomColumn("IbuKandung", "-");
				bp.set_CustomColumn("NoKTP", "-");
				bp.setIsCustomer(true);
				bp.setIsEmployee(true);
				bp.set_ValueNoCheck("IsBPCash", "Y");
				bp.setIsSalesRep(true);
				bp.saveEx();
				
				//Create BP Location
				MBPartnerLocation BPLoc = new MBPartnerLocation(getCtx(), 0, get_TrxName());
				BPLoc.setAD_Org_ID( InitialOrg.getAD_Org_ID());
				BPLoc.setC_BPartner_ID(bp.getC_BPartner_ID());
				BPLoc.setName(userLoc.getAddress1());
				BPLoc.setC_Location_ID(userLoc.getC_Location_ID());
				BPLoc.setPhone(phone);
				BPLoc.setPhone2(phone2);
				BPLoc.setFax("-");
				BPLoc.setIsShipTo(true);
				BPLoc.setIsBillTo(true);
				BPLoc.setIsPayFrom(true);
				BPLoc.setIsRemitTo(true);
				BPLoc.set_ValueNoCheck("latitude", p_latitude);
				BPLoc.set_ValueNoCheck("longitude", p_longitude);
				//BPLoc.set_CustomColumn("IsTax", true);
				BPLoc.saveEx();
				
				user.setC_BPartner_ID(bp.getC_BPartner_ID());
				user.setC_BPartner_Location_ID(BPLoc.getC_BPartner_Location_ID());
				user.saveEx();
				
				MUserRoles userRole = new MUserRoles(getCtx(), user.getAD_User_ID(), 1000008, get_TrxName());
				userRole.saveEx();
				
				if(InitialOrg != null){
					
					//Create Warehouse Pilih Lokasi 
					whPilihLokasi = new MWarehouse(getCtx(), 0, get_TrxName());
					whPilihLokasi.setAD_Org_ID(InitialOrg.getAD_Org_ID());
					whPilihLokasi.setValue("-- Pilih Lokasi "+name +" --");
					whPilihLokasi.setName("-- Pilih Lokasi "+name +" --");
					whPilihLokasi.setDescription("");
					whPilihLokasi.setIsActive(false);
					whPilihLokasi.setC_Location_ID(userLoc.getC_Location_ID());
					whPilihLokasi.setSeparator("*");
					whPilihLokasi.setIsDisallowNegativeInv(true);
					whPilihLokasi.saveEx();
					
					if(whPilihLokasi != null){
						
						MLocator pilihLoc = new MLocator(getCtx(), 0, get_TrxName());
						pilihLoc.setValue("-- Pilih Lokasi --");
						pilihLoc.setAD_Org_ID(InitialOrg.getAD_Org_ID());
						pilihLoc.setM_Warehouse_ID(whPilihLokasi.getM_Warehouse_ID());
						pilihLoc.setPriorityNo(10);
						pilihLoc.setX(String.valueOf(1));
						pilihLoc.setY(String.valueOf(2));
						pilihLoc.setZ(String.valueOf(3));
						pilihLoc.setIsActive(true);
						pilihLoc.saveEx();					
						
					}
					
					//Create Warehouse
					wh = new MWarehouse(getCtx(), 0, get_TrxName());
					wh.setAD_Org_ID(InitialOrg.getAD_Org_ID());
					wh.setValue(value);
					wh.setName(name);
					wh.setDescription("");
					wh.setIsActive(true);
					wh.setC_Location_ID(userLoc.getC_Location_ID());
					wh.setSeparator("*");
					wh.setIsDisallowNegativeInv(true);
					wh.saveEx();
							
					if(wh != null){
						
						MLocator locDefault = new MLocator(getCtx(), 0, get_TrxName());
						
						locDefault.setValue("Gudang "+ name);
						locDefault.setAD_Org_ID(InitialOrg.getAD_Org_ID());
						locDefault.setM_Warehouse_ID(wh.getM_Warehouse_ID());
						locDefault.setPriorityNo(20);
						locDefault.setX(String.valueOf(10));
						locDefault.setY(String.valueOf(20));
						locDefault.setZ(String.valueOf(30));
						locDefault.setIsActive(true);
						locDefault.setIsDefault(true);
						locDefault.saveEx();		
					}
					
				}
				
			}
			
			String rsContract = Contract(IsNewCust, IsPerpanjangan, IsUpgrade, IsGroup,p_package_id);
			//boolean  ok = sendIndividualMail(user.getAD_User_ID(),password);

			
			if(rsContract.equals(Error)){
				return Error;
			}
			
		} catch (Exception e) {
			rslt = Error;
		}
		
	
		return rslt;
		
	}


private String Contract (boolean IsNewCust, Boolean IsPerpanjangan,Boolean IsUpgrade, Boolean IsGroup, int package_id){
	
	String rslt = "";
	
	try {
		
		
		StringBuilder SQLGetOrgID = new StringBuilder(); 
		SQLGetOrgID.append("SELECT AD_Org_ID ");
		SQLGetOrgID.append(" FROM AD_Org");
		SQLGetOrgID.append(" WHERE AD_Client_ID = " + p_ad_client_id);
		SQLGetOrgID.append(" AND value ='"+p_org_value+"'");

		int AD_OrgOld_ID = DB.getSQLValueEx(get_TrxName(), SQLGetOrgID.toString());
		
		if(IsGroup){
					
			user = new MUser(getCtx(), 0, get_TrxName());
			user.setValue(p_user_name);
			user.setName(p_user_email);
			user.setEMail(p_user_email);
			user.setBirthday(p_user_birthday);
			user.setPassword(p_user_pass);
			user.setAD_Org_ID(AD_OrgOld_ID);
			user.setIsActive(true);
			user.saveEx();
			
			//Create User Location
			MLocation userLoc = new MLocation(getCtx(), 0, get_TrxName());
			userLoc.setAD_Org_ID(0);
			userLoc.setAddress1(p_user_address);
			userLoc.setC_City_ID(p_user_city_ID);
			userLoc.setC_Country_ID(p_user_country_id);
			userLoc.saveEx();
			
			//Create Bussiness Partner
			MBPartner bp = new MBPartner(getCtx(), 0, get_TrxName());
			bp.setAD_Org_ID( AD_OrgOld_ID);
			bp.setValue(user.getValue());
			bp.setName(user.getName());
			bp.setName2(user.getName());
			bp.setSO_CreditLimit(Env.ZERO);
			bp.set_CustomColumn("IbuKandung", "-");
			bp.set_CustomColumn("NoKTP", "-");
			bp.setIsCustomer(true);
			bp.setIsEmployee(true);
			bp.set_ValueNoCheck("IsBPCash", "Y");
			bp.setIsSalesRep(true);
			bp.saveEx();
			
			//Create BP Location
			MBPartnerLocation BPLoc = new MBPartnerLocation(getCtx(), 0, get_TrxName());
			BPLoc.setAD_Org_ID(AD_OrgOld_ID);
			BPLoc.setC_BPartner_ID(bp.getC_BPartner_ID());
			BPLoc.setName(userLoc.getAddress1());
			BPLoc.setC_Location_ID(userLoc.getC_Location_ID());
			BPLoc.setPhone(p_user_phone);
			BPLoc.setPhone2(p_user_phone2);
			BPLoc.setFax("-");
			BPLoc.setIsShipTo(true);
			BPLoc.setIsBillTo(true);
			BPLoc.setIsPayFrom(true);
			BPLoc.setIsRemitTo(true);
			BPLoc.set_ValueNoCheck("latitude", p_latitude);
			BPLoc.set_ValueNoCheck("longitude", p_longitude);
			//BPLoc.set_CustomColumn("IsTax", true);
			BPLoc.saveEx();
			
			user.setC_BPartner_ID(bp.getC_BPartner_ID());
			user.setC_BPartner_Location_ID(BPLoc.getC_BPartner_Location_ID());
			user.saveEx();
			
			MUserRoles userRole = new MUserRoles(getCtx(), user.getAD_User_ID(), 1000008, get_TrxName());
			userRole.saveEx();
			
			Env.reset(false);	
			CacheMgt.get().reset();
			
		}
		
		
		StringBuilder SQLGetContract = new StringBuilder();
		SQLGetContract.append("SELECT mob_trx_contract_id ");
		SQLGetContract.append(" FROM mob_trx_contract ");
		SQLGetContract.append(" WHERE user_id = (");
		SQLGetContract.append(" SELECT AD_User_ID ");
		SQLGetContract.append(" FROM AD_User ");
		SQLGetContract.append(" WHERE name = '"+p_user_email+"'");
		SQLGetContract.append(" AND AD_Client_ID ="+p_ad_client_id+")");
		SQLGetContract.append(" AND AD_Client_ID ="+p_ad_client_id);
		SQLGetContract.append(" AND AD_Org_ID = (SELECT AD_Org_ID ");
		SQLGetContract.append(" FROM AD_Org ");
		SQLGetContract.append(" WHERE value = '"+p_org_value+"') ");


		
		Integer mob_trx_contract_id = DB.getSQLValueEx(get_TrxName(), SQLGetContract.toString());

		X_mob_trx_contract contract = null;
		
		if(mob_trx_contract_id < 0) {
			mob_trx_contract_id = 0;
		}
		
	
		//Generate Contract
		if(isNewCust ||IsGroup ){
			
			contract = new X_mob_trx_contract(getCtx(), 0 , get_TrxName());
			
			Random rand = new Random();
			if(isNewCust) {
				contract.setAD_Org_ID(InitialOrg.getAD_Org_ID());
			}else if(isGroup) {
				contract.setAD_Org_ID(AD_OrgOld_ID);
			}
			contract.setcontract_no(rand.nextInt(100000));
			contract.setuser_id(user.getAD_User_ID());
			
		}else {
			
			contract = new X_mob_trx_contract(getCtx(), mob_trx_contract_id, get_TrxName());
			
		}
		
		

		Timestamp next_due_date = null;
		Date start_date = new Date(); 
		Date finalDate = new Date();
		
		Calendar c = Calendar.getInstance();
		if(IsNewCust || IsGroup) {
			start_date = new Timestamp(System.currentTimeMillis());
		}else {
			start_date = contract.getnext_due_date();
		}
		
		c.setTime(start_date); 
		
		X_mob_mst_package pkg = new X_mob_mst_package(getCtx(), package_id, get_TrxName());
		if(pkg.getname_package().toUpperCase().equals("TRIAL")) {
			
			p_reg_mth = 1;
		}

		int contractDuration = p_reg_mth;
		
		
		
		
		if(p_reg_mth >0) {
			c.add(Calendar.MONTH, contractDuration);
			
			if(c.get(Calendar.DAY_OF_MONTH) >= 28) {
				
				c.set(Calendar.DAY_OF_MONTH, 1);
				c.add(Calendar.MONTH, 1);
			}
			
		}
		
		finalDate = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String tglTostr = df.format(finalDate);

		next_due_date = Timestamp.valueOf(tglTostr);
		
		if(IsGroup){
			
			contract.setis_group(true);
		}
		
		if(IsNewCust || IsPerpanjangan || IsGroup){
			
			contract.setnext_due_date(next_due_date);
			contract.setamt_ots(p_reg_amt_paid);
			contract.setlast_paid_date(p_reg_paid_date);
			contract.setamt_last_paid(p_reg_amt_paid);
			
		
		}
		
		if(!isPerpanjangan && !p_is_mutation) {
			contract.setuser_id(user.getAD_User_ID());
		}
		
		if(IsNewCust || IsUpgrade || IsGroup){
			contract.setpackage_id(p_package_id);
			contract.setlast_package_id(p_package_id);
			contract.setlast_mutation_date(p_reg_paid_date);
		}
		contract.saveEx();
		
		
		X_mob_trx_hist_payment histPayment = new X_mob_trx_hist_payment(getCtx(), 0, get_TrxName());
		histPayment.setAD_Org_ID(p_ad_org_id);
		histPayment.setcontract_no(contract.getcontract_no());
		histPayment.setpaid_date(p_reg_paid_date);
		histPayment.setamt_paid(p_reg_amt_paid);
		histPayment.setis_extend(IsPerpanjangan);
		histPayment.setis_mutation(IsUpgrade);
		histPayment.set_ValueNoCheck("reg_mth", p_reg_mth);
		histPayment.saveEx();
		
		
		X_mob_trx_registration regisUpdate = new X_mob_trx_registration(getCtx(), p_mob_trx_registration_id, get_TrxName());
		regisUpdate.setreg_date_process(p_reg_paid_date);
		if(IsNewCust || IsGroup){
			regisUpdate.setcontract_no(contract.getcontract_no());
		}
		regisUpdate.setIsCreated("Y");
		regisUpdate.saveEx();
		
	} catch (Exception e) {

		rslt = Error;
	}
	
	return rslt;
}

//private Boolean sendIndividualMail (int AD_User_ID, String pass) throws Exception{
//	
//		
//		m_MailText = new MMailText (getCtx(), m_R_MailText_ID, get_TrxName());
//		if (m_MailText.getR_MailText_ID() == 0)
//			throw new Exception ("Not found @R_MailText_ID@=" + m_R_MailText_ID);
//		
//		//	Client Info
//		m_client = MClient.get (getCtx());
//		if (m_client.getAD_Client_ID() == 0)
//			throw new Exception ("Not found @AD_Client_ID@");
//		
//		// Check SMTP Host
//		if (m_client.getSMTPHost() == null || m_client.getSMTPHost().length() == 0)
//			throw new Exception ("No SMTP Host found");
//		
//	
//		MUser to = new MUser (getCtx(), AD_User_ID, null);
//		m_MailText.setUser(AD_User_ID);		//	parse context
//		StringBuilder message = new StringBuilder(m_MailText.getMailText(true));
//		
//		//m_from = new MUser(getCtx(), getAD_User_ID(), get_TrxName());
//		
//		message.append(" "+to.getName());
//		message.append("\n");
//		message.append("Registrasi User Anda Telah Berhasil Dengan Data Sebagai Berikut : ");
//		message.append("Username : "+to.getEMail());
//		message.append("\n");
//		message.append("Password : "+pass);
//		message.append("\n");
//		message.append("Data Tersebut Bersifat Rahasia, Mohon Tidak Membagikannya Kepada Pihak Lain.");
//		message.append("Terima Kasih.");
//		message.append("\n");
//		message.append("\n");
//		message.append("\n");
//		message.append("\n");
//		message.append("Salam Hangat");
//		message.append("\n");
//		message.append("\n");
//		message.append("Team SemeruLite");
//		
//		EMail email = m_client.createEMail(m_from, to, m_MailText.getMailHeader(), message.toString());
//		
//		if (m_MailText.isHtml())
//			email.setMessageHTML(m_MailText.getMailHeader(), message.toString());
//			
//		else
//		{
//			email.setSubject (m_MailText.getMailHeader());
//			email.setMessageText (message.toString());
//		}
//		if (!email.isValid() && !email.isValid(true))
//		{
//			log.warning("NOT VALID - " + email);
//			to.setIsActive(false);
//			to.addDescription("Invalid EMail");
//			to.saveEx();
//			return Boolean.FALSE;
//		}
//		boolean OK = EMail.SENT_OK.equals(email.send());
//		new MUserMail(m_MailText, AD_User_ID, email).saveEx();
//		//
//		if (OK) {
//			if (log.isLoggable(Level.FINE)) log.fine(to.getEMail());
//		} else {
//			log.warning("FAILURE - " + to.getEMail());
//		}
//		StringBuilder msglog = new StringBuilder((OK ? "@OK@" : "@ERROR@")).append(" - ").append(to.getEMail());
//		addLog(0, null, null, msglog.toString());
//		return new Boolean(OK);
//	}	//	sendIndividualMail
	
}
