package org.semeru.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MMailText;
import org.compiere.model.MUser;
import org.compiere.model.MUserMail;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.EMail;
import org.semeru.model.X_mob_trx_registration;

public class SMR_ProcessEmailActiveUser extends SvrProcess{

	private int				m_R_MailText_ID = 1000000;
	private MMailText		m_MailText = null;
	private MClient			m_client = null;

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String doIt() throws Exception {
		
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
		
		
		StringBuilder SQLGetUser = new StringBuilder();
		SQLGetUser.append("SELECT ad.AD_User_ID, mob.mob_trx_registration_id,mob.email_sales,cus.passtring");
		SQLGetUser.append(" FROM AD_User ad");
		SQLGetUser.append(" LEFT JOIN mob_trx_registration mob ON mob.user_email = ad.name");
		SQLGetUser.append(" LEFT JOIN semerulite.customer cus ON cus.email = mob.user_email");
		SQLGetUser.append(" WHERE mob.IsEmailed = 'N'");

		
		
		PreparedStatement pstmt = null;
     	ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(SQLGetUser.toString(), null);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					
					X_mob_trx_registration regis = new X_mob_trx_registration(getCtx(), rs.getInt(2), get_TrxName());
					String Type = "";
					
					if(regis.is_extend()) {
						Type = "E";
					}else if(regis.is_group()) {
						Type = "G";
					}else if(regis.is_mutation()){
						Type = "M";
					}else if(!regis.is_extend() && !regis.is_group()&& !regis.is_mutation()) {
						Type = "N";
					}
					
					if(Type.equals("N") || Type.equals("G")) {
					
//					Random rand  = new Random();
//					Integer rand_pass  = rand.nextInt(100000);
					String password = rs.getString(4);
					
					MUser user = new MUser(getCtx(), rs.getInt(1), get_TrxName());
					user.setPassword(password);
					user.saveEx();
					
					sendIndividualMail(rs.getInt(1), password,rs.getInt(2),Type,rs.getString(3));
					
					}else {
						sendIndividualMail(rs.getInt(1), "",rs.getInt(2),Type,"");

					}
					
				    	   			 				  
				}

			} catch (SQLException err) {
				log.log(Level.SEVERE, SQLGetUser.toString(), err);
			} finally {
				DB.close(rs, pstmt);
				rs = null;
				pstmt = null;
			}
		
		
		
		//sendIndividualMail( );


			
		return null;
	}

private Boolean sendIndividualMail (int AD_User_ID, String newPass, int mob_trx_registration_id, String Type, String emailSales) throws Exception{
				
	 	MUser m_from = null;
//	 	Integer sales_ID = 0;
	 	ArrayList<Integer> ListUser = new ArrayList<>();
	 	ListUser.add(AD_User_ID);
	 	boolean OK  = false;
	 	
//	 	if(emailSales != null && !emailSales.isEmpty() && emailSales != "") {
//	 		StringBuilder sales = new StringBuilder();
//	 		sales.append("SELECT AD_User_ID");
//	 		sales.append(" FROM AD_User ");
//	 		sales.append(" WHERE AD_Client_ID = "+ Env.getAD_Client_ID(Env.getCtx()));
//	 		sales.append(" AND email = '"+emailSales+"'");
//	 	
//	 		sales_ID = DB.getSQLValueEx(get_TrxName(), sales.toString());
//	 		
//	 		if(sales_ID > 0) {
//	 			ListUser.add(sales_ID);
//	 		}
//	 		
//	 	}
	 	
	 	for(int i =0 ; i < ListUser.size() ; i++) {
	 		
	 	
	 	
			MUser to = new MUser (getCtx(), ListUser.get(i), null);
			m_MailText.setUser(ListUser.get(i));		//	parse context
			StringBuilder message = new StringBuilder(m_MailText.getMailText(true));
			
			//m_from = new MUser(getCtx(), getAD_User_ID(), get_TrxName());
			
	
			message.append(" "+to.getName());
			message.append("\n");
			if(Type.equals("N") || Type.equals("G")) {
				message.append("Registrasi User Anda Telah Berhasil Dengan Data Sebagai Berikut : ");
			}else if(Type.equals("M")) {
				message.append("Upgrade Paket Telah Berhasil ");
			}else if(Type.equals("E")) {
				message.append("Perpanjangan Paket Anda Telah Berhasil ");
			}
			
			if(Type.equals("N") || Type.equals("G")) {
			
				message.append("\n");
				message.append("\n");
				message.append("Username : "+to.getEMail());
				message.append("\n");
				message.append("Password : "+newPass);
				message.append("\n");
				message.append("Data Tersebut Bersifat Rahasia, Mohon Tidak Membagikannya Kepada Pihak Lain.");
				message.append("Terima Kasih.");
			
			}
			message.append("\n");
			message.append("\n");
			message.append("\n");
			message.append("\n");
			message.append("Salam Hangat");
			message.append("\n");
			message.append("\n");
			message.append("Team SemeruLite");
			
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
			if(emailSales != null && !emailSales.isEmpty() && emailSales != "") {
				email.addCc(emailSales);
				
			}
			OK = EMail.SENT_OK.equals(email.send());
			new MUserMail(m_MailText, ListUser.get(i), email).saveEx();
			//
			
			if(OK) {
				
				StringBuilder SQLUpdate = new StringBuilder();
				SQLUpdate.append("UPDATE mob_trx_registration ");
				SQLUpdate.append(" SET IsEmailed = 'Y' ");
				SQLUpdate.append(" WHERE mob_trx_registration_id = "+mob_trx_registration_id);
				
				DB.executeUpdateEx(SQLUpdate.toString(), get_TrxName());
			}
			
			if (OK) {
				if (log.isLoggable(Level.FINE)) log.fine(to.getEMail());
	
			
			} else {
				log.warning("FAILURE - " + to.getEMail());
			}
			StringBuilder msglog = new StringBuilder((OK ? "@OK@" : "@ERROR@")).append(" - ").append(to.getEMail());
			addLog(0, null, null, msglog.toString());
		
	 	}
	 	
		return new Boolean(OK);
	}	//	sendIndividualMail
	
	
	
}
